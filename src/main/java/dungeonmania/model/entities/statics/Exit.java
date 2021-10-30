package dungeonmania.model.entities.statics;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.movings.MovingEntityBehaviour;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.util.Position;

public class Exit extends Entity {

    public Exit(Position position) {
        super(position, false, true);
    }

    @Override
    public void interact(Dungeon dungeon, MovingEntityBehaviour character) {
        if (character instanceof Player) dungeon.reachedExit();
    }
}
