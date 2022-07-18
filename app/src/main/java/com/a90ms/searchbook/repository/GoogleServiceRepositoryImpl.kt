package com.a90ms.searchbook.repository

import com.a90ms.searchbook.data.RequestParams
import com.a90ms.searchbook.network.GoogleApisService

class GoogleServiceRepositoryImpl(
    private val service: GoogleApisService
) : GoogleServiceRepository {

    override suspend fun requestSearchBooks(params: RequestParams) = service.getSearchBookList(
        query = params.query,
        orderBy = params.orderBy,
        page = params.startIndex
    )

}