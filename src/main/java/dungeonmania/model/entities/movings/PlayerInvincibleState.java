package dungeonmania.model.entities.movings;

public class PlayerInvincibleState implements PlayerState {
    
    private Player player;
    private int timeLimit = 2;

    public PlayerInvincibleState(Player player) {
        this.player = player;
    }

    /**
     * Any battles that occur when the character has the effects of the potion
     * end immediately, with the character immediately winning.
     * The effects of the potion only last for a limited time.
     * 
     * NOTE: Durability of any weapons e.g. sword or bow
     *       are not reduced while a player is invincible.
     */
    @Override
    public void battle(MovingEntity opponent) {
        opponent.kill();
    }

    @Override
    public void updateState(Player player) {
        timeLimit--;
        if (timeLimit == 0) {
            this.player.setState(new PlayerDefaultState(player));
        }
    }
    
    
}