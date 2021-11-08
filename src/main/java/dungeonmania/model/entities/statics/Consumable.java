package dungeonmania.model.entities.statics;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.player.Player;

public interface Consumable {
    
    /**
     * Apply any effects of any consumable item
     * @param game
     * @param item that is consumable
     */
    public void consume(Game game, Player player);
}
