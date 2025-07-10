package uk.ac.le.co2103.part2;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

    @Database(entities = {ShoppingList.class, Product.class}, version = 1, exportSchema = false)
    public abstract class ShoppingCartDB extends RoomDatabase {

        public abstract ShoppingListDao shoppingListDao();
        public abstract ProductDao productDao();

        private static volatile ShoppingCartDB INSTANCE;
        private static final int NUMBER_OF_THREADS = 4;
        static final ExecutorService databaseWriteExecutor =
                Executors.newFixedThreadPool(NUMBER_OF_THREADS);

        static ShoppingCartDB getDatabase(final Context context) {
            if (INSTANCE == null) {
                synchronized (ShoppingCartDB.class) {
                    if (INSTANCE == null) { //I think this is for the very first access
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                        ShoppingCartDB.class, "shoppingcart_db")
                                .addCallback(sRoomDatabaseCallback)
                                .build();
                    }
                }
            }
            return INSTANCE;
        }

        private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                databaseWriteExecutor.execute(() -> {
                    ShoppingListDao shoppingListDao = INSTANCE.shoppingListDao();
                    ProductDao productDao= INSTANCE.productDao();
                    shoppingListDao.deleteAll();
                    productDao.deleteAll();

                });
            }
        };
    }

