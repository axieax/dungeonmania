package dungeonmania.model.entities.movings.older_player;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.MovingEntity;

public interface OlderPlayerState {
    // public void battle(Game game, MovingEntity opponent);

    public void move(Game game, OlderPlayer ilNam);
    // public void updateState(OlderPlayer ilNam);
}
