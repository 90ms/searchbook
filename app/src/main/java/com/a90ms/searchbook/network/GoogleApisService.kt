package com.a90ms.searchbook.network

import com.a90ms.searchbook.model.ResponseBookList
import com.a90ms.searchbook.util.VALUE_DEFAULT_ORDER_BY
import com.a90ms.searchbook.util.VALUE_DEFAULT_PAGE
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleApisService {
    @GET("books/v1/volumes/")
    suspend fun getSearchBookList(
        @Query("q", encoded = true) query: String,
        @Query("startIndex") page: Int? = VALUE_DEFAULT_PAGE,
        @Query("orderBy") orderBy: String? = VALUE_DEFAULT_ORDER_BY,
    ): ResponseBookList
}