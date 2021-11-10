package dungeonmania.model;

import dungeonmania.EntityFactory;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.Tickable;
import dungeonmania.model.entities.buildables.Buildable;
import dungeonmania.model.entities.movings.BribableEnemy;
import dungeonmania.model.entities.movings.Hydra;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.model.entities.movings.Spider;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.statics.Boulder;
import dungeonmania.model.entities.statics.FloorSwitch;
import dungeonmania.model.entities.statics.Portal;
import dungeonmania.model.entities.statics.ZombieToastSpawner;
import dungeonmania.model.goal.Goal;
import dungeonmania.model.mode.Mode;
import dungeonmania.response.models.AnimationQueue;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public final class Game {

    private final String dungeonId;
    private final String dungeonName;
    private final List<Entity> entities;
    private final Goal goal;
    private final Mode mode;

    private int MAX_WIDTH = 50;
    private int MAX_HEIGHT = 50;

    private int tick = 0;

    public Game(String dungeonName, List<Entity> entities, Goal goal, Mode mode) {
        this.dungeonId = UUID.randomUUID().toString();
        this.dungeonName = dungeonName;
        this.entities = new ArrayList<>(entities);
        this.goal = goal;
        this.mode = mode;
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
        return entities
            .stream()
            .filter(e -> e instanceof Player)
            .map(e -> (Player) e)
            .findFirst()
            .orElse(null);
    }

    public final List<Entity> getEntities() {
        return entities;
    }

    public final Goal getGoal() {
        return goal;
    }

    public final Mode getMode() {
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
        List<Position> positions = new ArrayList<>();
        positions.add(new Position(x, y + 1));
        positions.add(new Position(x - 1, y));
        positions.add(new Position(x + 1, y));
        positions.add(new Position(x, y - 1));
        getCardinallyAdjacentEntities(position)
            .stream()
            .forEach(e -> {
                // consider portals as moveable positions since all moving entities can teleport
                if (from.collision(e) && !(e instanceof Portal)) positions.remove(e.getPosition());
            });
        return positions
            .stream()
            .filter(pos ->
                (
                    pos.getX() >= 0 &&
                    pos.getX() < MAX_WIDTH &&
                    pos.getY() >= 0 &&
                    pos.getY() < MAX_HEIGHT
                )
            )
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
                Position difference = Position.calculatePositionBetween(e.getPosition(), position);
                int xDiff = Math.abs(difference.getX());
                int yDiff = Math.abs(difference.getY());
                return (
                    // ensure both xDiff and yDiff are either 0 or 1
                    (xDiff == (xDiff & 1)) &&
                    (yDiff == (yDiff & 1)) &&
                    // logical XOR to check x and y are different
                    ((xDiff == 1) ^ (yDiff == 1))
                );
            })
            .collect(Collectors.toList());
    }

    /**
     * Returns a starter DungeonResponse object for the Dungeon
     *
     * @return DungeonResponse for the Dungeon
     */
    public final DungeonResponse getDungeonResponse() {
        Player player = getCharacter();
        return new DungeonResponse(
            dungeonId,
            dungeonName,
            entities.stream().map(Entity::getEntityResponse).collect(Collectors.toList()),
            (player != null) ? player.getInventoryResponses() : new ArrayList<ItemResponse>(),
            this.getBuildables(),
            formatGoal(),
            generateAnimations()
        );
    }

    /**
     * Formats the Goal string, removing any brackets if necessary
     *
     * @return Goal string for DungeonResponse
     */
    private final String formatGoal() {
        if (goal == null || getCharacter() == null) return "";
        String goalString = goal.toString(this);
        // remove starting and closing brackets
        if (goalString.startsWith("(") && goalString.endsWith(")")) {
            goalString = goalString.substring(1, goalString.length() - 1);
        }
        return goalString;
    }

    /**
     * Generates animations for the Dungeon
     *
     * @return animations for all Entities which can be animated
     */
    private final List<AnimationQueue> generateAnimations() {
        List<AnimationQueue> animations = entities
            .stream()
            .filter(e -> e.getAnimation() != null)
            .map(e -> e.getAnimation())
            .collect(Collectors.toList());
        animations.addAll(boulderAnimations());
        return animations;
    }

    /**
     * Generates animations for boulders on switches
     *
     * @return list of boulder animations
     */
    private final List<AnimationQueue> boulderAnimations() {
        // locate switch positions
        Set<Position> switchPositions = new HashSet<>();
        for (Entity e : entities) {
            Position pos = e.getPosition();
            if (e instanceof FloorSwitch) switchPositions.add(pos.asLayer(0));
        }

        // different skins for boulders on switches
        return entities
            .stream()
            .filter(e ->
                e instanceof Boulder && switchPositions.contains(e.getPosition().asLayer(0))
            )
            .map(e ->
                new AnimationQueue(
                    "PostTick",
                    e.getId(),
                    Arrays.asList("sprite boulder_on_switch"),
                    false,
                    -1
                )
            )
            .collect(Collectors.toList());
    }

    /**
     * Gets a list of items which the Player can build
     *
     * @return list of buildable items (String)
     */
    private final List<String> getBuildables() {
        Player player = getCharacter();
        if (player == null) return new ArrayList<String>();
        return EntityFactory
            .allBuildables()
            .stream()
            .filter(item -> item instanceof Item && player.checkBuildable(item))
            .map(item -> ((Item) item).getType())
            .collect(Collectors.toList());
    }

    public final DungeonResponse tick(String itemUsedId, Direction movementDirection)
        throws IllegalArgumentException, InvalidActionException {
        if (itemUsedId != null && itemUsedId.length() == 0) throw new IllegalArgumentException(
            itemUsedId
        );
        this.tick += 1;

        List<Tickable> tickables = entities
            .stream()
            .filter(e -> e instanceof Tickable)
            .map(e -> (Tickable) e)
            .collect(Collectors.toList());

        // Player moves before other entities (so that bribable enemies can follow the player)
        getCharacter().move(this, movementDirection, itemUsedId);

        // Separate loop to avoid concurrency issues when zombie spawner adds new entity
        tickables.forEach(e -> {
            if (!(e instanceof Player)) {
                ((Tickable) e).tick(this);
            }
        });

        Spider.spawnSpider(this, this.mode.damageMultiplier());
        Hydra.spawnHydra(this, this.mode.damageMultiplier());
        return getDungeonResponse();
    }

    public final DungeonResponse build(String buildable) throws InvalidActionException {
        Player player = getCharacter();
        Buildable item = EntityFactory.getBuildable(buildable);
        player.craft(item);
        return getDungeonResponse();
    }

    public final DungeonResponse interact(String entityId)
        throws IllegalArgumentException, InvalidActionException {
        if (!entities.stream().map(Entity::getId).collect(Collectors.toList()).contains(entityId)) {
            throw new IllegalArgumentException();
        }
        MovingEntity player = getCharacter();
        Entity entity = getEntity(entityId);
        if (entity instanceof BribableEnemy) {
            ((BribableEnemy) entity).interact(this, (Player) player);
        } else if (entity instanceof MovingEntity) {
            player.interact(this, (MovingEntity) entity);
        } else if (entity instanceof ZombieToastSpawner) {
            ZombieToastSpawner spawner = (ZombieToastSpawner) entity;
            spawner.interact(this, player);
        }
        return getDungeonResponse();
    }

    public int getTick() {
        return tick;
    }

    public int getTickRate() {
        return mode.tickRate();
    }
}
