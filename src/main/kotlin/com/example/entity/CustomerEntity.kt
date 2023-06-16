package com.example.entity

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object CustomerEntity:Table<Nothing>("customers") {
    val id = int("id").primaryKey()
    val name = varchar("name")
    val age = int("age")
}