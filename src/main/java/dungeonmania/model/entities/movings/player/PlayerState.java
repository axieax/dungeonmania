package dungeonmania.model.entities.movings.player;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.MovingEntity;

public interface PlayerState {
    public void battle(Game game, MovingEntity opponent);

    public void updateState(Player player);

    public int ticksLeft();
    
}
