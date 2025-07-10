package uk.ac.le.co2103.part2;

import com.bumptech.glide.Glide;
import android.net.Uri;
import android.text.TextUtils;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public class ShoppingListAdapter extends ListAdapter<ShoppingList, ShoppingListViewHolder> {

    private ShoppingListViewModel shoppingListViewModel;
    public ShoppingListAdapter(@NonNull DiffUtil.ItemCallback<ShoppingList> diffCallback, ShoppingListViewModel shoppingListViewModel) {
        super(diffCallback);
        this.shoppingListViewModel=shoppingListViewModel;
    }

   @Override
    public ShoppingListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ShoppingListViewHolder.create(parent, shoppingListViewModel);
    }

    @Override
    public void onBindViewHolder(ShoppingListViewHolder holder, int position) {
        ShoppingList current = getItem(position);
        holder.bind(current.getName());

        if (!TextUtils.isEmpty(current.getImage())) {
            Glide.with(holder.itemView.getContext())
                    .load(Uri.parse(current.getImage()))
                    .into(holder.itemImageView);
        } else {
            // If there is no image, display the default image
            holder.itemImageView.setImageResource(R.drawable.ic_shopping_cart);
        }
    }
        static class ShoppingListDiff extends DiffUtil.ItemCallback<ShoppingList> {

            @Override
            public boolean areItemsTheSame(@NonNull ShoppingList oldItem, @NonNull ShoppingList newItem) {
                return oldItem == newItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull ShoppingList oldItem, @NonNull ShoppingList newItem) {
                return oldItem.getName().equals(newItem.getName());
            }
    }
}
