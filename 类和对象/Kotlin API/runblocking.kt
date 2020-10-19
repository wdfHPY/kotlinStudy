fun main(args: Array<String>) {
    //GetPostData.getdata()
    GetFileData("test").getdata
}

object GetPostData{
    fun getdata(): String {
        return "hello World"
    }
}

object GetFileData(str:String) {
    fun getdata():String {
        return "hello kotlin"
    }
}