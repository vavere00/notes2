package com.example.notes2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 1, entities = [ShoppingItem::class])
abstract class ShoppingDatabase : RoomDatabase() {

    abstract fun shoppingItemDao(): ShoppingItemDao

}

object Database {

    private var instance: ShoppingDatabase? = null

    fun getInstance(context: Context) = instance ?: Room.databaseBuilder(
        context.applicationContext, ShoppingDatabase::class.java, "shopping-db"
    )
        .allowMainThreadQueries()
        .build()
        .also { instance = it }
}