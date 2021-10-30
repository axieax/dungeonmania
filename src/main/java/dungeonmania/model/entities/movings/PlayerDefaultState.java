package dungeonmania.model.entities.movings;

import java.util.List;

public class PlayerDefaultState implements PlayerState {
    
    private Player player;

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
        // battles only last a single tick
        while (playerHealth >= 0 && opponentHealth >= 0) {
            opponent.setHealth(opponent.getHealth() - damageToOpponent);
            player.setHealth(player.getHealth() - damageToPlayer);

            playerHealth = player.getHealth();
            opponentHealth = opponent.getHealth();
        }
    }

    @Override
    public void updateState(Player player) {}
}
