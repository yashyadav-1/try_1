package com.yyn.task_2

//class PhotoViewModelFactory {
//}

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.yyn.task_2.ApiService
import kotlinx.coroutines.flow.Flow

class PhotoViewModel(private val apiService: ApiService) : ViewModel() {
    constructor() : this(ApiService.create())

    fun searchPhotos(query: String): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                PhotoPagingSource(
                    apiService,
                    query
                ) as PagingSource<Int, Photo>
            }
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

    class PhotoViewModelFactory(private val apiService: ApiService) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PhotoViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PhotoViewModel(apiService) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

