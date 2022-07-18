package com.a90ms.searchbook.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.a90ms.searchbook.base.BaseViewModel
import com.a90ms.searchbook.model.Items
import com.a90ms.searchbook.ui.detail.BookDetailActivity.Companion.BUNDLE_ITEMS
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _state = MutableLiveData<BookDetailState>()
    val state: LiveData<BookDetailState> get() = _state

    private val _expanded = MutableLiveData(false)
    val expanded: LiveData<Boolean> get() = _expanded

    val item = savedStateHandle.getLiveData<Items>(BUNDLE_ITEMS)

    fun onClickLink(url: String?) {
        _state.value = BookDetailState.OnClickBuyLink(url)
    }

    fun onClickExpand() {
        _expanded.value = !expanded.value!!
    }
}