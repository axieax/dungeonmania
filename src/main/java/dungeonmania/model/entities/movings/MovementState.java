package dungeonmania.model.entities.movings;

import dungeonmania.model.Game;
import dungeonmania.util.Position;

public interface MovementState {
    public void move(Game game, Position playerPos);
}
