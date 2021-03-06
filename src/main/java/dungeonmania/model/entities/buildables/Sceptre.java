package dungeonmania.model.entities.buildables;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.movings.player.Inventory;

public class Sceptre extends Item implements Buildable {

    private static final int WOOD_NEEDED = 1;
    private static final int ARROWS_NEEDED = 2;
    private static final int KEYS_NEEDED = 1;
    private static final int TREASURE_NEEDED = 1;
    private static final int SUNSTONE_NEEDED = 1;

    public Sceptre() {
        super("sceptre", null);
    }

    public boolean isBuildable(Game game, Inventory inventory) {
        boolean useSunStone = inventory.hasItemQuantity(
            "sun_stone",
            TREASURE_NEEDED + SUNSTONE_NEEDED
        );
        return (
            (
                inventory.hasItemQuantity("wood", WOOD_NEEDED) ||
                inventory.hasItemQuantity("arrow", ARROWS_NEEDED)
            ) &&
            (
                inventory.hasItemQuantity("key", KEYS_NEEDED) ||
                useSunStone ||
                inventory.hasItemQuantity("treasure", TREASURE_NEEDED)
            ) &&
            inventory.hasItemQuantity(
                "sun_stone",
                SUNSTONE_NEEDED + (useSunStone ? TREASURE_NEEDED : 0)
            )
        );
    }

    public void craft(Inventory inventory) {
        boolean useSunStone = inventory.hasItemQuantity(
            "sun_stone",
            TREASURE_NEEDED + SUNSTONE_NEEDED
        );

        if (inventory.hasItemQuantity("wood", WOOD_NEEDED)) {
            inventory.removeItemQuantity("wood", WOOD_NEEDED);
        } else {
            inventory.removeItemQuantity("arrow", ARROWS_NEEDED);
        }

        if (inventory.hasItemQuantity("key", KEYS_NEEDED)) {
            inventory.removeItemQuantity("key", KEYS_NEEDED);
        } else if (useSunStone) {
            inventory.removeItemQuantity("sun_stone", TREASURE_NEEDED);
        } else {
            inventory.removeItemQuantity("treasure", TREASURE_NEEDED);
        }
        inventory.removeItemQuantity("sun_stone", SUNSTONE_NEEDED);
        inventory.addItem(new Sceptre());
    }

    public Buildable clone() {
        return new Sceptre();
    }
}
