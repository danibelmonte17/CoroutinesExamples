package com.example.fundamentoscoroutinas

fun main(){

    lambda()

    multiLambda(3, 3) { result ->
        println(result)
    }

}

fun lambda(){
    println(multi(2, 3))
}

fun multiLambda(x: Int, y:Int, callback: (result: Int) -> Unit){
    callback(x*y)
}

fun multi(x: Int, y:Int): Int{
    return x*y
}