package uk.ac.le.co2103.part2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ShoppingListActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String PRODUCT_UNIT = "kg";
    private static final String TAG = ShoppingListActivity.class.getSimpleName();
    public static final String PRODUCT_NAME = "PRODUCT_NAME" ;
    private ProductViewModel productViewModel;
    public static final int ADD_PRODUCT_ACTIVITY_REQUEST_CODE = 1;
    private int parent_shopping_list_id = -1;
    String parent_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        RecyclerView recyclerView=findViewById(R.id.recyclerview);
        final ProductListAdapter adapter=new ProductListAdapter(new ProductListAdapter.ProductDiff(), new ViewModelProvider(this).get(ProductViewModel.class));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent=getIntent();
        parent_id=intent.getStringExtra(ShoppingListActivity.EXTRA_ID);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getAllProductsFromShoppingList(Integer.parseInt(parent_id)).observe(this, products->{
        adapter.submitList(products);
        });

        FloatingActionButton fab = findViewById(R.id.fabAddProduct);
        fab.setOnClickListener( view -> {
            Intent intent_addProduct = new Intent(ShoppingListActivity.this, AddProductActivity.class);
            //pass the parent shopping list id to the intent
            intent_addProduct.putExtra(EXTRA_ID, parent_id);
            startActivityForResult(intent_addProduct, 1);
        });
    };

    //add a function for clicking the back button or return button
    @Override
public void onBackPressed() {
        Intent intent = new Intent(ShoppingListActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        super.onBackPressed();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2 && resultCode==RESULT_OK){
            if (data!=null) {
                String existing_old_product_name = data.getStringExtra(UpdateProductActivity.OLD_PRODUCT_NAME);
                String existing_old_product_quantity_string = data.getStringExtra(UpdateProductActivity.OLD_PRODUCT_QUANTITY);
                float existing_old_product_quantity = Float.parseFloat(existing_old_product_quantity_string);
                String existing_old_product_unit = data.getStringExtra(UpdateProductActivity.OLD_PRODUCT_UNIT);
                String existing_product_id = data.getStringExtra(UpdateProductActivity.OLD_PRODUCT_PARENT_SHOPPING_LIST_ID);
                int existing_product_id_int = Integer.parseInt(existing_product_id);

                String new_product_name = data.getStringExtra(UpdateProductActivity.NEW_PRODUCT_NAME);
                String new_product_quantity_string = data.getStringExtra(UpdateProductActivity.NEW_PRODUCT_QUANTITY);
                float new_product_quantity = Float.parseFloat(new_product_quantity_string);
                String new_product_unit = data.getStringExtra(UpdateProductActivity.NEW_PRODUCT_UNIT);

            }
            }
        else if (requestCode==2 & resultCode==RESULT_CANCELED & data!=null){
            String cancel_reason=data.getStringExtra("cancel_existing_reason");
            if (cancel_reason.equals("The product exists")){
                Toast.makeText(
                        getApplicationContext(),
                        R.string.repeated_name_product,
                        Toast.LENGTH_LONG).show();
            }
            else if(cancel_reason.equals("The product name and quantity is empty")){
                Toast.makeText(
                        getApplicationContext(),
                        R.string.name_quantity_empty_not_saved,
                        Toast.LENGTH_LONG).show();
            }
            else if(cancel_reason.equals("The product name is empty")){
                Toast.makeText(
                        getApplicationContext(),
                        R.string.name_empty_not_saved,
                        Toast.LENGTH_LONG).show();
            }
            else if(cancel_reason.equals("The product quantity is empty")){
                Toast.makeText(
                        getApplicationContext(),
                        R.string.quantity_empty_not_saved,
                        Toast.LENGTH_LONG).show();
            }

        }

        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                String product_quantity_string = data.getStringExtra(AddProductActivity.PRODUCT_QUANTITY);
                float product_quantity = Float.parseFloat(product_quantity_string);
                String product_unit=data.getStringExtra(PRODUCT_UNIT);
                Product product = new Product(data.getStringExtra(PRODUCT_NAME), product_quantity, product_unit, Integer.parseInt(parent_id) );
                productViewModel.insert(product);
            }
        }
        else if (requestCode==1 & resultCode==RESULT_CANCELED & data!=null){
            String cancel_reason=data.getStringExtra("cancel_reason");
            if (cancel_reason.equals("The product exists")){
                Toast.makeText(
                        getApplicationContext(),
                        R.string.repeated_name_product,
                        Toast.LENGTH_LONG).show();
            }
            else if(cancel_reason.equals("The product name and quantity is empty")){
                Toast.makeText(
                        getApplicationContext(),
                        R.string.name_quantity_empty_not_saved,
                        Toast.LENGTH_LONG).show();
            }
            else if (cancel_reason.equals("The product name is empty")){
                Toast.makeText(
                        getApplicationContext(),
                        R.string.name_empty_not_saved,
                        Toast.LENGTH_LONG).show();
            }
            else if(cancel_reason.equals("The product quantity is empty")) {
                Toast.makeText(
                        getApplicationContext(),
                        R.string.quantity_empty_not_saved,
                        Toast.LENGTH_LONG).show();
            }
            else{
                Log.d(TAG, "Something went wrong");
            }
        }
    }
    }

