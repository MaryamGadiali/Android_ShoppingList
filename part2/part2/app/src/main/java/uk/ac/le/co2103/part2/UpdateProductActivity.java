package uk.ac.le.co2103.part2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class UpdateProductActivity extends AppCompatActivity {
    public static final String OLD_PRODUCT_NAME = "product_name";
    public static final String OLD_PRODUCT_QUANTITY = "product_quantity";
    public static final String OLD_PRODUCT_UNIT = "product_unit";
    public static final String OLD_PRODUCT_PARENT_SHOPPING_LIST_ID="-1";
    public static final String NEW_PRODUCT_NAME = "product_name";
    public static final String NEW_PRODUCT_QUANTITY = "product_quantity" ;
    public static final String NEW_PRODUCT_UNIT = "product_unit";

    private static final String TAG = UpdateProductActivity.class.getSimpleName();
    private EditText edit_existing_product_name;
    private EditText edit_existing_product_quantity;
    private Spinner edit_existing_product_unit;
    private Button increment_button;
    private Button decrement_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProductViewModel productViewModel = new ProductViewModel(getApplication());
        setContentView(R.layout.activity_update_product);

        String old_productName = getIntent().getStringExtra(OLD_PRODUCT_NAME);
        String old_productQuantity = getIntent().getStringExtra(OLD_PRODUCT_QUANTITY);
        String old_productUnit = getIntent().getStringExtra(OLD_PRODUCT_UNIT);

        edit_existing_product_name = findViewById(R.id.edit_existing_product_name);
        edit_existing_product_quantity = findViewById(R.id.edit_existing_product_quantity);
        edit_existing_product_unit = findViewById(R.id.edit_existing_product_unit);
        increment_button=findViewById(R.id.incrementButton);
        decrement_button=findViewById(R.id.decrementButton);

        edit_existing_product_name.setText(old_productName);
        edit_existing_product_quantity.setText(old_productQuantity);

        String[] spinnerItems = {"Unit", "Kg", "Litre"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edit_existing_product_unit.setAdapter(adapter);
        adapter=(ArrayAdapter) edit_existing_product_unit.getAdapter();
        AtomicInteger position = new AtomicInteger(adapter.getPosition(old_productUnit));
        edit_existing_product_unit.setSelection(position.get());

        increment_button.setOnClickListener(view -> {
            float quantity = Float.parseFloat(edit_existing_product_quantity.getText().toString());
            quantity++;
            edit_existing_product_quantity.setText(String.valueOf(quantity));
        });

        decrement_button.setOnClickListener(view -> {
            float quantity = Float.parseFloat(edit_existing_product_quantity.getText().toString());
            if (quantity>0){ //quantity cannot be negative
                quantity--;
            }

            edit_existing_product_quantity.setText(String.valueOf(quantity));
        });

        final Button button = findViewById(R.id.button_saveProduct);
        ArrayAdapter<String> finalAdapter = adapter;
        button.setOnClickListener(view -> {
            Intent saveIntent = new Intent();

            if (TextUtils.isEmpty(edit_existing_product_name.getText()) && TextUtils.isEmpty(edit_existing_product_quantity.getText())) {
                saveIntent.putExtra("cancel_existing_reason", "The product name and quantity is empty");
                setResult(RESULT_CANCELED, saveIntent);
                finish();
            }
            else if (TextUtils.isEmpty(edit_existing_product_name.getText())){
                saveIntent.putExtra("cancel_existing_reason", "The product name is empty");
                setResult(RESULT_CANCELED, saveIntent);
                finish();
            } else if (TextUtils.isEmpty(edit_existing_product_quantity.getText())){
                saveIntent.putExtra("cancel_existing_reason", "The product quantity is empty");
                setResult(RESULT_CANCELED, saveIntent);
                finish();
            } else {
                String new_productName = edit_existing_product_name.getText().toString();
                String new_productQuantity = edit_existing_product_quantity.getText().toString();
                String new_productUnit = edit_existing_product_unit.getSelectedItem().toString();

                ProductDao productDao=ShoppingCartDB.getDatabase(getApplicationContext()).productDao();
                String shoppingListID_str=getIntent().getStringExtra(OLD_PRODUCT_PARENT_SHOPPING_LIST_ID);
                int parentShoppingListID=Integer.parseInt(shoppingListID_str);

                LiveData<Product> productLiveData=productDao.getProductWithNameAndID(new_productName, parentShoppingListID);
                productLiveData.observe(this, product -> {
                    if (product!=null && !Objects.equals(product.getName(), old_productName)){
                        saveIntent.putExtra("cancel_existing_reason", "The product exists");
                        setResult(RESULT_CANCELED, saveIntent);
                        finish();
                    } else {
                        saveIntent.putExtra(OLD_PRODUCT_NAME, old_productName);
                        saveIntent.putExtra(OLD_PRODUCT_QUANTITY, old_productQuantity);
                        saveIntent.putExtra(OLD_PRODUCT_UNIT, old_productUnit);
                        saveIntent.putExtra(OLD_PRODUCT_PARENT_SHOPPING_LIST_ID, String.valueOf(parentShoppingListID));
                        saveIntent.putExtra(NEW_PRODUCT_NAME, new_productName);
                        saveIntent.putExtra(NEW_PRODUCT_QUANTITY, new_productQuantity);
                        saveIntent.putExtra(NEW_PRODUCT_UNIT, new_productUnit);

                        float new_productQuantity_float=Float.parseFloat(new_productQuantity);
                        setResult(RESULT_OK, saveIntent);
                        productViewModel.updateProductWithNameAndID(new_productName, new_productQuantity_float, new_productUnit, old_productName, parentShoppingListID);
                        productLiveData.removeObservers(this);
                        finish();

                    }

                });
            }
        });
    }
}