package dungeonmania.model;

import dungeonmania.EntityFactory;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.exceptions.PlayerDeadException;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.Tickable;
import dungeonmania.model.entities.buildables.Buildable;
import dungeonmania.model.entities.movings.BribableEnemy;
import dungeonmania.model.entities.movings.Enemy;
import dungeonmania.model.entities.movings.Hydra;
import dungeonmania.model.entities.movings.Mercenary;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.model.entities.movings.Spider;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.statics.Boulder;
import dungeonmania.model.entities.statics.FloorSwitch;
import dungeonmania.model.entities.statics.Portal;
import dungeonmania.model.entities.statics.SwampTile;
import dungeonmania.model.entities.statics.TimeTravellingPortal;
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
    private Position playerSpawnLocation;
    private int tick = 0;

    public Game(String dungeonName, List<Entity> entities, Goal goal, Mode mode) {
        this.dungeonId = UUID.randomUUID().toString();
        this.dungeonName = dungeonName;
        this.entities = new ArrayList<>(entities);
        this.goal = goal;
        this.mode = mode;
        this.playerSpawnLocation =
            (getPlayer() != null) ? getPlayer().getPosition() : new Position(0, 0);
    }

    /**
     * Adds an Entity to the game
     *
     * @param entity to be added
     */
    public final void addEntity(Entity entity) {
        entities.add(entity);
    }

    /**
     * Removes an Entity from the game
     *
     * @param entity entity to be removed
     *
     * @return true if successfully removed, false otherwise
     */
    public final boolean removeEntity(Entity entity) {
        return entities.remove(entity);
    }

    /**
     * Gets an Entity from the game by id
     *
     * @param entityId id of the Entity
     *
     * @return Entity with the given id
     */
    public final Entity getEntity(String entityId) {
        return entities.stream().filter(e -> e.getId().equals(entityId)).findFirst().orElse(null);
    }

    /**
     * Get the Player from the game
     *
     * @return Player object
     */
    public final Player getPlayer() {
        return entities
            .stream()
            .filter(e -> e instanceof Player)
            .map(e -> (Player) e)
            .findFirst()
            .orElse(null);
    }

    /**
     * Return the current Player position
     * @return Position object
     */
    public Position getCharacterPosition() {
        Player player = getPlayer();
        return player.getPosition();
    }

    /**
     * Get the list of entities in the current game
     *
     * @return list of Entity objects
     */
    public final List<Entity> getEntities() {
        return entities;
    }

    /**
     * Get the list of entities in the current game which are at a given Position
     *
     * @param position position of Entities to be returned
     * @return list of Entity objects
     */
    public final List<Entity> getEntities(Position position) {
        return entities
            .stream()
            .filter(e -> e.getPosition().equals(position))
            .collect(Collectors.toList());
    }

    /**
     * Get the player's spawn location
     *
     * @return player's spawn Position
     */
    public Position getPlayerSpawnLocation() {
        return playerSpawnLocation;
    }

    /**
     * Get the dungeon name
     *
     * @return name of the dungeon
     */
    public final String getDungeonName() {
        return dungeonName;
    }

    /**
     * Get the goal of the game
     *
     * @return Goal object
     */
    public final Goal getGoal() {
        return goal;
    }

    /**
     * Get the mode of the game
     *
     * @return Mode object
     */
    public final Mode getMode() {
        return mode;
    }

    /**
     * Get all the enemies in the game
     *
     * @return list of Enemy objects
     */
    public final List<Enemy> getAllEnemies() {
        return entities
            .stream()
            .filter(e -> e instanceof Enemy)
            .map(e -> (Enemy) e)
            .collect(Collectors.toList());
    }

    /**
     * Get all the portals in the game
     *
     * @return list of Portal objects
     */
    public final List<Portal> getAllPortals() {
        return entities
            .stream()
            .filter(e -> e instanceof Portal)
            .map(e -> (Portal) e)
            .collect(Collectors.toList());
    }

    /**
     * Finds the maximum X coordinate of any Entity positions
     *
     * @return maximum X coordinate
     */
    private final int findMaxX() {
        int maxX = 0;
        for (Entity e : entities) if (e.getX() > maxX) maxX = e.getX();
        return maxX;
    }

    /**
     * Finds the maximum Y coordinate of any Entity positions
     *
     * @return maximum Y coordinate
     */
    private final int findMaxY() {
        int maxY = 0;
        for (Entity e : entities) if (e.getY() > maxY) maxY = e.getY();
        return maxY;
    }

    /**
     * Get all the moveable positions of a given Entity from a given Position
     *
     * @param entity Entity to be moved
     * @param position Position to be moved from
     *
     * @return list of possible positions the Entity can move to
     */
    public final List<Position> getMoveablePositions(MovingEntity entity, Position position) {
        // all positions
        int x = position.getX();
        int y = position.getY();
        List<Position> positions = new ArrayList<Position>(
            Arrays.asList(
                new Position(x, y + 1),
                new Position(x - 1, y),
                new Position(x + 1, y),
                new Position(x, y - 1)
            )
        );

        // filter out collisions
        List<Position> toRemove = new ArrayList<>();
        getCardinallyAdjacentEntities(position)
            .stream()
            .forEach(e -> {
                if (entity.collision(e)) toRemove.add(e.getPosition());
            });
        positions.removeAll(toRemove);

        return positions
            .stream()
            .filter(pos ->
                (
                    pos.getX() >= 0 &&
                    pos.getX() <= this.findMaxX() &&
                    pos.getY() >= 0 &&
                    pos.getY() <= this.findMaxY()
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
                // Cardinally adjacent if one coordinate is (1 or -1) with the other 0
                Position difference = Position.calculatePositionBetween(e.getPosition(), position);
                int xDiff = Math.abs(difference.getX());
                int yDiff = Math.abs(difference.getY());
                return (
                    // Ensure both xDiff and yDiff are either 0 or 1
                    (xDiff == (xDiff & 1)) &&
                    (yDiff == (yDiff & 1)) &&
                    // Logical XOR to check that x and y are different
                    ((xDiff == 1) ^ (yDiff == 1))
                );
            })
            .collect(Collectors.toList());
    }

    /**
     * Returns the DungeonResponse object for the Dungeon
     *
     * @return DungeonResponse for the Dungeon
     */
    public final DungeonResponse getDungeonResponse() {
        Player player = getPlayer();
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
        if (goal == null || getPlayer() == null) return "";
        String goalString = goal.toString(this);
        // Remove starting and closing brackets
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
        // Locate switch positions
        Set<Position> switchPositions = new HashSet<>();
        for (Entity e : entities) {
            Position pos = e.getPosition();
            if (e instanceof FloorSwitch) switchPositions.add(pos.asLayer(0));
        }

        // Different skins for boulders on switches
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
        Player player = getPlayer();
        if (player == null) return new ArrayList<String>();
        return EntityFactory
            .allBuildables()
            .stream()
            .filter(item -> item instanceof Item && player.checkBuildable(this, item))
            .map(item -> ((Item) item).getType())
            .collect(Collectors.toList());
    }

    /**
     * Ticks the game state. When a tick occurs: - The player moves in the specified
     * direction one square - All enemies move respectively - Any items which are
     * used are 'actioned' and interact with the relevant entity
     *
     * @param itemUsedId id of item used
     * @param movementDirection Direction to move the player
     * @return DungeonResponse of current game
     * @throws IllegalArgumentException if itemUsed is not a bomb, health_potion
     *                                  invincibility_potion, or an invisibility_potion,
     *                                  or null (if no item is being used)
     * @throws InvalidActionException   if itemUsed is not in the player's inventory
     */
    public final DungeonResponse tick(String itemUsedId, Direction movementDirection)
        throws IllegalArgumentException, InvalidActionException {
        // empty item used
        if (itemUsedId != null && itemUsedId.length() == 0) {
            throw new IllegalArgumentException(itemUsedId);
        }
        this.tick += 1;

        try {
            // Player moves before other entities (so that bribable enemies can follow the player)
            getPlayer().move(this, movementDirection, itemUsedId);

            List<Tickable> tickables = entities
                .stream()
                .filter(e -> e instanceof Tickable)
                .map(e -> (Tickable) e)
                .collect(Collectors.toList());

            // Separate loop to avoid concurrency issues when zombie spawner adds new entity
            tickables.forEach(e -> {
                if (!(e instanceof Player)) {
                    ((Tickable) e).tick(this);
                }
            });

            Spider.spawnSpider(this, this.mode.damageMultiplier());
            Mercenary.spawnMercenary(this, this.mode.damageMultiplier());
            Hydra.spawnHydra(this, this.mode.damageMultiplier());
        } catch (PlayerDeadException | NullPointerException e) {}

        return getDungeonResponse();
    }

    /**
     * Builds the given entity
     *
     * @param buildable name of entity to be built
     * @return DungeonResponse of current game
     * @throws IllegalArgumentException If buildable is not one of bow, shield, sceptre, midnight_armour
     * @throws InvalidActionException   If the player does not have sufficient items
     *                                  to craft the buildable
     */
    public final DungeonResponse build(String buildable) throws InvalidActionException {
        Player player = getPlayer();
        Buildable item = EntityFactory.getBuildable(buildable);
        player.craft(this, item);
        return getDungeonResponse();
    }

    /**
     * Interacts with a mercenary (where the character bribes the mercenary) or a
     * zombie spawner, where the character destroys the spawner.
     *
     * @param entityId of entity to interact with
     * @return DungeonResponse of current game
     * @throws IllegalArgumentException if entityId does not refer to a valid entity ID
     * @throws InvalidActionException   if the player is not cardinally adjacent to
     *                                  the given entity; if the player does not have
     *                                  any gold/sun stones and attempts to bribe or
     *                                  mind-control a mercenary; if the player does
     *                                  not have a weapon and attempts to destroy a spawner
     */
    public final DungeonResponse interact(String entityId)
        throws IllegalArgumentException, InvalidActionException {
        if (!entities.stream().map(Entity::getId).collect(Collectors.toList()).contains(entityId)) {
            throw new IllegalArgumentException(entityId);
        }
        MovingEntity player = getPlayer();
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

    /**
     * Get the tick for the current game
     *
     * @return tick
     */
    public int getTick() {
        return tick;
    }

    /**
     * Get the tick rate of the current game mode
     *
     * @return tick rate
     */
    public int getTickRate() {
        return mode.tickRate();
    }

    /**
     * Get the swamp tile at a given Position
     *
     * @param position Position of a swamp tile
     *
     * @return SwampTile at the given position
     */
    public SwampTile getSwampTile(Position position) {
        return entities
            .stream()
            .filter(e -> e.getPosition().equals(position) && e instanceof SwampTile)
            .map(e -> (SwampTile) e)
            .findFirst()
            .orElse(null);
    }

    /**
     * Checks whether the player has reached the TimeTravellingPortal yet
     *
     * @return true if player has reached the portal, false otherwise
     */
    public final boolean playerReachedTTPortal() {
        Entity player = getPlayer();
        if (player == null) return false;
        Position position = player.getPosition();

        return (
            entities
                .stream()
                .filter(e -> e instanceof TimeTravellingPortal && e.getPosition().equals(position))
                .count() >
            0
        );
    }

    /**
     * Checks whether the player has a time turner
     *
     * @return true if player has a time turner, false otherwise
     */
    public final boolean playerHasTimeTurner() {
        Player player = getPlayer();
        if (player == null) return false;

        return player.findInventoryItem("time_turner") != null;
    }
}
