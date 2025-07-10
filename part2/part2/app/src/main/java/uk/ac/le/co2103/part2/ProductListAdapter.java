package uk.ac.le.co2103.part2;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public class ProductListAdapter extends ListAdapter<Product, ProductViewHolder> {
    private ProductViewModel productViewModel;

    protected ProductListAdapter(@NonNull DiffUtil.ItemCallback<Product> diffCallback, ProductViewModel productViewModel) {
        super(diffCallback);
        this.productViewModel=productViewModel;
    }

    @Override
    public ProductViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        return ProductViewHolder.create(parent, productViewModel);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product currentProduct = getItem(position);
        holder.bind(currentProduct);

    }

    static class ProductDiff extends DiffUtil.ItemCallback<Product> {

        @Override
        public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    }



}
