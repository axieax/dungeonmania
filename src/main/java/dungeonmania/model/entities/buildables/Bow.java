package dungeonmania.model.entities.buildables;

import dungeonmania.model.entities.AttackEquipment;
import dungeonmania.model.entities.movings.player.Inventory;

public class Bow extends AttackEquipment implements Buildable {

    private static final int WOOD_NEEDED = 1;
    private static final int ARROW_NEEDED = 3;

    private static final int ATTACK_DAMAGE = 25;
    private static final int HIT_RATE = 2;

    public Bow() {
        super("bow", ATTACK_DAMAGE, HIT_RATE);
    }

    @Override
    public boolean isBuildable(Inventory inventory) {
        return (
            inventory.hasItemQuantity("wood", WOOD_NEEDED) &&
            inventory.hasItemQuantity("arrow", ARROW_NEEDED)
        );
    }

    @Override
    public void craft(Inventory inventory) {
        inventory.removeItemQuantity("wood", WOOD_NEEDED);
        inventory.removeItemQuantity("arrow", ARROW_NEEDED);
        inventory.addItem(new Bow());
    }

    @Override
    public Buildable clone() {
        return new Bow();
    }
}
