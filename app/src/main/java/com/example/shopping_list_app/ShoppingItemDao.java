package com.example.shopping_list_app;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ShoppingItemDao {
    @Query("SELECT * FROM shopping_items")
    List<ShoppingItemEntity> getAllItems();

    @Insert
    void insert(ShoppingItemEntity... items);

    @Delete
    void deleteItem(ShoppingItemEntity item);
}
