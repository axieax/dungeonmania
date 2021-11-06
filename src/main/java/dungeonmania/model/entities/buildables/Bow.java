package dungeonmania.model.entities.buildables;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.entities.AttackEquipment;
import dungeonmania.model.entities.Equipment;
import dungeonmania.model.entities.movings.Inventory;
import dungeonmania.model.entities.movings.MovingEntity;
public class Bow extends Equipment implements AttackEquipment, Buildable {

    private static final int WOOD_NEEDED = 1;
    private static final int ARROWS_NEEDED = 3;
    private final double MULTIPLIER = 2;
    public final int ATTACK_DAMAGE = 30;

    public Bow() {
        super("bow", null);
    }

    @Override
    public boolean isBuildable(Inventory inventory) {
        return (
            inventory.hasItemQuantity("wood", WOOD_NEEDED) &&
            inventory.hasItemQuantity("arrow", ARROWS_NEEDED)
        );
    }

    @Override
    public void craft(Inventory inventory) throws InvalidActionException {
        if (isBuildable(inventory)) {
            inventory.removeItemQuantity("wood", WOOD_NEEDED);
            inventory.removeItemQuantity("arrow", ARROWS_NEEDED);
            inventory.addItem(new Bow());
        } else {
            throw new InvalidActionException("You don't have enough resources to build a Bow.");
        }
    }

    @Override
    public int getAttackDamage(MovingEntity entity) {
        return this.ATTACK_DAMAGE;
    }

    @Override
    public Buildable clone() {
        return new Bow();
    }
}
