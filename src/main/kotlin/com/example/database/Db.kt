package com.example.database

import org.ktorm.database.Database

object Db{
    val database = Database.connect(url = "jdbc:mysql://localhost:3306/ktor", driver = "com.mysql.cj.jdbc.Driver"
        ,user = "root", password = "root")
}