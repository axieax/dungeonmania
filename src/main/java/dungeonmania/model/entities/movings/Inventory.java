package dungeonmania.model.entities.movings;

import dungeonmania.model.entities.Equipment;
import dungeonmania.model.entities.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Inventory {

    List<Item> items = new ArrayList<>();

    public Item getItem(String itemId) {
        return items.stream().filter(i -> i.getId() == itemId).findFirst().orElse(null);
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public Item findItem(String className) {
        return items
            .stream()
            .filter(i -> i.getClass().getSimpleName().equals(className))
            .findFirst()
            .orElse(null);
    }

    public void removeItem(String itemId) {
        items.remove(getItem(itemId));
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

    public List<Equipment> getEquipmentList() {
        return items
            .stream()
            .filter(equipment -> equipment instanceof Equipment)
            .map(equipment -> (Equipment) equipment)
            .collect(Collectors.toList());
    }
}
