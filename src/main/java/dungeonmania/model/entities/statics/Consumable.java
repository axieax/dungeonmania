package dungeonmania.model.entities.statics;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.player.Player;

public interface Consumable {

    /**
     * Apply any effects of any consumable item
     *
     * @param game game state
     * @param player player consuming item
     */
    public void consume(Game game, Player player);
}
