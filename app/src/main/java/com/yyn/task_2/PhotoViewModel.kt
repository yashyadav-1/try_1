package com.yyn.task_2

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow

class PhotoViewModel(private val apiService: ApiService) : ViewModel() {
    constructor() : this(ApiService.create())

    fun searchPhotos(query: String): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PhotoPagingSource(apiService, query) as PagingSource<Int, Photo> }
        ).flow
    }

    fun getRecentPhotos(): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { RecentPhotoPagingSource(apiService) as PagingSource<Int, Photo> }
        ).flow
    }
}
