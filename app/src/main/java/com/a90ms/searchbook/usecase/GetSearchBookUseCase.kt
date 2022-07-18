package com.a90ms.searchbook.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.a90ms.searchbook.base.IoDispatcher
import com.a90ms.searchbook.data.RequestParams
import com.a90ms.searchbook.data.SearchBookPagingSource
import com.a90ms.searchbook.model.Items
import com.a90ms.searchbook.repository.GoogleServiceRepository
import com.a90ms.searchbook.util.UseCase
import com.a90ms.searchbook.util.VALUE_DEFAULT_SIZE
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchBookUseCase @Inject constructor(
    private val repository: GoogleServiceRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<RequestParams, Flow<PagingData<Pair<Items, Int>>>>(
    coroutineDispatcher = dispatcher
) {

    override suspend fun execute(
        parameters: RequestParams
    ): Flow<PagingData<Pair<Items,Int>>> = Pager(
        config = PagingConfig(
            pageSize = VALUE_DEFAULT_SIZE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            SearchBookPagingSource(
                requestParams = parameters,
                repository = repository,
            )
        }
    ).flow
}
