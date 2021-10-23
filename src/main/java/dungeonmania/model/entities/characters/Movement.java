package dungeonmania.model.entities.characters;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public interface Movement {
    public void move(Direction direction);

    public void moveTo(Position position);

}