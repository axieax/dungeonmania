package dungeonmania.model.entities.movings;

public class ArmouredState implements PlayerState {
    Player player;

    public ArmouredState(Player player) {
        this.player = player;
    }

    /**
     * Body armour provides defence and halves enemy attack
     */
    @Override
    public void battle(MovingEntity opponent) {
        // If no durability left, then armour deteriorates and cannot be used
        if(!player.hasArmour()) {
            player.setState(player.getDefaultState());
        }
        
        // battle
        player.setHealth(
            player.getHealth() - opponent.getHealth() * opponent.getAttackDamage() / 20 
        );

        opponent.setHealth(
            opponent.getHealth() - player.getHealth() * player.getAttackDamage() / 5
        );

        player.reduceArmourDurability();

        // if after fighting player has no armour
        if(!player.hasArmour()) {
            player.setState(player.getDefaultState());
        }
    }
}
