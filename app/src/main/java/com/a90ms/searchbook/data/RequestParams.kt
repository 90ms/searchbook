package com.a90ms.searchbook.data

import com.a90ms.searchbook.util.VALUE_DEFAULT_ORDER_BY

data class RequestParams(
    val query: String,
    val orderBy: String? = VALUE_DEFAULT_ORDER_BY,
    val startIndex: Int? = 0
)