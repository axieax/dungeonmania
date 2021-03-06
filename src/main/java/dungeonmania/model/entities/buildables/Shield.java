package dungeonmania.model.entities.buildables;

import dungeonmania.model.Game;
import dungeonmania.model.entities.DefenceEquipment;
import dungeonmania.model.entities.movings.player.Inventory;

public class Shield extends DefenceEquipment implements Buildable {

    private static final int WOOD_NEEDED = 2;
    private static final int TREASURE_NEEDED = 1;
    private static final int KEY_NEEDED = 1;

    private static final double DEFENCE_MULTIPLIER = 0.3;

    public Shield() {
        super("shield", DEFENCE_MULTIPLIER);
    }

    public boolean isBuildable(Game game, Inventory inventory) {
        return (
            inventory.hasItemQuantity("wood", WOOD_NEEDED) &&
            (
                inventory.hasItemQuantity("treasure", TREASURE_NEEDED) ||
                inventory.hasItemQuantity("sun_stone", TREASURE_NEEDED) ||
                inventory.hasItemQuantity("key", KEY_NEEDED)
            )
        );
    }

    public void craft(Inventory inventory) {
        inventory.removeItemQuantity("wood", WOOD_NEEDED);
        if (inventory.hasItemQuantity("sun_stone", TREASURE_NEEDED)) {
            inventory.removeItemQuantity("sun_stone", TREASURE_NEEDED);
        } else if (inventory.hasItemQuantity("treasure", TREASURE_NEEDED)) {
            inventory.removeItemQuantity("treasure", TREASURE_NEEDED);
        } else {
            inventory.removeItemQuantity("key", KEY_NEEDED);
        }
        inventory.addItem(new Shield());
    }

    public Buildable clone() {
        return new Shield();
    }
}
