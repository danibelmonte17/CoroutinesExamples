import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.random.Random
import kotlin.system.measureTimeMillis

fun main() {
    //basicFlows()
    //operator()
    //terminalFlowOperators()
    //bufferFlow()
    //conflationFlow()
    //multiFlow()
    flatFlows()
}

fun flatFlows() {
    runBlocking {
        newTopic("Flujos de aplanamiento")
        newTopic("FlatMapConcat")
        getCitiesFlow().flatMapConcat { city -> //Flow<Flow<TYPE>>
            getDataFlatFlow(city)
        }.map {
            setFormat(it)
        }.collect{ println(it) }
        newTopic("FlatMapMerge")
        getCitiesFlow().flatMapMerge { city -> //Flow<Flow<TYPE>>
            getDataFlatFlow(city)
        }.map {
            setFormat(it)
        }.collect{ println(it) }
    }
}

fun getDataFlatFlow(city: String): Flow<Float> = flow {
    //(1..3).forEach {
        println("Temperatura de ayer en $city")
        emit(Random.nextInt(10, 30).toFloat())
        println("Temperatura actual en $city")
        delay(100)
        emit(20 + 1 + Random.nextFloat())
    //}
}

fun getCitiesFlow() : Flow<String> = flow {
    listOf("Santander", "Almeria", "Huelva","Madrid","Barcelona").forEach { city ->
        delay(1000)
        emit(city)
    }
}

fun multiFlow(){
    runBlocking {
        newTopic("Zip")
        getDataByFlow()
            .map { setFormat(it) }
            .zip(getMatchResultsFlow()){ degrees, result ->
                "$result degree: $degrees"
            }.collect{
                println(it)
            }
        newTopic("Combine")
        getDataByFlow()
            .map { setFormat(it) }
            .combine(getMatchResultsFlow()){ degrees, result ->
                "$result degree: $degrees"
            }.collect{
                println(it)
            }
    }
}

fun conflationFlow(){
    runBlocking {
        newTopic("Fusion")
        val time = measureTimeMillis {
            getMatchResultsFlow()
                .conflate() //5172
                //.buffer() //9691
                .collect { //14599
                    delay(100)
                    println(it)
                }
        }
        println("Time $time miliseconds")
    }
}

fun getMatchResultsFlow(): Flow<String>{
    return flow {
        var homeTeam = 0
        var awayTeam = 0
        (0..90).forEach {
            println("Minuto: $it")
            delay(50)
            homeTeam+=Random.nextInt(0, 21)/20
            awayTeam+=Random.nextInt(0, 21)/20
            emit("$homeTeam - $awayTeam")
        }
    }
}

fun bufferFlow() {
    runBlocking {
        newTopic("Sin Buffer Flow")
        val time1 = measureTimeMillis {
            getDataByFlow()
                .map { setFormat(it) }
                .collect{
                    delay(100)
                    println(it)
                }
        }
        println("Time -> ${time1/1000} seconds")
        newTopic("Buffer Flow")
        val time2 = measureTimeMillis {
            getDataByFlow()
                .map { setFormat(it) }
                .buffer()
                .collect{
                    delay(100)
                    println(it)
                }
        }
        println("Time -> ${time2/1000} seconds")
    }
}

fun basicFlows() {
    newTopic("Flows Basicos")
    runBlocking {
        launch {
            getDataByFlow().collect {
                println("Num -> $it")
            }
        }
        launch {
            (1..5).forEach {
                delay(someTime() / 10)
                println("Num 2... -> $it")
            }
        }
    }
}

fun getDataByFlow(): Flow<Float> {
    return flow {
        (1..25).forEach {
            println("procesando datos...")
            delay(someTime()/10)
            emit(20 + it + kotlin.random.Random.nextFloat())
        }
    }
}

fun setFormat(temp: Float, degree: String = "ºC"): String = String.format(
    Locale.getDefault(),
    "%.1f$degree", temp
)

fun operator() {
    runBlocking {
        newTopic("Operadores intermediarios")
        getDataByFlow().filter {
            it > 23
        }.map {
            setFormat(it)
        }.collect {
            println(it)
        }
        newTopic("Transform")
        getDataByFlow().transform {
            emit(setFormat(it))
            emit(setFormat(it, "ºF"))
        }.collect{
            println(it)
        }
    }
}

fun terminalFlowOperators(){
    runBlocking {
        newTopic("Operadores terminales")
        newTopic("List")
        println("List -> ${getDataByFlow().toList()}")
        newTopic("Single")
        println("Single -> ${getDataByFlow().take(1).single()}")
        newTopic("First")
        println("First -> ${getDataByFlow().first()}")
        newTopic("Last")
        println("Last -> ${getDataByFlow().last()}")
        newTopic("Reduce")
        println("Reduce -> ${getDataByFlow().reduce{
            accumulator, value ->  
            println("Accumulator -> $accumulator")
            println("Value -> $value")
            accumulator + value
        }}")
        newTopic("Fold")
        println("Fold -> ${getDataByFlow().fold(0) { acc, value ->
            println("Accumulator -> $acc")
            println("Value -> $value")
            (acc + value).toInt()
        }
        }")
    }
}