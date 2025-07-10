package uk.ac.le.co2103.part2;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {
    private ProductRepository productRepository;


    public ProductViewModel(@NonNull Application application) {
        super(application);
        productRepository=new ProductRepository(application);
    }

    LiveData<List<Product>> getAllProductsFromShoppingList(int id){
        return productRepository.getAllProductsFromShoppingList(id);
    }
    LiveData<Product> getProductWithNameAndID(String productName, int shopping_list_id){
        return productRepository.getProductWithNameAndID(productName, shopping_list_id);
    }

    LiveData<Boolean> hasProductWithName(String productName, int shopping_list_id){
        return productRepository.hasProductWithName(productName, shopping_list_id);
    }

    public void deleteProductWithNameAndID(String productName, int shopping_list_id){
        productRepository.deleteProductWithNameAndID(productName, shopping_list_id);
    }
    public void insert (Product product){
        productRepository.insert(product);
    }

    public void updateProductWithNameAndID(String newProductName, float newProductQuantity, String newProductUnit, String oldProductName, int shopping_list_id){
        productRepository.updateProductWithNameAndID(newProductName, newProductQuantity, newProductUnit, oldProductName, shopping_list_id);
    }
}
