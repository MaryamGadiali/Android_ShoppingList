package uk.ac.le.co2103.part2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

public class CreateListActivity extends AppCompatActivity {

    private static final String TAG = CreateListActivity.class.getSimpleName();
    public static final String EXTRA_TEXT = "uk.ac.le.co2103.part2.TEXT";
    public static final String EXTRA_IMAGE= "";
    private static final int ADD_PRODUCT_ACTIVITY_REQUEST_CODE = -1;
    private EditText editTextItem;
    private EditText editImageItem;
    private static final int REQUEST_SELECT_IMAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shopping_list);

        editTextItem = findViewById(R.id.edit_new_shopping_list);
        editImageItem=findViewById(R.id.edit_image_item);

        final Button button = findViewById(R.id.button_createProduct);

        final Button selectImageButton=findViewById(R.id.upload_image);
        selectImageButton.setOnClickListener(view->{
            Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*"); //Any image type
            startActivityForResult(intent, REQUEST_SELECT_IMAGE);
        });

        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(editTextItem.getText())) {
                replyIntent.putExtra("cancel_reason", "The item name is empty");
                setResult(RESULT_CANCELED, replyIntent);
                finish();
            }
            else{
                String textItem = editTextItem.getText().toString();
                String imageItem=editImageItem.getText().toString();
                ShoppingListDao shoppingListDao = ShoppingCartDB.getDatabase(getApplicationContext()).shoppingListDao();

                LiveData<ShoppingList> itemLiveData = shoppingListDao.getShoppingListWithName(textItem);
                itemLiveData.observe(this, item -> {
                    if(item != null) {
                        replyIntent.putExtra("cancel_reason", "The item exists");
                        setResult(RESULT_CANCELED, replyIntent);
                    } else {
                        replyIntent.putExtra(EXTRA_TEXT, textItem);
                        replyIntent.putExtra(EXTRA_IMAGE, imageItem );
                        setResult(RESULT_OK, replyIntent);
                    }
                    finish();
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_IMAGE && resultCode == RESULT_OK) {
            // Handle selected image here
            Uri selectedImageUri = data.getData();
            editImageItem=findViewById(R.id.edit_image_item);
            editImageItem.setText(selectedImageUri.toString());
    }
}


}