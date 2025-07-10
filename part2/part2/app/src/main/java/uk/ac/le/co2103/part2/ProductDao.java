package uk.ac.le.co2103.part2;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProductDao {

    @Query("INSERT INTO products (product_name, product_quantity, product_unit, parent_shopping_list_id) VALUES (:productName, :productQuantity, :productUnit, :parentShoppingListID)")
    void insert(String productName, float productQuantity, String productUnit, int parentShoppingListID);


    @Query("DELETE FROM products")
    void deleteAll();

    @Query("SELECT * FROM products ORDER BY product_name ASC")
    LiveData<List<Product>> getAllProducts(); //probably useless


    @Query("SELECT * FROM products where parent_shopping_list_id=:shopping_list_id")
    LiveData<List<Product>> getAllProductsFromShoppingList(int shopping_list_id);

    @Query("SELECT EXISTS(SELECT 1 FROM products WHERE product_name =:productName and parent_shopping_list_id=:shopping_list_id)")
    LiveData<Boolean> hasProductWithName(String productName, int shopping_list_id);

    @Query("SELECT * FROM products where product_name= :productName and parent_shopping_list_id=:shopping_list_id")
    LiveData<Product> getProductWithNameAndID(String productName, int shopping_list_id);

    //delete product with name and id
    @Query("DELETE FROM products where product_name= :productName and parent_shopping_list_id=:shopping_list_id")
    void deleteProductWithNameAndID(String productName, int shopping_list_id);

    //update a product name, quantity, and unit,  with old name and id provided
    @Query("UPDATE products SET product_name=:newProductName, product_quantity=:newProductQuantity, product_unit=:newProductUnit WHERE product_name=:oldProductName and parent_shopping_list_id=:shopping_list_id")
    void updateProductWithNameAndID(String newProductName, float newProductQuantity, String newProductUnit, String oldProductName, int shopping_list_id);

}
