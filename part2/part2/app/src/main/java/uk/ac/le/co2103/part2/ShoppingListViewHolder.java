package uk.ac.le.co2103.part2;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.concurrent.Executors;

//To connect the RecyclerView to the DB
public class ShoppingListViewHolder extends RecyclerView.ViewHolder {
    private final TextView itemTextView;
    public ImageView itemImageView;
    private ShoppingListViewModel shoppingListViewModel;
    private ShoppingListDao shoppingListDao;
    private static final String TAG = ShoppingListViewHolder.class.getSimpleName();

    private ShoppingListViewHolder(View itemView, ShoppingListViewModel shoppingListViewModel) {
        super(itemView);
        this.shoppingListViewModel = shoppingListViewModel;
        itemTextView = itemView.findViewById(R.id.shopping_list_name);
        itemImageView = itemView.findViewById(R.id.imageView);
    }

    private void passData(int id, View view) {
        Intent intent = new Intent(view.getContext(), ShoppingListActivity.class);
        intent.putExtra(ShoppingListActivity.EXTRA_ID, String.valueOf(id));
        Context context = view.getContext();

        //Clear the back stack so that when you go back to MainActivity, it doesn't go back to SecondActivity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.startActivity(intent);
        } else {
            view.getContext().startActivity(intent);
        }
    }

    public boolean onLongClick(View view, String name) {
        // Creates a dialog box when an item is long-clicked
        //Shows the dialog confirmation box
        AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
        builder.setTitle("Delete Shopping List confirmation");
        builder.setMessage("Are you sure you want to delete this shopping list?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //deletes the item from the database
                //uses a background thread
                Executors.newSingleThreadExecutor().execute(() -> {
                    shoppingListViewModel.deleteShoppingListWithName(name);
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do nothing
            }
        });
        builder.show();
        return true;
    }
    public void bind(String text) {
        itemTextView.setText(text); //Sets the recycler display to the actual item name

        // Set the long click listener for the text view
        itemTextView.setOnLongClickListener(view -> onLongClick(view, text));

        // Set the long click listener for the image view
        itemImageView.setOnLongClickListener(view -> onLongClick(view, text));

        itemTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                LiveData<ShoppingList> shoppingList=shoppingListViewModel.getShoppingListWithName(text);
                LifecycleOwner owner = (LifecycleOwner) view.getContext(); // get the lifecycle owner from the context
                shoppingList.observe(owner, new Observer<ShoppingList>() {
                    @Override
                    public void onChanged(ShoppingList shoppingList) {
                        passData(shoppingList.getId(), view);
                    }
                });
            }

        });

    }

    static ShoppingListViewHolder create(ViewGroup parent, ShoppingListViewModel shoppingListViewModel) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new ShoppingListViewHolder(view, shoppingListViewModel);
    }
}




