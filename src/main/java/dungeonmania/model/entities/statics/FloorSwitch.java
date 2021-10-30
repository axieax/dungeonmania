package dungeonmania.model.entities.statics;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.collectables.Bomb;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.util.Position;
import java.util.List;

public class FloorSwitch extends Entity {

    public FloorSwitch(Position position) {
        super("switch", position, false, true);
    }

    /**
     * Checks if the FloorSwitch is triggered by a boulder.
     * @param game
     * @return
     */
    public boolean isTriggered(Game game) {
        List<Entity> entities = game.getEntitiesAtPosition(this.getPosition());
        return entities.stream().anyMatch(entity -> (entity instanceof Boulder));
    }

    @Override
    public void interact(Game game, MovingEntity character) {
        // TODO
    }

    /**
     * When called, trigger the switch so that cardinally adjacent bombs explode, destroying 
     * all entities in the bomb's blast radius, except for the character.
     * @param game
     */
    public void triggerSwitch(Game game) {
        if (this.isTriggered(game)) {
            List<Entity> entities = game.getCardinallyAdjacentEntities(this.getPosition());
            entities.forEach(
                entity -> {
                    if (entity instanceof Bomb) ((Bomb) entity).explode(game);
                }
            );
        }
    }
}
