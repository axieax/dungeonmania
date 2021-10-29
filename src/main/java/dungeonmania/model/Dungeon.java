package dungeonmania.model;

import dungeonmania.EntityFactory;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.buildables.BuildableEquipment;
import dungeonmania.model.entities.movings.Character;
import dungeonmania.model.entities.statics.Portal;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class Dungeon {

    private final String dungeonId;
    private final String dungeonName;
    private final List<Entity> entities = new ArrayList<>();

    public Dungeon(String dungeonId, String dungeonName) {
        this.dungeonName = dungeonName;
        setupDungeon();
    }

    private final void setupDungeon() {
        entities = EntityFactory.loadDungeon(dungeonName);
    }

    public final void addEntity(Entity entity) {
        entities.add(entity);
    }

    public final boolean removeEntity(Entity entity) {
        return entities.remove(entity);
    }

    public final Entity getEntity(String entityId) {
        return entities.stream().filter(e -> e.getId().equals(entityId)).findFirst().orElse(null);
    }

    public final Entity getCharacter() {
        // TODO: import Character class
        return entities.stream().filter(e -> e instanceof Character).findFirst().orElse(null);
    }

    public final List<Entity> getEntities() {
        return entities;
    }

    public final List<Entity> getEntities(Position position) {
        return entities
            .stream()
            .filter(e -> e.getPosition().equals(position))
            .collect(Collectors.toList());
    }

    public final List<Portal> getAllPortals() {
        return entities
            .stream()
            .filter(e -> e instanceof Portal)
            .map(e -> (Portal) e)
            .collect(Collectors.toList());
    }

    public final List<Entity> getAdjacentEntities(Position position) {
        return entities
            .stream()
            .filter(e -> Position.isAdjacent(e.getPosition(), position))
            .collect(Collectors.toList());
    }

    public final List<Entity> getCardinallyAdjacentEntities(Position position) {
        return getAdjacentEntities(position)
            .stream()
            .filter(e -> {
                // cardinally adjacent if one coordinate is (1 or -1) with the other 0
                // NOTE: can use & to extract last bit and ensure number is 0 or 1
                // then return x XOR y
                Position difference = Position.calculatePositionBetween(e.getPosition(), position);
                int xDiff = Math.abs(difference.getX());
                int yDiff = Math.abs(difference.getY());
                if ((xDiff == 0 && yDiff == 1) || (yDiff == 0 && xDiff == 1)) return true;
                return false;
            })
            .collect(Collectors.toList());
    }

    /**
     * Returns a starter DungeonResponse object for the Dungeon
     *
     * @return DungeonResponse for the Dungeon
     */
    public final DungeonResponse getDungeonResponse() {
        return new DungeonResponse(
            dungeonId,
            dungeonName,
            entities.stream().map(Entity::getEntityResponse),
            getCharacter().getInventoryResponse,
            getBuildables(),
            ""
        );
    }

    private final List<BuildableEquipment> getBuildables() {
        Character player = (Character) getCharacter();
        EntityFactory.getBuildableEquipments().stream().filter(eq -> player.checkBuildable(eq));
    }
}
