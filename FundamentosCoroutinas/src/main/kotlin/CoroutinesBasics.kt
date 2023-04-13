import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce

fun main() {
    globalScope()
//    suspendFun()
//
//    newTopic("Constructores de coroutinas")
//    cRunBlocking()
//    cLaunch()
//    cAsync()
    //cProduce()

    //job()

    //deferred()

    //readln()
}

fun cProduce() = runBlocking {
    newTopic("Produce")
    val names = produceNames()
    names.consumeEach { println(it) }
}

fun CoroutineScope.produceNames(): ReceiveChannel<String> = produce{
    (1..5).forEach { send("name: $it") }
}

fun deferred() {
    runBlocking {
        newTopic("Deferred")
        val deferred = async {
            startMsg()
            delay(someTime())
            println("deferred...")
            endMsg()
            multi(2,5)
        }
        println("Deferred -> $deferred")
        println("Valor del Deferred.await -> ${deferred.await()}")
    }
}

fun job() {
    runBlocking {
        newTopic("Job")
        val job = launch {
            startMsg()
            delay(someTime())
            println("job...")
            endMsg()
        }
        delay(2_100)
        println("Job -> $job")
        println("Job active-> ${job.isActive}")
        println("Job cancel-> ${job.isCancelled}")
        println("Job completed-> ${job.isCompleted}")
    }
}

fun cAsync() {
    runBlocking {
        newTopic("Async")
        val result = async {
            startMsg()
            delay(someTime())
            println("async...")
            endMsg()
            1
        }
        println("Result async -> ${result.await()}")
    }
}

fun cLaunch() {
    runBlocking {
        newTopic("Launch")
        launch {
            startMsg()
            delay(someTime())
            println("launch...")
            endMsg()
        }
    }
}

fun cRunBlocking() {
    newTopic("Run Blocking")
    runBlocking {
        startMsg()
        delay(someTime())
        println("runBlocking...")
        endMsg()
    }
}

fun suspendFun() {
    newTopic("Suspend")
    Thread.sleep(someTime())
    GlobalScope.launch {
        delay(someTime())
    }
}

fun globalScope() {
    newTopic("Global Scope")
    println(
        """
        Global Scope -> Nos permite que cada coroutina siga en curso mientras la aplicacion este viva
    """.trimIndent()
    )
    GlobalScope.launch {
        startMsg()
        delay(someTime())
        println("Mi Coroutina")
        endMsg()
    }
}

fun startMsg() {
    println("Comenzando coroutina - ${Thread.currentThread().name}-")
}

fun endMsg() {
    println("Coroutina - ${Thread.currentThread().name} - Finalizada")
}
