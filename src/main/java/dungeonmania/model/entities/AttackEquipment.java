package dungeonmania.model.entities;

import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.util.Position;

public abstract class AttackEquipment extends Equipment {
    private int attackDamage;
    private int hitRate;

    public AttackEquipment(String prefix, int attackDamage, int hitRate) {
        this(prefix, attackDamage, hitRate, null);
    }

    public AttackEquipment(String prefix, int attackDamage, int hitRate, Position position) {
        super(prefix, position);
        this.attackDamage = attackDamage;
        this.hitRate = hitRate;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public int getHitRate() {
        return hitRate;
    }

    @Override
    public double useEquipment(Player player) {
        super.useEquipment(player);
        return getAttackDamage();
    }
}
