fun main(args: Array<String>) {
    Message("run()").run {
        
    }
}

class Message(val str:String) {
    fun MyPrintln(int:Int) :String {
        return "$int $str"
    }
}