package dungeonmania.model.entities.buildables;

import dungeonmania.model.entities.movings.Inventory;

public class Shield extends BuildableEquipment {

    private static final int WOOD_NEEDED = 2;
    private static final int TREASURE_NEEDED = 1;
    private static final int KEY_NEEDED = 1;

    public Shield() {
        super(null);
    }

    public static boolean isBuildable(Inventory inventory) {
        return (
            inventory.hasItemQuantity("Wood", WOOD_NEEDED) &&
            (
                inventory.hasItemQuantity("Treasure", TREASURE_NEEDED) ||
                inventory.hasItemQuantity("Key", KEY_NEEDED)
            )
        );
    }

    public static void craft(Inventory inventory) {
        if (isBuildable(inventory)) {
            inventory.removeItemQuantity("Wood", WOOD_NEEDED);
            if (inventory.hasItemQuantity("Treasure", TREASURE_NEEDED)) {
                inventory.removeItemQuantity("Treasure", TREASURE_NEEDED);
            } else {
                inventory.removeItemQuantity("Key", KEY_NEEDED);
            }
            inventory.addItem(new Shield());
        }
    }
}
