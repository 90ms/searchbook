package com.a90ms.searchbook.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.a90ms.searchbook.model.Items
import com.a90ms.searchbook.repository.GoogleServiceRepository
import com.a90ms.searchbook.util.VALUE_DEFAULT_SIZE
import javax.inject.Inject

class SearchBookPagingSource @Inject constructor(
    private val requestParams: RequestParams,
    private val repository: GoogleServiceRepository
) : PagingSource<Int, Pair<Items, Int>>() {

    override fun getRefreshKey(state: PagingState<Int, Pair<Items, Int>>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pair<Items, Int>> {
        val page = params.key ?: 0
        return try {
            val dto = repository.requestSearchBooks(requestParams.copy(startIndex = page))

            val items = dto.items
            val nextPage = page + VALUE_DEFAULT_SIZE

            LoadResult.Page(
                data = items.map { Pair(it, dto.totalItems) },
                prevKey = null,
                nextKey = if (dto.totalItems <= nextPage) null else nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
