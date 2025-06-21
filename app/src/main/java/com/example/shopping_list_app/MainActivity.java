package com.example.shopping_list_app;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ShoppingDatabase shoppingDatabase;
    private ListView listView;
    private ListViewAdapter adapter;
    private EditText itemInput;
    private ImageView addButton ,removeButton;
    private List<ShoppingItemEntity> itemEntities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupDatabase();
        setupListView();
        setupButtonClick();
        loadContent();

    }

    private void initializeViews() {
        listView = findViewById(R.id.listView);
        itemInput = findViewById(R.id.input);
        addButton = findViewById(R.id.add_items);
        removeButton = findViewById(R.id.remove);
    }

    private void setupDatabase() {
        shoppingDatabase = Room.databaseBuilder(getApplicationContext(),
                ShoppingDatabase.class, "shopping-db").build();
    }

    private void setupListView() {
        adapter = new ListViewAdapter(this, itemEntities, shoppingDatabase);
        listView.setAdapter(adapter);
    }

    private void setupButtonClick() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = itemInput.getText().toString();
                if (text.isEmpty()) {
                    makeToast("Please Enter Item Name");
                } else {
                    addShoppingItem(text);
                    itemInput.setText("");
                }
            }
        });
}

    private void addShoppingItem(final String itemName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ShoppingItemEntity itemEntity = new ShoppingItemEntity(itemName);
                shoppingDatabase.shoppingItemDao().insert(itemEntity);
                loadContent();
            }
        }).start();
    }

    private void loadContent() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                itemEntities.clear();
                itemEntities.addAll(shoppingDatabase.shoppingItemDao().getAllItems());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }


    private void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveContent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveContent();
    }

    private void saveContent() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ShoppingItemEntity> entitiesToSave = new ArrayList<>(itemEntities);
                shoppingDatabase.shoppingItemDao().insert(entitiesToSave.toArray(new ShoppingItemEntity[0]));
            }
        }).start();
    }

    public void addItem(final String text) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ShoppingItemEntity itemEntity = new ShoppingItemEntity(text);
                shoppingDatabase.shoppingItemDao().insert(itemEntity);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        itemEntities.add(itemEntity);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }
}
