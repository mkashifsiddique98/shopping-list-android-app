package com.example.shopping_list_app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    private List<ShoppingItemEntity> itemEntities;
    private Context context;
    private Activity activity;
    private ShoppingDatabase shoppingDatabase;
    private LayoutInflater inflater;

    public ListViewAdapter(Activity activity, List<ShoppingItemEntity> itemEntities, ShoppingDatabase shoppingDatabase) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.itemEntities = itemEntities;
        this.shoppingDatabase = shoppingDatabase;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return itemEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return itemEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_row, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Update UI with data from itemEntities
        ShoppingItemEntity currentItem = itemEntities.get(position);
        holder.bind(currentItem, position);

        return convertView;
    }

    private void removeItem(final int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ShoppingItemEntity itemEntity = itemEntities.get(position);
                shoppingDatabase.shoppingItemDao().deleteItem(itemEntity);

                // Remove item from the list on the UI thread
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        itemEntities.remove(position);
                        notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    private class ViewHolder {
        TextView number;
        TextView name;
        ImageView remove;

        ViewHolder(View view) {
            number = view.findViewById(R.id.number);
            name = view.findViewById(R.id.name);
            remove = view.findViewById(R.id.remove);

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeItem((int) remove.getTag());
                }
            });
        }

        void bind(ShoppingItemEntity item, int position) {
            number.setText(String.format("%d. ", position + 1));
            name.setText(item.getItemName());
            remove.setTag(position);
        }
    }
}
