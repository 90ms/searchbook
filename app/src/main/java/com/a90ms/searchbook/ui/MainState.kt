package com.a90ms.searchbook.ui

import androidx.paging.PagingData
import com.a90ms.searchbook.model.Items

sealed class MainState {
    data class OnUpdateList(val pagingData: PagingData<Items>) : MainState()
    data class OnError(val msg: String) : MainState()
    object OnScrollTop : MainState()
    data class OnClickItem(val item: Items) : MainState()
}