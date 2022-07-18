package com.a90ms.searchbook.ui

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.a90ms.searchbook.base.BaseViewModel
import com.a90ms.searchbook.base.onError
import com.a90ms.searchbook.base.onException
import com.a90ms.searchbook.base.onSuccess
import com.a90ms.searchbook.data.RequestParams
import com.a90ms.searchbook.model.Items
import com.a90ms.searchbook.usecase.GetSearchBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val searchBookUseCase: GetSearchBookUseCase
) : BaseViewModel() {

    private val _state = MutableLiveData<MainState>()
    val state: LiveData<MainState> get() = _state

    /**
     * totalItems 이상함
     * */
    private val _maxCount = MutableLiveData<Int>()
    val maxCount: LiveData<Int> get() = _maxCount

    private val _showTopButton = MutableLiveData(false)
    val showTopButton: LiveData<Boolean> get() = _showTopButton

    val searchQuery = ObservableField<String>()

    fun searchData() = viewModelScope.launch {
        _maxCount.value = 0
        searchBookUseCase(RequestParams(query = searchQuery.get() ?: ""))
            .onSuccess { it ->
                it.map {
                    it.map { pair ->
                        _maxCount.value = pair.second ?: 0
                        pair.first
                    }
                }.cachedIn(viewModelScope).collect {
                    _state.value = MainState.OnUpdateList(it)
                }
            }.onError { code, message ->
                _state.value = MainState.OnError("onError[$code]$message")
            }.onException {
                _state.value = MainState.OnError("onException$it")
            }
    }

    fun clearSearchQuery() {
        searchQuery.set("")
    }

    fun updateTopButton(value: Boolean) {
        _showTopButton.value = value
    }

    fun onClickTop() {
        _state.value = MainState.OnScrollTop
    }

    fun onClickItem(item: Items) {
        _state.value = MainState.OnClickItem(item)
    }
}