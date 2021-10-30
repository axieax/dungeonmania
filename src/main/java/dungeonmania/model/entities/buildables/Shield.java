package dungeonmania.model.entities.buildables;

import dungeonmania.model.Game;
import dungeonmania.model.entities.DefenceEquipment;

public class Shield extends BuildableEquipment implements DefenceEquipment {

    private static final int WOOD_NEEDED = 2;
    private static final int TREASURE_NEEDED = 1;
    private static final int KEY_NEEDED = 1;

    public Shield() {
        super("shield", null);
    }

    public static boolean isBuildable(Inventory inventory) {
        return (
            inventory.hasItemQuantity("wood", WOOD_NEEDED) &&
            (
                inventory.hasItemQuantity("treasure", TREASURE_NEEDED) ||
                inventory.hasItemQuantity("key", KEY_NEEDED)
            )
        );
    }

    public static void craft(Inventory inventory) {
        if (isBuildable(inventory)) {
            inventory.removeItemQuantity("Wood", WOOD_NEEDED);
            if (inventory.hasItemQuantity("Treasure", TREASURE_NEEDED)) {
                inventory.removeItemQuantity("Treasure", TREASURE_NEEDED);
            } else {
                inventory.removeItemQuantity("key", KEY_NEEDED);
            }
            inventory.addItem(new Shield());
        }
    }
}
