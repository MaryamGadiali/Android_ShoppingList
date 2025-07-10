package uk.ac.le.co2103.part2;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

//Database name and primary and foreign keys are declared here
@Entity(tableName = "products",
        primaryKeys = {"product_name", "parent_shopping_list_id"},
        foreignKeys = @ForeignKey(
                entity = ShoppingList.class,
                parentColumns = "listId",
                childColumns = "parent_shopping_list_id",
                onDelete = ForeignKey.CASCADE), indices = {@Index("parent_shopping_list_id")})


public class Product {

    @NonNull
    @ColumnInfo(name = "product_name") //Unique check is done when the user tries to add a new product in the AddProductActivity form
    public String name;

    @ColumnInfo(name = "product_quantity") //float is of type number, chose as measurements such as kg or litres are used with decimal points in shops
    public float quantity;

    @ColumnInfo(name = "product_unit") //Spinner is implement with the AddProductActivity class
    public String unit;

    @NonNull
    @ColumnInfo(name = "parent_shopping_list_id")
    public int parent_shopping_list_id;

    public Product(String name, float quantity, String unit, int parent_shopping_list_id) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.parent_shopping_list_id = parent_shopping_list_id;
    }


    public int getParent_shopping_list_id() {
        return parent_shopping_list_id;
    }

    public void setParent_shopping_list_id(int parent_shopping_list_id) {
        this.parent_shopping_list_id = parent_shopping_list_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

}


