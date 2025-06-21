package com.example.shopping_list_app;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ShoppingItemEntity.class}, version = 1)
public abstract class ShoppingDatabase extends RoomDatabase {
    public abstract ShoppingItemDao shoppingItemDao();
}
