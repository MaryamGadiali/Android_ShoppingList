package uk.ac.le.co2103.part2;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="shopping_lists") //Indicates the class is an entity and represents a db table
public class ShoppingList {

    @PrimaryKey(autoGenerate = true)
    public int listId;

    @ColumnInfo(name="shopping_list_name") //The unique check happens when the attempt to add a new shopping list happens
    public String name;

    public String image; //This optional check happens also when the user adds a new shopping list

    public ShoppingList(String name) {
        this.name = name;
        this.listId=listId;
    }


    public int getId() {
        return listId;
    }

    public void setId(int id) {
        this.listId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ShoppingList{" +
                "id=" + listId +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}


