package io.shubham0204.model2vec

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
