package dungeonmania.model.entities.movings.player;

import dungeonmania.model.entities.AttackEquipment;
import dungeonmania.model.entities.Equipment;
import dungeonmania.model.entities.Item;
import dungeonmania.response.models.ItemResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.json.JSONArray;

public class Inventory {

    List<Item> items = new ArrayList<>();

    /**
     * Get the item with itemId from the inventory.
     *
     * @param itemId
     * @return
     */
    public Item getItem(String itemId) {
        return items.stream().filter(i -> i.getId().equals(itemId)).findFirst().orElse(null);
    }

    /**
     * Add the item to the inventory.
     * @param item
     */
    public void addItem(Item item) {
        item.setPosition(null);
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
        return items.stream().filter(i -> i.getType().startsWith(prefix)).findFirst().orElse(null);
    }

    /**
     * @return the first instance of any AttackEquipment, otherwise null
     */
    public Item findWeapon() {
        return items.stream().filter(i -> i instanceof AttackEquipment).findFirst().orElse(null);
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
            if (item.getType().startsWith(prefix)) itemQuantity++;
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
            List<Item> toRemove = new ArrayList<>();
            items
                .stream()
                .filter(item -> item.getType().startsWith(prefix))
                .limit(quantity)
                .forEach(item -> toRemove.add(item));
            toRemove.stream().forEach(i -> items.remove(i));
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

        for (Item i : items) {
            ItemResponse itemResponse = new ItemResponse(i.getId(), i.getType());
            response.add(itemResponse);
        }

        return response;
    }

    public JSONArray toJSON() {
        JSONArray itemsJSON = new JSONArray();
        for (Item item : items) {
            itemsJSON.put(item.toJSON());
        }
        return itemsJSON;
    }
}
