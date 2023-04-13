import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

val provincias = listOf("Santander", "Almeria", "Huelva", "Madrid", "Barcelona")

fun main() {
    bufferChannel()
}

fun bufferChannel() {
    runBlocking {
        newTopic("Buffer para channels")
        val time = System.currentTimeMillis()
        val channel = Channel<String>()
        launch {
            provincias.forEach {
                delay(100)
                channel.send(it)
            }
            channel.close()
        }
        launch {
            delay(1000)
            channel.consumeEach {
                println(it)
            }
            println("Time -> ${System.currentTimeMillis()-time}ms")
        }
        val bufferTime = System.currentTimeMillis()
        val bufferChannel = Channel<String>(2)
        launch {
            provincias.forEach {
                delay(100)
                bufferChannel.send(it)
            }
            bufferChannel.close()
        }
        launch {
            delay(1000)
            bufferChannel.consumeEach {
                println(it)
            }
            println("B Time -> ${System.currentTimeMillis()-bufferTime}ms")
        }
    }
}

fun pipelines(){
    runBlocking {
        newTopic("Pipelines")
        val citiesChannel = produceCities()
        val foodChannel = produceFoods(citiesChannel)
        foodChannel.consumeEach { println(it) }
        citiesChannel.cancel()
        foodChannel.cancel()
        newTopic("Fin de comida")
    }
}

fun CoroutineScope.produceFoods(
    cities: ReceiveChannel<String>
): ReceiveChannel<String> = produce {
    for(city in cities){
        val food = getFoodByCity(city)
        send("$food en $city")
    }
}

suspend fun getFoodByCity(city: String): String  {
    delay(300)
    return when(city){
        "Huelva" -> "Orejones de habas"
        "Almeria" -> "Bacalao con tomate"
        "Santander" -> "Sobao"
        else -> "No saben comer bien"
    }
}

fun produceChannel() {
    runBlocking {
        newTopic("Canales y el patrón productor-consumidor")
        produceCities().consumeEach {
            println(it)
        }
    }
}

fun CoroutineScope.produceCities(): ReceiveChannel<String> = produce {
    provincias.forEach {
        send(it)
    }
}

fun basicChannel() {
    runBlocking {
        newTopic("Canal básico")
        //Es una forma de transferir un flujo de valores entre coroutinas
        val channel = Channel<String>()
        launch {
            provincias.forEach {
                channel.send(it)
            }
        }

        for (value in channel) {
            println(value)
        }
    }
}

fun closeChannel() {
    runBlocking {
        newTopic("Cerrar canal básico")
        //Es una forma de transferir un flujo de valores entre coroutinas
        val channel = Channel<String>()
        launch {
            provincias.forEach {
                channel.send(it)
//                if(it=="Madrid"){
//                    channel.close()
//                    return@launch
//                }
            }
            channel.close()
        }


//        for (value in channel) {
//            println(value)
//        }
        while(!channel.isClosedForReceive){
            println(channel.receive())
        }
//        channel.consumeEach {
//            println(it)
//        }
    }
}



