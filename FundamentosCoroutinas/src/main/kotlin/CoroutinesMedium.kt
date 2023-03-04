import kotlinx.coroutines.*

fun main(){
    //dispatcher()
    //nested()
    sequences()
}

fun sequences() {
    newTopic("Secuence")
    getDataBySeq().forEach {
        println("$it")
    }
}

fun getDataBySeq(): Sequence<Float> {
    return sequence {
        (1..5).forEach {
            println("procesando datos...")
            Thread.sleep(someTime())
            yield(20 + it + kotlin.random.Random.nextFloat())
        }
    }
}

fun nested() {
    runBlocking {
        newTopic("Anidar Coroutinas")
        val job = launch {
            startMsg()
            launch {
                startMsg()
                delay(someTime())
                println("Otra tarea")
                endMsg()
            }
            launch(Dispatchers.IO) {
                startMsg()
                launch(newSingleThreadContext("\"Curso coroutinas\"")) {
                    startMsg()
                    println("tarea curso")
                    endMsg()
                }
                delay(someTime())
                println("Tarea en el servidor")
                endMsg()
            }
            endMsg()
        }
    }
}

fun dispatcher(){
    runBlocking {
        newTopic("Dispatchers")
        launch {
            startMsg()
            println("NONE")
            endMsg()
        }
        launch(Dispatchers.IO) {
            //IO - Lo usamos para conexion de bases de datos locales o remotas, escritura de ficheros o peticiones rest
            startMsg()
            println("IO")
            endMsg()
        }
        launch(Dispatchers.Unconfined) {
            //Unconfined - Un despachador de corrutina que no se limita a ningún hilo específico.
            startMsg()
            println("Unconfined")
            endMsg()
        }
        launch(Dispatchers.Default) {
            //Default - Se usa para hacer tareas de larga duracion, como procesamiento de images...
            startMsg()
            println("Default")
            endMsg()
        }
        withContext(Dispatchers.Default){
            startMsg()
            println("Unconfined")
            endMsg()
        }
    }
}