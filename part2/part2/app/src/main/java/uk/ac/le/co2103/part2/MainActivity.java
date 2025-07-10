package uk.ac.le.co2103.part2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ShoppingListViewModel shoppingListViewItem;
    public static final int CREATE_SHOPPING_LIST_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ShoppingListAdapter adapter = new ShoppingListAdapter(new ShoppingListAdapter.ShoppingListDiff(), new ViewModelProvider(this).get(ShoppingListViewModel.class));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        shoppingListViewItem = new ViewModelProvider(this).get(ShoppingListViewModel.class);
        shoppingListViewItem.getAllShoppingLists().observe(this, items -> {
            adapter.submitList(items);
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener( view -> {
            Intent intent = new Intent(MainActivity.this, CreateListActivity.class);
            startActivityForResult(intent, CREATE_SHOPPING_LIST_ACTIVITY_REQUEST_CODE);

        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_SHOPPING_LIST_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                ShoppingList shoppingList = new ShoppingList(data.getStringExtra(CreateListActivity.EXTRA_TEXT));
                shoppingList.setImage(data.getStringExtra(CreateListActivity.EXTRA_IMAGE));
                shoppingListViewItem.insert(shoppingList);
            }
        }
        else if (resultCode==RESULT_CANCELED & data!=null){
            String cancel_reason=data.getStringExtra("cancel_reason");
            if (cancel_reason.equals("The item exists")){
                Toast.makeText(
                        getApplicationContext(),
                        R.string.repeated_name,
                        Toast.LENGTH_LONG).show();

            }
            else if (cancel_reason.equals("The item name is empty")){
                Toast.makeText(
                        getApplicationContext(),
                        R.string.name_empty_not_saved,
                        Toast.LENGTH_LONG).show();

            }

        }

    }
}