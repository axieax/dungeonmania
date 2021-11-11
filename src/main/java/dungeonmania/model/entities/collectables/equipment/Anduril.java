package dungeonmania.model.entities.collectables.equipment;

import dungeonmania.model.entities.AttackEquipment;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.movings.Boss;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.util.Position;

public class Anduril extends AttackEquipment {

    private static final int ATTACK_DAMAGE = 50;
    private static final int HIT_RATE = 1;

    public Anduril(Position position) {
        super("anduril", ATTACK_DAMAGE, HIT_RATE, position);
    }

    @Override
    public double useEquipment(Player player, Entity enemy) {
        double attackAmount = super.useEquipment(player, enemy);
        
        // A very high damage sword which causes triple damage against bosses
        if (enemy instanceof Boss) {
            attackAmount = attackAmount * 3;
        }

        return attackAmount;
    }
}
