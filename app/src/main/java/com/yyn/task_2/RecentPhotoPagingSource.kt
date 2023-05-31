package com.yyn.task_2

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yyn.task_2.ApiService
import com.yyn.task_2.Photo
import retrofit2.HttpException
import java.io.IOException

class RecentPhotoPagingSource(
    private val apiService: ApiService
) : PagingSource<Int, Photo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val page = params.key ?: 1
        val perPage = params.loadSize

        return try {
            val response = apiService.getRecentPhotos(page, perPage)
            if (response.isSuccessful) {
                val photos = response.body()?.photos?.photo
                val prevKey = if (page > 1) page - 1 else null
                val nextKey = if (photos?.isNotEmpty() == true) page + 1 else null

                LoadResult.Page(
                    data = photos ?: emptyList(),
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            } else {
                LoadResult.Error(IOException("Failed to load recent photos: ${response.message()}"))
            }
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
