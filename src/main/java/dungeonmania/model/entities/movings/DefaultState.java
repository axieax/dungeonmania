package dungeonmania.model.entities.movings;

public class DefaultState implements PlayerState {
    Player player;

    public DefaultState(Player player) {
        this.player = player;
    }

    @Override
    public void battle(MovingEntity opponent) {
        player.setHealth(
            player.getHealth() - opponent.getHealth() * opponent.getAttackDamage() / 10 
        );

        opponent.setHealth(
            opponent.getHealth() - player.getHealth() * player.getAttackDamage() / 5
        );
    }
    
}
