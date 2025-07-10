package uk.ac.le.co2103.part2;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ShoppingListDao {

    @Insert
    void insert(ShoppingList shoppingList);

    @Query("DELETE FROM shopping_lists")
    void deleteAll();

    @Query("SELECT * FROM shopping_lists ORDER BY shopping_list_name ASC")
    LiveData<List<ShoppingList>> getShoppingLists();

    @Query("SELECT * FROM shopping_lists where shopping_list_name=:shoppingListName")
    LiveData<ShoppingList> getShoppingListWithName(String shoppingListName);

    @Query("SELECT listId FROM shopping_lists where shopping_list_name=:shoppingListName")
    int getShoppingListIDWithName(String shoppingListName);

    @Query("SELECT EXISTS(SELECT 1 FROM shopping_lists WHERE shopping_list_name =:shoppingListName)")
    LiveData<Boolean> hasShoppingListWithName(String shoppingListName);

    @Query("DELETE FROM shopping_lists WHERE shopping_list_name =:shoppingListName")
    void deleteShoppingListWithName(String shoppingListName);

    @Query("SELECT * FROM shopping_lists WHERE listId = :id")
    LiveData<ShoppingList> getShoppingListById(int id);

    @Query("SELECT listId FROM shopping_lists WHERE listId = :id")
    int getShoppingListId(int id);


}
