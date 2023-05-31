package com.yyn.task_2

data class PhotoResponse(
    val photos: Photos
) {
    val data: List<Photo>
        get() {
            return photos.photo
        }
}

data class Photos(
    val photo: List<Photo>
)

data class Photo(
    val id: String,
    val title: String,
    val url: String
)
