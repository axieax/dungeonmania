package dungeonmania.model.entities.statics;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.movings.Enemy;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.model.entities.movings.ZombieToast;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.json.JSONObject;

public class Portal extends Entity {

    private String colour;

    public Portal(Position position, String colour) {
        super("portal", position, false, false);
        this.colour = colour;
    }

    /**
     * Get the colour of a Portal
     *
     * @return colour
     */
    public String getColour() {
        return colour;
    }

    @Override
    public void interact(Game game, Entity character) {
        // If a entity interacts with the Portal, they can be teleported to the
        // corresponding portal if the tile on the destination portal is free for
        // the entity to go through by.
        if (!(character instanceof MovingEntity)) return;
        this.teleport(game, (MovingEntity) character);
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
     * Teleports the entity exactly to the tile where the corresponding portal is located.
     *
     * @param game game state
     * @param character entity to interact with 
     */
    public void teleport(Game game, MovingEntity character) {
        Portal portal = this.findPortal(game);
        // zombies cannot go through portal
        if (portal != null && !(character instanceof ZombieToast)) {
            Position teleportedPosition = portal
                .getPosition()
                .translateBy(character.getDirection());
            boolean collision = false;
            for (Entity e : game.getEntities(teleportedPosition)) {
                if (character.collision(e)) collision = true;
            }
            if (!collision) character.moveTo(portal.getPosition());
        } else if (character instanceof Enemy) ((Enemy) character).setDirection(Direction.NONE);
    }

    @Override
    public JSONObject toJSON() {
        JSONObject info = super.toJSON();
        info.put("colour", colour);
        return info;
    }

    @Override
    public EntityResponse getEntityResponse() {
        String type = String.format("%s_%s", getType(), colour.toLowerCase());
        return new EntityResponse(getId(), type, getPosition(), isInteractable());
    }

}
