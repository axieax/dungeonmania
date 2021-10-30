package dungeonmania.model.entities.statics;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Boulder extends Entity {

    public Boulder(Position position) {
        super("boulder", position);
    }

    /**
     * If the boulder is interacted by the player, it moves the boulder to the next
     * tile.
     */
    @Override
    public void interact(Game game, MovingEntity character) {
        if (character instanceof Player) {
            this.moveBoulder(game, character.getDirection());
        }
    }

    /**
     * Moves the boulder to the specified direction if it is an empty tile or the
     * entity in the directed position is a floor switch.
     *
     * @param game
     * @param direction
     */
    public void moveBoulder(Game game, Direction direction) {
        Position newPosition = this.getOffsetPosition(direction);
        Entity entityAtPosition = game.getEntityAtPosition(newPosition);
        if (entityAtPosition == null || entityAtPosition instanceof FloorSwitch) {
            this.setPosition(newPosition);
        }
        if (entityAtPosition instanceof FloorSwitch)
            ((FloorSwitch) entityAtPosition).triggerSwitch(game);
    }
}
