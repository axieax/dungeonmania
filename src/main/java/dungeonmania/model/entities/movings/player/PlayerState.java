package dungeonmania.model.entities.movings.player;

import dungeonmania.exceptions.PlayerDeadException;
import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.Enemy;

public interface PlayerState {

    /**
     * Battles an enemy
     *
     * @param game game state
     * @param opponent enemy
     *
     * @throws PlayerDeadException if player dead lol
     */
    public void battle(Game game, Enemy opponent) throws PlayerDeadException;

    /**
     * Update player state
     *
     * @param player player to update
     */
    public void updateState(Player player);

    /**
     * Number of ticks left
     *
     * @return ticks left
     */
    public int ticksLeft();
}
