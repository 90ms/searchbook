package com.a90ms.searchbook.repository

import com.a90ms.searchbook.data.RequestParams
import com.a90ms.searchbook.model.ResponseBookList

interface GoogleServiceRepository {

    suspend fun requestSearchBooks(params: RequestParams): ResponseBookList
}