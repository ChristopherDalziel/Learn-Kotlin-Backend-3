package com.example

import io.ktor.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

val portArgName = "--server.port"
val defaultPort = 8080

fun main(args: Array<String>) {
    val portConfigured = args.isNotEmpty() && args[0].startsWith(portArgName)

    val port = if (portConfigured) {
        args[0].split("=").last().trim().toInt()
    } else defaultPort

    embeddedServer(Netty, port, module = Application::main).start(wait = true)
}