package dungeonmania.model.entities.movings;

import dungeonmania.model.Game;

public interface PlayerState {
    public void battle(Game game, MovingEntity opponent);

    public void updateState(Player player);
}
