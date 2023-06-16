package com.example

import com.example.model.userModel.UserCredentials
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

fun main() = runBlocking{
    var job = launch (Dispatchers.Default){
        println("in 8080 ${Thread.currentThread().name}")
        embeddedServer(Netty, port = 8080, host = "localHost", module = Application::module).start(wait = true)
    }
    var job1 = launch(Dispatchers.Default) {
        println("in 8081 ${Thread.currentThread().name}")
        embeddedServer(Netty, port = 8081, host = "localHost", module = Application::start).start(wait = true)
    }
    job.join()
    job1.join()


}

fun Application.start(){

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
    install(RateLimit){
        register(RateLimitName("public")){
            rateLimiter(limit = 2, refillPeriod = 60.seconds)
        }
    }
    install(StatusPages){
        status(HttpStatusCode.TooManyRequests){
            call,status -> val retryAfter = call.response.headers["Retry-After"]
            call.respondText(text = "429: Too many requests. Wait for $retryAfter seconds.", status = status)
        }
    }
    configureRouting()

}
fun Application.module() {

    install(RequestValidation) {
        validate<UserCredentials> { it ->
            if (it.username.length < 5) {
                ValidationResult.Invalid("Name Should Have Min 5 Characters")
            } else {
                ValidationResult.Valid
            }
        }
    }
    install(StatusPages) {
        exception<RequestValidationException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, cause.reasons.toString())
        }
    }

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
    install(RateLimit){
        register(RateLimitName("public")){
            rateLimiter(limit = 2, refillPeriod = 60.seconds)
        }
    }



    configureRouting()

}

//    println("Main Thread Starts ${Thread.currentThread().name}")
//    var job = launch {
//        println("In Coroutine ${Thread.currentThread().name}")
//        while (true){
//            println("AS")
//            yield()
//        }
//    }
//
//    var j = life
//
//    println("Main Thread Ends ${Thread.currentThread().name}")
//
//}
//suspend fun networkCall1(i:Long){
//    delay(i)
//}
//suspend fun networkCall2(i:Long){
//    delay(i)
//}


