package dungeonmania.model.entities.buildables;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.Inventory;
import dungeonmania.util.Position;

public class Shield extends BuildableEquipment {

    private static final int WOOD_NEEDED = 1;
    private static final int TREASURE_NEEDED = 1;
    private static final int KEY_NEEDED = 1;

    public Shield(String entityId, Position position) {
        super(entityId, position);
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

    public static void craft(Game game, Inventory inventory) {
        if (Bow.isBuildable(inventory)) {
            inventory.removeItemQuantity("Wood", WOOD_NEEDED);
            if (inventory.hasItemQuantity("Treasure", TREASURE_NEEDED)) {
                inventory.removeItemQuantity("Treasure", TREASURE_NEEDED);
            } else {
                inventory.removeItemQuantity("Key", KEY_NEEDED);
            }
            inventory.addItem(new Shield(game.generateEntityId(), null));
        }
    }
}
