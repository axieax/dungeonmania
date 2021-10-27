package dungeonmania.model.entities.movings;

import dungeonmania.model.entities.Item;

public class InvisibleState implements PlayerState {
    Player player;

    public InvisibleState(Player player) {
        this.player = player;
    }

    /**
     * Player immediately become "invisible" and can move past all other entities undetected.
     * This is implemented by simulating attacks from both parties as having no effect.
     */
    @Override
    public void battle(MovingEntity opponent) {
        Item potion = player.getItem("invisibility_potion");
        int potionUsesLeft = player.getInvisibilityPotionUses();

        // potion effects have vanished
        if(potionUsesLeft == 0) {
            player.setState(player.getDefaultState());
            return;
        }
        
        player.reduceInvisibilityPotionUses(potion);
    }
    
}
