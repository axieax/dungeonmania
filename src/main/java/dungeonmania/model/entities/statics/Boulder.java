package dungeonmania.model.entities.statics;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Boulder extends Entity {

    public Boulder(Position position) {
        super(position);
    }

    /**
     * If the boulder is interacted by the player, it moves the boulder to the next
     * tile.
     */
    @Override
    public void interact(Dungeon dungeon, MovingEntity character) {
        if (character instanceof Player) {
            this.moveBoulder(dungeon, character.getDirection());
        }
    }

    /**
     * Moves the boulder to the specified direction if it is an empty tile or the
     * entity in the directed position is a floor switch.
     *
     * @param dungeon
     * @param direction
     */
    public void moveBoulder(Dungeon dungeon, Direction direction) {
        Position newPosition = this.getOffsetPosition(direction);
        Entity entityAtPosition = dungeon.getEntityAtPosition(newPosition);
        if (entityAtPosition == null || entityAtPosition instanceof FloorSwitch) {
            this.setPosition(newPosition);
        }
        if (entityAtPosition instanceof FloorSwitch)
            ((FloorSwitch) entityAtPosition).triggerSwitch(dungeon);
    }
}
