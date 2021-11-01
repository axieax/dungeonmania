package dungeonmania.model.entities.statics;

import java.util.List;

import org.json.JSONObject;

import dungeonmania.model.Game;
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
    public void interact(Game game, MovingEntity character) {
        this.teleport(game, character);
    }

    public Portal findPortal(Game game) {
        return game
            .getAllPortals()
            .stream()
            .filter(portal ->
                portal.getColour().equals(this.colour) && portal.getId() != this.getId()
            )
            .findFirst()
            .orElse(null);
    }

    /**
     * Teleports the entity exactly to the tile where the corresponding portal is
     * located at.
     */
    public void teleport(Game game, MovingEntity character) {
        Portal portal = this.findPortal(game);
        Position teleportedPosition = portal.getPosition().translateBy(character.getDirection());
        List<Entity> entities = game.getEntities(teleportedPosition);
        if (portal != null) {
            boolean collision = false;
            for (Entity entity : entities) {
                if (character.collision(entity)) collision = true;
            }
            if (!collision) character.moveTo(portal.getPosition());
        }
    }

    @Override 
    public JSONObject toJSON() {
        JSONObject info = super.toJSON();
        info.put ("colour", colour);
        return info;
    }
}
