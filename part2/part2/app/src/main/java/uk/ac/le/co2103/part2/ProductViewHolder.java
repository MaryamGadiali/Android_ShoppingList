package uk.ac.le.co2103.part2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//To connect the RecyclerView to the DB
public class ProductViewHolder extends RecyclerView.ViewHolder {
    public static final int UPDATE_PRODUCT_ACTIVITY_REQUEST_CODE = 2;
    private final TextView product_name_text_view;
    private final TextView product_quantity_text_view;
    private final TextView product_unit_text_view;
    private ProductViewModel productViewModel;
    public static final String PRODUCT_NAME="";
    public static final String PRODUCT_QUANTITY="";
    public static final String PRODUCT_UNIT="";
    private static final String TAG = "ProductViewHolder";

    public ProductViewHolder(@NonNull View productView, ProductViewModel productViewModel) {
        super(productView);
        this.productViewModel = productViewModel;
        product_name_text_view=productView.findViewById(R.id.product_name);
        product_quantity_text_view=productView.findViewById(R.id.product_quantity);
        product_unit_text_view=productView.findViewById(R.id.product_unit);
    }

    public boolean onClick(View view, Product product){
        Toast.makeText(view.getContext(), "Name is " + product_name_text_view.getText() + ", Quantity is " + product_quantity_text_view.getText() + ", Unit is " + product_unit_text_view.getText(), Toast.LENGTH_LONG).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
        builder.setTitle("Edit or Delete Product");
        builder.setMessage("Choose to edit or delete the product");
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(itemView.getContext(), UpdateProductActivity.class);
                intent.putExtra(UpdateProductActivity.OLD_PRODUCT_NAME, product.getName());
                intent.putExtra(UpdateProductActivity.OLD_PRODUCT_QUANTITY, String.valueOf(product.getQuantity()));
                intent.putExtra(UpdateProductActivity.OLD_PRODUCT_UNIT, product.getUnit());
                intent.putExtra(UpdateProductActivity.OLD_PRODUCT_PARENT_SHOPPING_LIST_ID, String.valueOf(product.getParent_shopping_list_id()));
                ((Activity)itemView.getContext()).startActivityForResult(intent, 2);
            }
        });
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    productViewModel.deleteProductWithNameAndID(product.getName(),product.getParent_shopping_list_id() );
            }
        });
        builder.show();

        return true;
    }



    public void bind(Product product) {
        product_name_text_view.setText(product.getName());
        product_quantity_text_view.setText(String.valueOf(product.getQuantity()));
        product_unit_text_view.setText(product.getUnit());

        product_name_text_view.setOnClickListener(view -> onClick(view, product));
        product_quantity_text_view.setOnClickListener(view -> onClick(view,product ));
        product_unit_text_view.setOnClickListener(view -> onClick(view, product));
    }
    static ProductViewHolder create (ViewGroup parent, ProductViewModel productViewModel) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_product, parent, false);
        return new ProductViewHolder(view, productViewModel);
    }
}
