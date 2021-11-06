package dungeonmania.model.entities.buildables;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.entities.DefenceEquipment;
import dungeonmania.model.entities.Equipment;
import dungeonmania.model.entities.movings.player.Inventory;

public class Shield extends Equipment implements DefenceEquipment, Buildable {

    private static final int WOOD_NEEDED = 2;
    private static final int TREASURE_NEEDED = 1;
    private static final int KEY_NEEDED = 1;

    public final double MULTIPLIER = 0.25;

    public Shield() {
        super("shield", null);
    }

    @Override
    public boolean isBuildable(Inventory inventory) {
        return (
            inventory.hasItemQuantity("wood", WOOD_NEEDED) &&
            (
                inventory.hasItemQuantity("treasure", TREASURE_NEEDED) ||
                inventory.hasItemQuantity("key", KEY_NEEDED)
            )
        );
    }

    @Override
    public void craft(Inventory inventory) throws InvalidActionException {
        if (isBuildable(inventory)) {
            inventory.removeItemQuantity("wood", WOOD_NEEDED);
            if (inventory.hasItemQuantity("treasure", TREASURE_NEEDED)) {
                inventory.removeItemQuantity("treasure", TREASURE_NEEDED);
            } else {
                inventory.removeItemQuantity("key", KEY_NEEDED);
            }
            inventory.addItem(new Shield());
        } else {
            throw new InvalidActionException("You don't have enough resources to build a Shield.");
        }
    }

    @Override
    public Buildable clone() {
        return new Shield();
    }
}
