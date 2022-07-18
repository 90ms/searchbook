package com.a90ms.searchbook.model

data class ResponseBookList(
    val kinds: String,
    val items: List<Items>,
    val totalItems: Int
)
