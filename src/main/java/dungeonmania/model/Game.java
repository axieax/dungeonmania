package dungeonmania.model;

import dungeonmania.EntityFactory;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.buildables.BuildableEquipment;
import dungeonmania.model.entities.movings.Character;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.model.entities.statics.Portal;
import dungeonmania.model.goal.Goal;
import dungeonmania.model.mode.Mode;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public final class Game {

    private final String dungeonId;
    private final String dungeonName;
    private List<Entity> entities = new ArrayList<>();
    private final Goal goal;
    private final Mode mode;

    public Game(String dungeonName, List<Entity> entities, Goal goal, Mode mode) {
        this.dungeonId = UUID.randomUUID().toString();
        this.dungeonName = dungeonName;
        this.entities = entities;
        this.goal = goal;
        this.mode = mode;
        // TODO: attach observers
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

    public final Player getCharacter() {
        // TODO: import Character class
        return entities
            .stream()
            .filter(e -> e instanceof Player)
            .map(e -> (Player) e)
            .findFirst()
            .orElse(null);
    }

    public final Mode getMode() {
        return mode;
    }

    public final List<Entity> getEntities() {
        return entities;
    }

    public final Goal getGoal () {
        return goal;
    }

    public final Mode getMode () {
        return mode;
    }

    public final String getDungeonName() {
        return dungeonName;
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

    public final List<Position> getMoveablePositions(MovingEntity from, Position position) {
        int x = position.getX();
        int y = position.getY();
        List<Position> positions = Arrays.asList(
            new Position(x, y + 1),
            new Position(x - 1, y),
            new Position(x + 1, y),
            new Position(x, y - 1)
        );
        getCardinallyAdjacentEntities(position)
            .stream()
            .forEach(
                e -> {
                    if (from.collision(e)) positions.remove(e.getPosition());
                }
            );
        return positions;
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
            .filter(
                e -> {
                    // cardinally adjacent if one coordinate is (1 or -1) with the other 0
                    Position difference = Position.calculatePositionBetween(
                        e.getPosition(),
                        position
                    );
                    int xDiff = Math.abs(difference.getX());
                    int yDiff = Math.abs(difference.getY());
                    return (
                        // ensure both xDiff and yDiff are either 0 or 1
                        (xDiff == (xDiff & 1)) &&
                        (yDiff == (yDiff & 1)) &&
                        // logical XOR to check x and y are different
                        ((xDiff == 1) ^ (yDiff == 1))
                    );
                }
            )
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
            entities.stream().map(Entity::getEntityResponse).collect(Collectors.toList()),
            this.getCharacter().getInventoryResponses(),
            this.getBuildables(),
            goal.toString()
        );
    }

    private final List<String> getBuildables() {
        Character player = (Character) getCharacter();
        EntityFactory
            .getBuildableEquipments()
            .stream()
            .filter(eq -> player.checkBuildable(eq))
            .map(eq -> eq.getPrefix())
            .collect(Collectors.toList());
    }

    public final DungeonResponse tick(String itemUsedId, Direction movementDirection) {
        entities
            .stream()
            .filter(e -> e instanceof MovingEntity)
            .forEach(e -> ((MovingEntity) e).tick(this));
        return getDungeonResponse();
    }

    public final DungeonResponse build(String buildable) {
        Character player = (Character) getCharacter();
        BuildableEquipment item = EntityFactory.getBuildable(buildable);
        player.craft(item);
        return getDungeonResponse();
    }

    public final DungeonResponse interact(String entityId) {
        Entity player = getCharacter();
        Entity entity = getEntity(entityId);
        player.interact(this, (MovingEntity) entity);
        return getDungeonResponse();
    }
}
