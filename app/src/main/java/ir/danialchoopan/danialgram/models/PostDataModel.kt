package ir.danialchoopan.danialgram.models

data class PostDataModel(
    val id: Int,
    var likes: Int,
    val comments: Int,
    val date: String,
    val description: String,
    val photo: String,
    val user: UserModel,
    var selfLike: Boolean
)
