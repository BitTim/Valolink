package dev.bittim.valolink

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform