package uk.ac.le.co2103.part2;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ShoppingListRepository {
    private ShoppingListDao shoppingListDao;
    private LiveData<List<ShoppingList>> allShoppingLists;

    ShoppingListRepository(Application application) {
        ShoppingCartDB db = ShoppingCartDB.getDatabase(application);
        shoppingListDao = db.shoppingListDao();
        allShoppingLists = shoppingListDao.getShoppingLists();
    }

    LiveData<List<ShoppingList>> getAllShoppingLists() {
        return allShoppingLists;
    }

    LiveData<ShoppingList> getShoppingListWithName(String shoppingListName) {
        return shoppingListDao.getShoppingListWithName(shoppingListName);
    }

    LiveData<Boolean> hasShoppingListWithName(String shoppingListName) {
        return shoppingListDao.hasShoppingListWithName(shoppingListName);
    }

    int getShoppingListIDWithName(String shoppingListName) {
        return shoppingListDao.getShoppingListIDWithName(shoppingListName);
    }

    void deleteShoppingListWithName(String shoppingListName) {
        shoppingListDao.deleteShoppingListWithName(shoppingListName);
    }

    void insert(ShoppingList shoppingList) {
        //performs insertion on a background thread, not a ui thread
        ShoppingCartDB.databaseWriteExecutor.execute(() -> {
            shoppingListDao.insert(shoppingList);
        });
    }
}
