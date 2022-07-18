package com.a90ms.searchbook.data

enum class OrderByType(val value: String) {

    RELEVANCE("relevance"), NEWEST("newest");

    companion object {
        @JvmStatic
        fun creator(value: String): OrderByType =
            values().firstOrNull { it.value == value.uppercase() } ?: RELEVANCE
    }
}