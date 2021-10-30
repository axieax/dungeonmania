package dungeonmania.model.entities.statics;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.collectables.Bomb;
import dungeonmania.model.entities.movings.MovingEntityBehaviour;
import dungeonmania.util.Position;
import java.util.List;

public class FloorSwitch extends Entity {

    public FloorSwitch(Position position) {
        super(position, false, true);
    }

    /**
     * Checks if the FloorSwitch is triggered by a boulder.
     * @param dungeon
     * @return
     */
    public boolean isTriggered(Dungeon dungeon) {
        List<Entity> entities = dungeon.getEntitiesAtPosition(this.getPosition());
        return entities.stream().anyMatch(entity -> (entity instanceof Boulder));
    }

    @Override
    public void interact(Dungeon dungeon, MovingEntityBehaviour character) {
        // TODO
    }

    /**
     * When called, trigger the switch so that cardinally adjacent bombs explode, destroying 
     * all entities in the bomb's blast radius, except for the character.
     * @param dungeon
     */
    public void triggerSwitch(Dungeon dungeon) {
        if (this.isTriggered(dungeon)) {
            List<Entity> entities = dungeon.getCardinallyAdjacentEntities(this.getPosition());
            entities.forEach(
                entity -> {
                    if (entity instanceof Bomb) ((Bomb) entity).explode(dungeon);
                }
            );
        }
    }
}
