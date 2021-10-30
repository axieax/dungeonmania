package dungeonmania.model.entities.movings;

import dungeonmania.model.entities.Item;
import java.util.ArrayList;
import java.util.List;

public class Inventory {

    List<Item> items = new ArrayList<>();

    public Item getItem(String id) {
        return null;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * Checks if the inventory has the specified quantity of the item.
     * 
     * @param className
     * @param quantity
     * @return
     */
    public boolean hasItemQuantity(String className, int quantity) {
        int itemQuantity = 0;
        for (Item item : items) {
            if (item.getClass().getSimpleName().equals(className)) itemQuantity++;
        }
        return itemQuantity >= quantity;
    }

    /**
     * Remove the specified item type from the inventory upto the quantity amount.
     * 
     * @param className
     * @param quantity
     */
    public void removeItemQuantity(String className, int quantity) {
        if (this.hasItemQuantity(className, quantity)) {
            items
                .stream()
                .filter(item -> item.getClass().getSimpleName().equals(className))
                .limit(quantity)
                .forEach(item -> items.remove(item));
        }
    }
}
