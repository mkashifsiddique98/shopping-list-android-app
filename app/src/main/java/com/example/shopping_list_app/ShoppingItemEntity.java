package com.example.shopping_list_app;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "shopping_items")
public class ShoppingItemEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    private String itemName;

    public ShoppingItemEntity(String itemName) {
        this.itemName = itemName;
    }

    public int getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }
}
