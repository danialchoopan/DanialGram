package ir.danialchoopan.danialgram.action

class EndPoints {
    companion object {
        val URL = "http://192.168.43.252:8585/"
        val HOME = URL + "api/"
        val LOGIN = HOME + "login"
        val REGISTER = HOME + "register"
        val LOGOUT = HOME + "logout"
        val SAVE_USER_INFO = HOME + "userinfo"
        val GET_POSTS = HOME + "posts"
        val CHECK_TOKEN = HOME + "userinfo"
        val DELETE_POST = HOME + "posts/"
        val LIKE_POST = HOME + "likes"
        val COMMENTS_POST = HOME + "comments/"
        val DELETE_COMMENT = HOME + "comments/"
        val USER_POSTS = HOME + "user/posts"
    }
}