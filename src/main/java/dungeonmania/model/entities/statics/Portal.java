package dungeonmania.model.entities.statics;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.util.Position;

public class Portal extends Entity {

    private String colour;

    public String getColour() {
        return colour;
    }

    public Portal(Position position, String colour) {
        super("portal", position);
        this.colour = colour;
    }

    /**
     * If a entity interacts with the Portal, they can be teleported to the
     * corresponding portal if the tile on the destination portal is free for
     * the entity to go through by.
     */
    @Override
    public void interact(Dungeon dungeon, MovingEntity character) {
        this.teleport(dungeon, character);
    }

    public Portal findPortal(Dungeon dungeon) {
        return dungeon
            .getAllPortals()
            .stream()
            .filter(
                portal -> portal.getColour().equals(this.colour) && portal.getId() != this.getId()
            )
            .findFirst()
            .orElse(null);
    }

    /**
     * Teleports the entity exactly to the tile where the corresponding portal is
     * located at.
     */
    public void teleport(Dungeon dungeon, MovingEntity character) {
        Portal portal = this.findPortal(dungeon);
        Position teleportedPosition = portal.getPosition().translateBy(character.getDirection());
        Entity entityAtPortal = dungeon.getEntityAtPosition(teleportedPosition);
        if (portal != null && character.isCollidable(entityAtPortal)) {
            character.moveTo(portal.getPosition());
        }
    }
}
