package uk.ac.le.co2103.part2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

public class AddProductActivity extends AppCompatActivity {
    private static final String EXTRA_ID = "";
    private String selectedUnit;
    private EditText edit_product_name;
    public static final String PRODUCT_NAME = "";
    private EditText edit_product_quantity;
    public static final String PRODUCT_QUANTITY = "";
    private Spinner edit_product_unit;
    private static final String TAG = uk.ac.le.co2103.part2.CreateListActivity.class.getSimpleName();
    public static final String EXTRA_TEXT = "uk.ac.le.co2103.part2.TEXT";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Handle back button press
                finish();
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        edit_product_name = findViewById(R.id.editTextName);
        edit_product_quantity= findViewById(R.id.editTextQuantity);
        edit_product_unit=findViewById(R.id.spinner);
        String[] spinnerItems = {"Unit", "Kg", "Litre"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edit_product_unit.setAdapter(adapter);

        edit_product_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedUnit = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedUnit = "";
            }
        });

        final Button button = findViewById(R.id.button_createProduct);

        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(edit_product_name.getText()) && TextUtils.isEmpty(edit_product_quantity.getText())) {
                replyIntent.putExtra("cancel_reason", "The product name and quantity is empty");
                setResult(RESULT_CANCELED, replyIntent);
                finish();
            }
            else if (TextUtils.isEmpty(edit_product_name.getText())) {
                replyIntent.putExtra("cancel_reason", "The product name is empty");
                setResult(RESULT_CANCELED, replyIntent);
                finish();}

            else if(TextUtils.isEmpty(edit_product_quantity.getText())){
                replyIntent.putExtra("cancel_reason", "The product quantity is empty");
                setResult(RESULT_CANCELED, replyIntent);
                finish();
            }
            else{
                String edit_product_name_string =edit_product_name.getText().toString();
                String edit_product_quantity_var=edit_product_quantity.getText().toString();
                String edit_product_unit_var=selectedUnit;
                ProductDao productDao = ShoppingCartDB.getDatabase(getApplicationContext()).productDao();

                String parent_shopping_list_id_str = getIntent().getStringExtra(ShoppingListActivity.EXTRA_ID);
                int parent_shopping_list_id = Integer.parseInt(parent_shopping_list_id_str);
                    LiveData<Product> productLiveData = productDao.getProductWithNameAndID(edit_product_name_string, parent_shopping_list_id);
                productLiveData.observe(this, product -> {
                    if(product != null) {
                        replyIntent.putExtra("cancel_reason", "The product exists");
                        setResult(RESULT_CANCELED, replyIntent);
                    } else {
                        replyIntent.putExtra(ShoppingListActivity.PRODUCT_NAME, edit_product_name_string);
                        replyIntent.putExtra(AddProductActivity.PRODUCT_QUANTITY, edit_product_quantity_var);
                        replyIntent.putExtra(ShoppingListActivity.PRODUCT_UNIT, selectedUnit.toString());

                        setResult(RESULT_OK, replyIntent);
                    }
                    finish();
                });

            }
    });
}}