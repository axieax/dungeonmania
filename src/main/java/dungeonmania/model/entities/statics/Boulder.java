package dungeonmania.model.entities.statics;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.List;

public class Boulder extends Entity {

    public Boulder(Position position) {
        super("boulder", position);
    }

    /**
     * If the boulder is interacted by the player, it moves the boulder to the next tile.
     */
    @Override
    public void interact(Game game, Entity character) {
        if (character instanceof Player)
            this.moveBoulder(game, ((Player) character).getDirection());
    }

    /**
     * Moves the boulder to the specified direction if it is an empty tile or the
     * entity in the directed position is a floor switch.
     *
     * @param game
     * @param direction
     */
    private void moveBoulder(Game game, Direction direction) {
        Position newPosition = this.getOffsetPosition(direction);
        List<Entity> entities = game.getEntities(newPosition);

        if (entities.isEmpty()) {
            this.setPosition(newPosition);
        } else {
            for (Entity entity : entities) {
                if (entity instanceof FloorSwitch) {
                    this.setPosition(newPosition);
                    ((FloorSwitch) entity).triggerSwitch(game);
                    break;
                }
            }
        }
    }
}
