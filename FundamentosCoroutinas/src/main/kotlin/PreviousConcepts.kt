import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.concurrent.thread
import kotlin.random.Random

fun main(){

    //lambda()

    /*multiLambda(3, 3) { result ->
        println(result)
    }*/

    //threads()

    coroutinesVsThreads()

}

fun coroutinesVsThreads(){
    newTopic("Coroutinas vs Threads")
    runBlocking {
        (1..1_000_000).forEach{
            launch {
                delay(someTime())
                if(it==999_999){
                    println("$it -> ${Calendar.getInstance().let { cal ->
                        cal.timeInMillis = System.currentTimeMillis()
                        "${cal.get(Calendar.HOUR_OF_DAY)}:${cal.get(Calendar.MINUTE)}:${cal.get(Calendar.SECOND)}"
                    }}")
                }
                if(it==1){
                    println("$it -> ${Calendar.getInstance().let { calendar ->
                        calendar.timeInMillis = System.currentTimeMillis()
                        "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}:${calendar.get(Calendar.SECOND)}"
                    }}")
                }
            }
        }
    }
//    (1..1_000_000).forEach {
//        thread{
//            Thread.sleep(someTime())
//            println(it)
//        }
//    }
}

private const val SEPARATOR = "==============="
fun newTopic(topic: String) {
    println("\n$SEPARATOR $topic $SEPARATOR")
}

fun threads(){
    newTopic("Threads")
    println(multiThreads(2, 3))
    multiThreadsLambda(3, 4){
        println("MultiTheadLamda $it")
    }
}

fun multiThreads(x: Int, y: Int): Int {
    var result = 0
    thread {
        Thread.sleep(someTime())
        result = x * y
    }
    Thread.sleep(2_100)
    return result
}

fun multiThreadsLambda(x: Int, y: Int, callback: (result: Int) -> Unit) {
    thread {
        Thread.sleep(someTime())
        callback(multi(x, y))
    }
}

fun someTime(): Long = Random.nextLong(500, 2000)

fun lambda(){
    newTopic("Lambda")
    println(multi(2, 3))
}

fun multiLambda(x: Int, y:Int, callback: (result: Int) -> Unit){
    callback(x*y)
}

fun multi(x: Int, y:Int): Int{
    return x*y
}