package com.yyn.task_2

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import java.io.IOException

class PhotoSearchPagingSource(
    private val apiService: ApiService,
    private val query: String
) : PagingSource<Int, Photo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val page = params.key ?: 1
        val perPage = params.loadSize

        return try {
            val response = apiService.searchPhotos(query, page, perPage)
            val photos = response.body()?.photos?.photo
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (photos.isNullOrEmpty()) null else page + 1

            LoadResult.Page(
                data = photos ?: emptyList(),
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        TODO("Not yet implemented")
    }
}
