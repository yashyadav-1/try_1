package com.yyn.task_2

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import java.io.IOException

class PhotoPagingSource(
    private val apiService: ApiService,
    private val query: String = ""
) : PagingSource<Int, Photo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        try {
            val page = params.key ?: 1
            val response = apiService.searchPhotos(query, page, params.loadSize)

            if (response.isSuccessful) {
                val photoResponse = response.body()

                val photos: List<Photo> = photoResponse?.data ?: emptyList()

                val prevPage = if (page > 1) page - 1 else null
                val nextPage = if (photos.isNotEmpty()) page + 1 else null

                return LoadResult.Page(
                    data = photos,
                    prevKey = prevPage,
                    nextKey = nextPage
                )
            } else {
                return LoadResult.Error(Exception("Failed to fetch data"))
            }
        } catch (exception: IOException) {
            // Handle IOException for network failures
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            // Handle HttpException for non-2xx HTTP status codes
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        // We don't need a refresh key in this case, so return null
        return null
    }
}
