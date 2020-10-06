package com.example.notes2

import androidx.room.*

@Entity(tableName = "shopping_item" )
data class ShoppingItem(
    val name: String,
    val details: String?,
    val urlPhoto: String?,
    @PrimaryKey(autoGenerate = true) var uid: Long = 0
)

@Dao
interface ShoppingItemDao {
    @Query("SELECT * FROM shopping_item")
    fun getAll(): List<ShoppingItem>

    @Query("SELECT * FROM shopping_item WHERE uid = :itemId")
    fun getItemById(itemId: Long): ShoppingItem

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg items: ShoppingItem): List<Long>

    @Update
    fun update(item: ShoppingItem)

    @Delete
    fun delete(item: ShoppingItem)

    @Query("DELETE FROM shopping_item WHERE uid = :userId")
    fun deleteByUserId(userId: Long)
}