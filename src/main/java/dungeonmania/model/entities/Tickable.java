package dungeonmania.model.entities;

import dungeonmania.exceptions.PlayerDeadException;
import dungeonmania.model.Game;

public interface Tickable {

    /**
     * Simulates a tick for an Entity
     *
     * @param game game state
     *
     * @throws PlayerDeadException to be thrown if player dies
     */
    public void tick(Game game) throws PlayerDeadException;
}
