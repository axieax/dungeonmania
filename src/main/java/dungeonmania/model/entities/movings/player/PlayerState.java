package dungeonmania.model.entities.movings.player;

import dungeonmania.exceptions.PlayerDeadException;
import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.Enemy;

public interface PlayerState {
    public void battle(Game game, Enemy opponent) throws PlayerDeadException;

    public void updateState(Player player);

    public int ticksLeft();

    public void setTicksLeft (int left);
}
