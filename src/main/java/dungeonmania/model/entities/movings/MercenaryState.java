package dungeonmania.model.entities.movings;

import dungeonmania.model.Dungeon;
import dungeonmania.util.Position;

public interface MercenaryState {
    public void move(Dungeon dungeon, Position playerPos);
}
