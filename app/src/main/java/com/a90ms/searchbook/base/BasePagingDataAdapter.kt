package com.a90ms.searchbook.base

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModel
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.a90ms.searchbook.util.COMMON_ERROR_MESSAGE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BasePagingDataAdapter<ITEM : Any, VH : RecyclerView.ViewHolder>(
    diffUtil: DiffUtil.ItemCallback<ITEM>
) : PagingDataAdapter<ITEM, VH>(diffUtil) {

    private var previousLoading: Boolean? = null

    private fun setupScrollTop(
        scope: CoroutineScope,
        scrollTop: () -> Unit
    ) {
        scope.launch {
            loadStateFlow
                .dropWhile {
                    it.refresh is LoadState.NotLoading &&
                            it.append is LoadState.NotLoading &&
                            it.prepend is LoadState.NotLoading
                }
                .distinctUntilChangedBy { it.refresh }
                .collect {
                    if (it.refresh is LoadState.NotLoading) {
                        scrollTop()
                    }
                }
        }
    }

    /**
     * @param isLoading : 새로고침 로딩 상태
     * @param isListEmpty : 현재 리스트 사이즈가 0인지 확인
     * @param isError : 데이터 로드 에러
     */
    fun setupSourceLoadStateListener(
        scope: CoroutineScope,
        isLoading: ((Boolean) -> Unit)? = null,
        isListEmpty: ((Boolean) -> Unit)? = null,
        scrollTop: (() -> Unit)? = null,
        isError: ((String) -> Unit)? = null
    ) {
        scrollTop?.let { setupScrollTop(scope, it) }

        scope.launch {
            loadStateFlow
                .dropWhile {
                    it.refresh is LoadState.NotLoading &&
                            it.append is LoadState.NotLoading &&
                            it.prepend is LoadState.NotLoading
                }
                .distinctUntilChanged()
                .collect {
                    val loading = it.refresh is LoadState.Loading
                    if (previousLoading != loading) {
                        isLoading?.invoke(loading)
                        previousLoading = loading
                    }

                    if (loading.not()) {
                        isListEmpty?.invoke(itemCount == 0)
                    }

                    val errorState = it.append as? LoadState.Error
                        ?: it.prepend as? LoadState.Error
                        ?: it.refresh as? LoadState.Error
                    errorState?.let { isError?.invoke(it.error.message ?: COMMON_ERROR_MESSAGE) }
                }
        }
    }
}

class BaseSingleViewPagingAdapter<ITEM : Any>(
    @LayoutRes private val layoutResourceId: Int,
    private val bindingItemId: Int,
    private val viewModel: Map<Int, ViewModel>,
    diffUtil: DiffUtil.ItemCallback<ITEM>
) : BasePagingDataAdapter<ITEM, BaseViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        BaseViewHolder(
            parent = parent,
            layoutResourceId = layoutResourceId,
            bindingItemId = bindingItemId,
            viewModel = viewModel
        )

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}