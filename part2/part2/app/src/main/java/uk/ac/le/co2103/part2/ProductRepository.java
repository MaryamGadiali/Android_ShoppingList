package uk.ac.le.co2103.part2;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ProductRepository {
    private uk.ac.le.co2103.part2.ProductDao productDao;

    ProductRepository(Application application){
        ShoppingCartDB db=ShoppingCartDB.getDatabase(application);
        productDao=db.productDao();
    }

    LiveData<List<Product>> getAllProductsFromShoppingList(int id){
        return productDao.getAllProductsFromShoppingList(id);
    }

    LiveData<Product> getProductWithNameAndID(String productName, int shopping_list_id){
        return productDao.getProductWithNameAndID(productName, shopping_list_id);
    }
    LiveData<Boolean> hasProductWithName(String productName, int shopping_list_id){
        return productDao.hasProductWithName(productName, shopping_list_id);
    }

    public void deleteProductWithNameAndID(String productName, int shopping_list_id){
        ShoppingCartDB.databaseWriteExecutor.execute(()->{
            productDao.deleteProductWithNameAndID(productName, shopping_list_id);
        });
    }

    void insert(Product product){
        ShoppingCartDB.databaseWriteExecutor.execute(()->{
            productDao.insert(product.getName(), product.getQuantity(), product.getUnit(), product.getParent_shopping_list_id());
        });
    }

    void updateProductWithNameAndID(String newProductName, float newProductQuantity, String newProductUnit, String oldProductName, int shopping_list_id){
        ShoppingCartDB.databaseWriteExecutor.execute(()->{
            productDao.updateProductWithNameAndID(newProductName, newProductQuantity, newProductUnit, oldProductName, shopping_list_id);
        });
    }




}
