package dungeonmania.model.character.entities;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public interface Movement {
    public void move(Direction direction);

    public void moveTo(Position position);

}
