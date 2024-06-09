package com.example.bloom.utils

fun String.toCamelCase(): String {
    return this.split(" ").joinToString(" ") { it.capitalize() }
}