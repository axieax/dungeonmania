package dungeonmania.model.entities.buildables;

import dungeonmania.model.Game;
import dungeonmania.model.entities.AttackEquipment;
import dungeonmania.model.entities.movings.player.Inventory;

public class Sceptre extends AttackEquipment implements Buildable {

    private static final int WOOD_NEEDED = 1;
    private static final int ARROW_NEEDED = 2;
    private static final int KEY_NEEDED = 1;
    private static final int TREASURE_NEEDED = 1;
    private static final int SUNSTONE_NEEDED = 1;

    private static final int ATTACK_DAMAGE = 60;
    private static final int HIT_RATE = 1;

    public Sceptre() {
        super("sceptre", ATTACK_DAMAGE, HIT_RATE);
    }

    @Override
    public boolean isBuildable(Game game, Inventory inventory) {
        boolean useSunStone = inventory.hasItemQuantity(
            "sun_stone",
            TREASURE_NEEDED + SUNSTONE_NEEDED
        );
        return (
            (
                inventory.hasItemQuantity("wood", WOOD_NEEDED) ||
                inventory.hasItemQuantity("arrow", ARROW_NEEDED)
            ) &&
            (
                inventory.hasItemQuantity("key", KEY_NEEDED) ||
                useSunStone ||
                inventory.hasItemQuantity("treasure", TREASURE_NEEDED)
            ) &&
            inventory.hasItemQuantity(
                "sun_stone",
                SUNSTONE_NEEDED + (useSunStone ? TREASURE_NEEDED : 0)
            )
        );
    }

    @Override
    public void craft(Inventory inventory) {
        boolean useSunStone = inventory.hasItemQuantity(
            "sun_stone",
            TREASURE_NEEDED + SUNSTONE_NEEDED
        );
        if (inventory.hasItemQuantity("wood", WOOD_NEEDED)) {
            inventory.removeItemQuantity("wood", WOOD_NEEDED);
        } else {
            inventory.removeItemQuantity("arrow", ARROW_NEEDED);
        }
        if (inventory.hasItemQuantity("key", KEY_NEEDED)) {
            inventory.removeItemQuantity("key", KEY_NEEDED);
        } else if (useSunStone) {
            inventory.removeItemQuantity("sun_stone", TREASURE_NEEDED);
        } else {
            inventory.removeItemQuantity("treasure", TREASURE_NEEDED);
        }
        inventory.removeItemQuantity("sun_stone", SUNSTONE_NEEDED);
        inventory.addItem(new Sceptre());
    }

    @Override
    public Buildable clone() {
        return new Sceptre();
    }
}
