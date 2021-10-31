package dungeonmania.model.entities.movings;

import java.util.List;
import java.util.Random;

import dungeonmania.model.entities.collectables.equipment.Armour;

public class PlayerDefaultState implements PlayerState {
    
    private Player player;
    public static final int ARMOUR_DROP_CHANCE = 10;

    public PlayerDefaultState(Player player) {
        this.player = player;
    }

    @Override
    public void battle(MovingEntity opponent) {
        List<MovingEntity> allies = player.getAllies();
        if (allies.contains(opponent)) {
            return;
        }

        int damageToOpponent = player.getCurrentAttackDamage();
        int damageToPlayer =
            opponent.getDefaultBattleDamange() +
            player.applyDefenceToOpponentAttack(opponent.getAttackDamage());

        int playerHealth = player.getHealth();
        int opponentHealth = opponent.getHealth();
        // Battles only last a single tick
        while (playerHealth >= 0 && opponentHealth >= 0) {
            opponent.setHealth(opponent.getHealth() - damageToOpponent);
            player.setHealth(player.getHealth() - damageToPlayer);

            playerHealth = player.getHealth();
            opponentHealth = opponent.getHealth();
        }

        // If the player wins against a zombie or mercenary, there is a small chance
        // that armour is dropped (and placed into the player's inventory)
        Random random = new Random();
        int num = random.nextInt(100);
        
        if(num <= ARMOUR_DROP_CHANCE) {
            Armour armour = new Armour(null);
            player.addInventoryItem(armour);
        }
    }

    @Override
    public void updateState(Player player) {}
}
