package dungeonmania.model.entities.movings.player;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.Enemy;

public interface PlayerState {
    public void battle(Game game, Enemy opponent);

    public void updateState(Player player);

    public int ticksLeft();
    
}
