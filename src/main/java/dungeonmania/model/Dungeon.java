package dungeonmania.model;

import java.util.List;

import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.statics.Portal;
import dungeonmania.util.Position;

public class Dungeon {
    public Dungeon(int width, int height) {

    }

    public void addEntity(Entity entity) {

    }

    public void removeEntity(Entity entity) {

    }

    public Entity getEntity(String entityId) {
        return null;
    }

    public Entity getEntityAtPosition(Position position) {
        return null;
    }

    public List<Entity> getEntitiesAtPosition(Position position) {
        return null;
    }

    public List<Entity> getAllEntities() {
        return null;
    }

    public List<Portal> getAllPortals() {
        return null;
    }

    public List<Entity> getAdjacentEntities(Position position) {
        return null;
    }

    public List<Entity> getCardinallyAdjacentEntities(Position position) {
        return null;
    }

    public void reachedExit() {
        return;
    }
}
