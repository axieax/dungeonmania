package dungeonmania.model.entities.movings;

public class DefensiveState implements PlayerState {
    Player player;

    public DefensiveState(Player player) {
        this.player = player;
    }

    // TODO: This class should handle any "Defensive" items a player
    //       has during battle e.g. armour and shield
    //       look through inventory and use any/all by computing total defence damange reduced
    //       Player switches to this class when it collects items?
    
    /**
     * Body armour provides defence and halves enemy attack
     */
    @Override
    public void battle(MovingEntity opponent) {
        // If no durability left, then armour deteriorates and cannot be used
        if(!player.hasArmour()) {
            player.setState(player.getDefaultState());
            return;
        }
        
        // divide by 20 as armour halves attacks
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
