package com.example

import com.example.repo.Game
import com.example.repo.GameRepo
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import kotlinx.html.*
import java.text.DateFormat
import java.time.Duration

// Configure our server - routes, middleware etc.

const val REST_ENDPOINT = "/game"

fun Application.main() {
    install(DefaultHeaders)
    install(CORS) {
        maxAge = Duration.ofDays(1)
    }
    install(ContentNegotiation) {
        gson { setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }

    routing {
        get("$REST_ENDPOINT/{id}") {
            errorAware { val id = call.parameters["id"] ?: throw IllegalArgumentException("Parameter ID not found")
            call.respond(GameRepo.get(id))}
        }

        get(REST_ENDPOINT) {
            errorAware { call.respond(GameRepo.getAll()) }
        }

        delete("$REST_ENDPOINT/{id}") {
            errorAware {
                val id = call.parameters["id"] ?: throw IllegalArgumentException("Parameter ID not found")
                call.respondSuccessJson(GameRepo.remove(id))
            }
        }

        delete(REST_ENDPOINT) {
            errorAware { GameRepo.clear()
            call.respondSuccessJson()}
        }

        post(REST_ENDPOINT) {
            errorAware {
                val receive = call.receive<Game>()
                println("Received post request: $receive")
                call.respond(GameRepo.add(receive))
            }
        }

        // Html
        get("/") {
            call.respondHtml {
                head {
                    title("Kotlin API")
                }
                body {
                    div {
                        h1 {
                            + "Welcome to the Games API"
                        }
                        p {
                            + "Go to '/game' to begin using the API"
                        }
                    }
                }
            }
        }
    }
}


// Extension functions

private suspend fun <R> PipelineContext<*, ApplicationCall>.errorAware(block: suspend () -> R): R? {
    return try {
        block()
    } catch (e: Exception) {
        call.respondText("""{"error":"$e"}"""
            , ContentType.parse("application/json")
            , HttpStatusCode.InternalServerError)
        null
    }
}

private suspend fun ApplicationCall.respondSuccessJson(value: Boolean = true) =
    respond("""{"success": "$value"}""")