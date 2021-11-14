package dungeonmania.model.entities.statics;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.List;

public class Boulder extends Entity {

    public Boulder(Position position) {
        super("boulder", position);
    }

    @Override
    public void interact(Game game, Entity character) throws InvalidActionException {
        // If the boulder is interacted by the player, it moves the boulder to the next tile.
        if (character instanceof Player) {
            this.moveBoulder(game, ((Player) character).getDirection());
        }
    }

    /**
     * Moves the boulder to the specified direction if it is an empty tile or the
     * entity in the directed position is a floor switch.
     *
     * @param game game state
     * @param direction to move the boulder
     */
    private void moveBoulder(Game game, Direction direction) {
        Position newPosition = this.getOffsetPosition(direction);
        List<Entity> entities = game.getEntities(newPosition);

        if (entities.isEmpty() || entities.stream().allMatch(e -> e.isPassable() && !(e instanceof MovingEntity))) {
            this.setPosition(newPosition);
            for (Entity entity : entities) {
                if (entity instanceof FloorSwitch) {
                    ((FloorSwitch) entity).triggerSwitch(game);
                    return;
                }
            }
        }
    }
}
