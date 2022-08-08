package com.a90ms.searchbook.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a90ms.mstoast.msToast
import com.a90ms.searchbook.BR
import com.a90ms.searchbook.R
import com.a90ms.searchbook.base.BaseActivity
import com.a90ms.searchbook.base.BaseSingleViewPagingAdapter
import com.a90ms.searchbook.databinding.ActivityMainBinding
import com.a90ms.searchbook.model.Items
import com.a90ms.searchbook.ui.detail.BookDetailActivity
import com.a90ms.searchbook.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var pagingAdapter: BaseSingleViewPagingAdapter<Items>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupData()
        setupLoadingObserver(viewModel)
        setupObserver()
        setupRecyclerView()
        setupListener()
    }

    private fun setupData() {
        with(binding) {
            vm = viewModel
        }
    }

    private fun setupObserver() {
        val owner = this
        viewModel.run {
            state.observe(owner) {
                when (it) {
                    is MainState.OnUpdateList -> lifecycleScope.launch {
                        pagingAdapter.submitData(it.pagingData)
                    }
                    is MainState.OnError -> {
                        msToast(it.msg)
                    }
                    MainState.OnScrollTop -> {
                        msToast("최 상단으로 이동",1000)
                        binding.rvBook.scrollToPosition(0)
                    }
                    is MainState.OnClickItem -> {
                        startActivity<BookDetailActivity>(
                            BookDetailActivity.createBundle(it.item)
                        )
                    }
                }
            }
            maxCount.observe(owner) {
                lifecycleScope.launch {
                    if (it == 0) {
                        pagingAdapter.submitData(PagingData.empty())
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvBook.run {
            val diffUtil = object : DiffUtil.ItemCallback<Items>() {
                override fun areItemsTheSame(
                    oldItem: Items,
                    newItem: Items
                ) = oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: Items,
                    newItem: Items
                ) = oldItem == newItem
            }

            this@MainActivity.pagingAdapter = BaseSingleViewPagingAdapter(
                layoutResourceId = R.layout.item_book,
                bindingItemId = BR.item,
                viewModel = mapOf(BR.vm to viewModel),
                diffUtil = diffUtil
            ).apply {
                setupSourceLoadStateListener(
                    scope = lifecycleScope,
                    isLoading = {
                        isLoadingState(it)
                    },
                    scrollTop = {
                        scrollToPosition(0)
                    },
                    isListEmpty = {
                        binding.tvEmpty.isVisible = it
                        binding.rvBook.isVisible = !it
                    }
                )
            }
            adapter = this@MainActivity.pagingAdapter

            addItemDecoration(RecyclerViewDividerDecoration())

            addOnScrollListener(
                object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        val lm = layoutManager as? LinearLayoutManager ?: return
                        viewModel.updateTopButton(lm.findFirstVisibleItemPosition() != 0)
                    }
                }
            )
        }
    }

    private fun setupListener() {
        with(binding) {
            tilSearch.setEndIconOnClickListener {
                viewModel.clearSearchQuery()
                showKeyboard(binding.tieSearch)
            }
            tieSearch.setOnEditorActionListener { v, actionId, event ->
                var handled = false

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    when {
                        v.text.isNullOrBlank() -> toast(getString(R.string.toast_empty_search))
                        v.text.isEmpty() -> toast(getString(R.string.toast_search_error))
                        else -> {
                            hideKeyboard(v)
                            viewModel.searchData()
                        }
                    }
                    handled = true
                }

                handled
            }
        }
    }

    override fun onBackPressed() {
        showCommonDialog(
            message = getString(R.string.dialog_title_finish),
            negative = getString(R.string.dialog_no),
            positive = getString(R.string.dialog_yes)
        ) { _, which ->
            if (which == DialogInterface.BUTTON_POSITIVE) {
                finish()
            }
        }
    }
}