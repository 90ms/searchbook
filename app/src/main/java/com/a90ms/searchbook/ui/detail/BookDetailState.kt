package com.a90ms.searchbook.ui.detail

sealed class BookDetailState {
    data class OnClickBuyLink(val url: String?): BookDetailState()
}