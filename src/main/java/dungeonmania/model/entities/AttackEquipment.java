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

    /**
     * Get attack damage of the AttackEquipment
     *
     * @return attack damage
     */
    public int getAttackDamage() {
        return attackDamage;
    }

    /**
     * Get hit rate of the AttackEquipment
     *
     * @return attack damage
     */
    public int getHitRate() {
        return hitRate;
    }

    @Override
    public double useEquipment(Player player, Entity enemy) {
        super.useEquipment(player, enemy);
        return attackDamage;
    }
}
