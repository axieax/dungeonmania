package dungeonmania.model.entities.movings;

import dungeonmania.model.entities.Equipment;
import dungeonmania.model.entities.Item;
import dungeonmania.response.models.ItemResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Inventory {

    List<Item> items = new ArrayList<>();

    /**
     * Get the item with itemId from the inventory.
     * 
     * @param itemId
     * @return
     */
    public Item getItem(String itemId) {
        return items.stream().filter(i -> i.getId() == itemId).findFirst().orElse(null);
    }

    /**
     * Add the item to the inventory.
     * @param item
     */
    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * Remove item with itemId from the inventory.
     * @param itemId
     */
    public void removeItem(String itemId) {
        items.remove(getItem(itemId));
    }

    /**
     * Find the first instance of the item that has the specified prefix in the 
     * inventory.
     * 
     * @param prefix
     * @return
     */
    public Item findItem(String prefix) {
        return items
            .stream()
            .filter(i -> i.getPrefix().equals(prefix))
            .findFirst()
            .orElse(null);
    }

    /**
     * Checks if the inventory has the specified quantity of the item.
     *
     * @param prefix
     * @param quantity
     * @return
     */
    public boolean hasItemQuantity(String prefix, int quantity) {
        int itemQuantity = 0;
        for (Item item : items) {
            if (item.getPrefix().equals(prefix)) itemQuantity++;
        }
        return itemQuantity >= quantity;
    }

    /**
     * Remove the specified item type from the inventory upto the quantity amount.
     *
     * @param prefix
     * @param quantity
     */
    public void removeItemQuantity(String prefix, int quantity) {
        if (this.hasItemQuantity(prefix, quantity)) {
            items
                .stream()
                .filter(item -> item.getPrefix().equals(prefix))
                .limit(quantity)
                .forEach(item -> items.remove(item));
        }
    }

    /**
     * Returns a list of equipment (attack and defence items) from the inventory.
     * @return
     */
    public List<Equipment> getEquipmentList() {
        return items
            .stream()
            .filter(equipment -> equipment instanceof Equipment)
            .map(equipment -> (Equipment) equipment)
            .collect(Collectors.toList());
    }

    /**
     * Returns all items from the inventory in the ItemResponse format
     * @return
     */
    public List<ItemResponse> getInventoryResponses() {
        List<ItemResponse> response = new ArrayList<>();

        for(Item i: items) {
            ItemResponse itemResponse = new ItemResponse(i.getId(), i.getPrefix());
            response.add(itemResponse);
        }

        return response;
    }
}
