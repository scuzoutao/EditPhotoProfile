package com.azwar.editphotoprofile

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform