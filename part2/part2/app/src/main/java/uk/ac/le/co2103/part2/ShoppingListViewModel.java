package uk.ac.le.co2103.part2;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

//View model acts as a bridge between the UI and the repository
public class ShoppingListViewModel extends AndroidViewModel {
    private ShoppingListRepository repo;
    private final LiveData<List<ShoppingList>> allShoppingLists;



    public ShoppingListViewModel(Application application) {
        super(application);
        repo = new ShoppingListRepository(application); //creates a new instance of repo
        allShoppingLists = repo.getAllShoppingLists();
    }

    LiveData<List<ShoppingList>> getAllShoppingLists() {
        return allShoppingLists;
    }

    public int getShoppingListIDWithName(String shoppingListName) {
        return repo.getShoppingListIDWithName(shoppingListName);
    }

    public void insert(ShoppingList shoppingList) {
        repo.insert(shoppingList);
    }

    public void deleteShoppingListWithName(String shoppingListName) {
        repo.deleteShoppingListWithName(shoppingListName);
    }

    public LiveData<ShoppingList> getShoppingListWithName(String shoppingListName) {
        return repo.getShoppingListWithName(shoppingListName);
    }
}
