package dungeonmania.model.entities.movings;

import dungeonmania.model.entities.Item;

public class InvincibleState implements PlayerState {
    Player player;

    public InvincibleState(Player player) {
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
        Item potion = player.getItem("invincibility_potion");
        int potionUsesLeft = player.getInvincibilityPotionUses();

        // potion effects have vanished
        if(potionUsesLeft == 0) {
            player.setState(player.getDefaultState());
            return;
        }
        
        opponent.kill();
        player.reduceInvincibilityPotionUses(potion);

        // if after fighting no uses of potion left
        if(potionUsesLeft == 0) {
            player.setState(player.getDefaultState());
        }
    }
    
    
}
