package dungeonmania;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.buildables.Bow;
import dungeonmania.model.entities.buildables.MidnightArmour;
import dungeonmania.model.entities.buildables.Sceptre;
import dungeonmania.model.entities.buildables.Shield;
import dungeonmania.model.entities.collectables.Arrow;
import dungeonmania.model.entities.collectables.Bomb;
import dungeonmania.model.entities.collectables.Key;
import dungeonmania.model.entities.collectables.SunStone;
import dungeonmania.model.entities.collectables.TheOneRing;
import dungeonmania.model.entities.collectables.TimeTurner;
import dungeonmania.model.entities.collectables.Treasure;
import dungeonmania.model.entities.collectables.Wood;
import dungeonmania.model.entities.collectables.equipment.Anduril;
import dungeonmania.model.entities.collectables.equipment.Armour;
import dungeonmania.model.entities.collectables.equipment.Sword;
import dungeonmania.model.entities.collectables.potion.HealthPotion;
import dungeonmania.model.entities.collectables.potion.InvincibilityPotion;
import dungeonmania.model.entities.collectables.potion.InvisibilityPotion;
import dungeonmania.model.entities.movings.Assassin;
import dungeonmania.model.entities.movings.Enemy;
import dungeonmania.model.entities.movings.Hydra;
import dungeonmania.model.entities.movings.Mercenary;
import dungeonmania.model.entities.movings.Spider;
import dungeonmania.model.entities.movings.ZombieToast;
import dungeonmania.model.entities.movings.movement.CircularMovementState;
import dungeonmania.model.entities.movings.movement.FollowPlayerMovementState;
import dungeonmania.model.entities.movings.movement.MovementState;
import dungeonmania.model.entities.movings.movement.RandomMovementState;
import dungeonmania.model.entities.movings.movement.RunMovementState;
import dungeonmania.model.entities.movings.olderPlayer.OlderPlayer;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.movings.player.PlayerDefaultState;
import dungeonmania.model.entities.movings.player.PlayerInvincibleState;
import dungeonmania.model.entities.movings.player.PlayerInvisibleState;
import dungeonmania.model.entities.movings.player.PlayerState;
import dungeonmania.model.entities.statics.Boulder;
import dungeonmania.model.entities.statics.Door;
import dungeonmania.model.entities.statics.Exit;
import dungeonmania.model.entities.statics.FloorSwitch;
import dungeonmania.model.entities.statics.Portal;
import dungeonmania.model.entities.statics.SwampTile;
import dungeonmania.model.entities.statics.TimeTravellingPortal;
import dungeonmania.model.entities.statics.Wall;
import dungeonmania.model.entities.statics.ZombieToastSpawner;
import dungeonmania.model.goal.Goal;
import dungeonmania.model.mode.Hard;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Peaceful;
import dungeonmania.model.mode.Standard;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class GameLoader {

    /**
     * Creates a JSONObject for a given saved game
     *
     * @param gameId id referring to a saved game
     *
     * @return JSONObject for the saved game
     *
     * @throws IllegalArgumentException if saved game could not be found
     */
    public static final JSONObject loadSavedGame(String gameId) throws IllegalArgumentException {
        try {
            String content = new String(
                Files.readAllBytes(Paths.get("./bin/savedGames/" + gameId + ".json"))
            );
            return new JSONObject(content);
        } catch (IOException e) {
            throw new IllegalArgumentException(gameId);
        }
    }

    /**
     * Converts a Game object to a JSONObject
     *
     * @param currentGame game to be converted
     *
     * @return JSONObject for the game
     */
    public static final JSONObject gameToJSONObject(Game currentGame) {
        JSONObject currGameJSON = new JSONObject();

        // save all entities in the game
        JSONArray entities = new JSONArray();
        for (Entity entity : currentGame.getEntities()) {
            entities.put(entity.toJSON());
        }
        currGameJSON.put("entities", entities);

        // save the mode of the game and the goal of the game
        currGameJSON.put("mode", currentGame.getMode().getClass().getSimpleName().toLowerCase());
        Goal goal = currentGame.getGoal();
        if (goal != null) currGameJSON.put("goal-condition", goal.toJSON());

        // save the dungeon name of the game
        currGameJSON.put("dungeonName", currentGame.getDungeonName());
        return currGameJSON;
    }

    /**
     * Converts a JSONObject to a Game object
     *
     * @param json game to be converted
     *
     * @return Game object
     */
    public static final Game JSONObjectToGame(JSONObject json) {
        Mode mode = GameLoader.extractMode(json);
        List<Entity> entities = GameLoader.extractEntities(json, mode);
        Goal goal = GameLoader.extractGoal(json);
        String dungeonName = GameLoader.extractDungeonName(json);

        // load game and set this game as the current game
        return new Game(dungeonName, entities, goal, mode);
    }

    /**
     * Extracts the entities from a given saved game
     *
     * @param gameId id of saved game to be loaded
     * @param mode game mode
     *
     * @return list of Entity objects in the game
     */
    public static final List<Entity> extractEntities(String gameId, Mode mode) {
        JSONObject json = loadSavedGame(gameId);
        return extractEntities(json, mode);
    }

    /**
     * Extracts the entities from a given saved game
     *
     * @param jsoninfo JSONObject for saved game to be loaded
     * @param mode game mode
     *
     * @return list of Entity objects in the game
     */
    public static final List<Entity> extractEntities(JSONObject jsonInfo, Mode mode) {
        // Extract JSON
        JSONArray entitiesInfo = jsonInfo.getJSONArray("entities");

        // Extract entities
        List<Entity> entities = new ArrayList<>();

        Entity playerEntity = null;

        for (int i = 0; i < entitiesInfo.length(); i++) {
            JSONObject entityInfo = entitiesInfo.getJSONObject(i);
            if (entityInfo.getString("type").startsWith("player")) {
                playerEntity = extractEntity(entityInfo, null, mode);
                entities.add(playerEntity);
            }
        }

        for (int i = 0; i < entitiesInfo.length(); i++) {
            JSONObject entityInfo = entitiesInfo.getJSONObject(i);
            if (entityInfo.getString("type").startsWith("player")) continue;
            Entity entity = extractEntity(entityInfo, (Player) playerEntity, mode);
            if (entity != null) entities.add(entity);
        }
        return entities;
    }

    /**
     * Maps a JSONObject for an entity to its Entity object
     *
     * @param entityInfo entity to extract
     * @param player game character
     * @param mode game mode
     *
     * @return Entity object for given entity
     */
    public static final Entity extractEntity(
        JSONObject entityInfo,
        Player currentPlayer,
        Mode mode
    ) {
        // Extract / generate basic parameters
        Position position = new Position(entityInfo.getInt("x"), entityInfo.getInt("y"));
        String type = entityInfo.getString("type");
        // Static Entities
        if (type.startsWith("wall")) {
            position = position.asLayer(0);
            return new Wall(position);
        } else if (type.startsWith("exit")) {
            position = position.asLayer(1);
            return new Exit(position);
        } else if (type.startsWith("boulder")) {
            position = position.asLayer(3);
            return new Boulder(position);
        } else if (type.startsWith("switch")) {
            position = position.asLayer(2);
            return new FloorSwitch(position);
        } else if (type.startsWith("door")) {
            int key = entityInfo.getInt("key");
            Boolean open = entityInfo.getBoolean ("open");
            position = position.asLayer(4);
            Door newDoor = new Door(position, key);
            if (open) newDoor.unlockDoor();
            return newDoor;
        } else if (type.startsWith("time_travelling_portal")) {
            position = position.asLayer(5);
            return new TimeTravellingPortal(position);
        } else if (type.startsWith("portal")) {
            String colour = entityInfo.getString("colour");
            position = position.asLayer(6);
            return new Portal(position, colour);
        } else if (type.startsWith("zombie_toast_spawner")) {
            position = position.asLayer(7);
            ZombieToastSpawner spawner = new ZombieToastSpawner(position, mode.tickRate());
            return spawner;
            // Collectable Entities
        } else if (type.startsWith("treasure")) {
            position = position.asLayer(8);
            return new Treasure(position);
        } else if (type.startsWith("key")) {
            int key = entityInfo.getInt("key");
            position = position.asLayer(9);
            return new Key(position, key);
        } else if (type.startsWith("health_potion")) {
            position = position.asLayer(10);
            return new HealthPotion(position);
        } else if (type.startsWith("invincibility_potion")) {
            position = position.asLayer(11);
            return new InvincibilityPotion(position);
        } else if (type.startsWith("invisibility_potion")) {
            position = position.asLayer(12);
            return new InvisibilityPotion(position);
        } else if (type.startsWith("wood")) {
            position = position.asLayer(13);
            return new Wood(position);
        } else if (type.startsWith("arrow")) {
            position = position.asLayer(14);
            return new Arrow(position);
        } else if (type.startsWith("bomb")) {
            position = position.asLayer(15);
            Boolean isPlaced = entityInfo.getBoolean("isPlaced");
            Bomb newBomb = new Bomb (position);
            newBomb.setPlaced(isPlaced);
            return newBomb;
        } else if (type.startsWith("sword")) {
            position = position.asLayer(16);
            Sword newSword = new Sword(position);
            int durability = entityInfo.getInt("durability");
            newSword.setDurability(durability);
            return newSword;
        } else if (type.startsWith("armour")) { //////
            position = position.asLayer(17);
            Armour newArmour = new Armour(position);
            int durability = entityInfo.getInt("durability");
            newArmour.setDurability(durability);
            return newArmour;
        } else if (type.startsWith("one_ring")) {
            position = position.asLayer(18);
            return new TheOneRing(position);
        } else if (type.startsWith("spider")) {
            position = position.asLayer(19);
            int damageMultiplier = entityInfo.getInt("damageMultiplier");
            Spider newSpider = new Spider(position, damageMultiplier, currentPlayer);
            int health = entityInfo.getInt("health");
            String movement = entityInfo.getString("movementState"); 
            int movementTick = entityInfo.getInt("movementTick");
            String direct = entityInfo.getString("movingDirection"); 
            newSpider.setHealth(health);
            newSpider.setMovementTick(movementTick);
            Direction direction = extractDirection(direct);
            newSpider.setDirection(direction);
            MovementState movementState = extractMovementState(movement, newSpider);
            newSpider.setMovementState(movementState);
            return newSpider;
        } else if (type.startsWith("zombie_toast")) {
            position = position.asLayer(20);
            int damageMultiplier = entityInfo.getInt("damageMultiplier");
            ZombieToast newZombieToast = new ZombieToast(position, damageMultiplier,currentPlayer);
            int health = entityInfo.getInt("health");
            String movement = entityInfo.getString("movementState"); 
            int movementTick = entityInfo.getInt("movementTick");
            String direct = entityInfo.getString("movingDirection"); 
            newZombieToast.setHealth(health);
            newZombieToast.setMovementTick(movementTick);
            Direction direction = extractDirection(direct);
            newZombieToast.setDirection(direction);
            MovementState movementState = extractMovementState(movement, newZombieToast);
            newZombieToast.setMovementState(movementState);
            return newZombieToast;
        } else if (type.startsWith("mercenary")) { 
            position = position.asLayer(21);
            Boolean bribed = entityInfo.getBoolean("bribed");
            Boolean mindControlled = entityInfo.getBoolean("mindControlled");
            Boolean moveTwice = entityInfo.getBoolean("moveTwice");
            int mindControlTicks = entityInfo.getInt("mindControlTicks");
            int damageMultiplier = entityInfo.getInt("damageMultiplier");
            String movement = entityInfo.getString("movementState"); 
            int movementTick = entityInfo.getInt("movementTick");
            int health = entityInfo.getInt("health");
            String direct = entityInfo.getString("movingDirection"); 
            Mercenary newMercenary = new Mercenary(position, damageMultiplier, currentPlayer);
            newMercenary.setBribed(bribed);
            newMercenary.update(currentPlayer);
            newMercenary.setMoveTwice(moveTwice);
            newMercenary.setMindControlTicks(mindControlTicks);
            newMercenary.setMovementTick(movementTick);
            newMercenary.setHealth(health);
            newMercenary.setMindControlled(mindControlled);
            Direction direction = extractDirection(direct);
            newMercenary.setDirection(direction);
            MovementState movementState = extractMovementState(movement, newMercenary);
            newMercenary.setMovementState(movementState);
            return newMercenary;
            // Collectable Entities
        } else if (type.startsWith("assassin")) { 
            position = position.asLayer(22);
            Boolean bribed = entityInfo.getBoolean("bribed");
            Boolean mindControlled = entityInfo.getBoolean("mindControlled");
            Boolean moveTwice = entityInfo.getBoolean("moveTwice");
            int mindControlTicks = entityInfo.getInt("mindControlTicks");
            int damageMultiplier = entityInfo.getInt("damageMultiplier");
            String movement = entityInfo.getString("movementState");
            int movementTick = entityInfo.getInt("movementTick");
            int health = entityInfo.getInt("health");
            String direct = entityInfo.getString("movingDirection");
            Assassin newAssassin = new Assassin(position, damageMultiplier, currentPlayer);
            newAssassin.setBribed(bribed);
            newAssassin.update(currentPlayer);
            newAssassin.setMoveTwice(moveTwice);
            newAssassin.setMindControlTicks(mindControlTicks);
            newAssassin.setMovementTick(movementTick);
            newAssassin.setHealth(health);
            newAssassin.setMindControlled(mindControlled);
            Direction direction = extractDirection(direct);
            newAssassin.setDirection(direction);
            MovementState movementState = extractMovementState(movement, newAssassin);
            newAssassin.setMovementState(movementState);
            return newAssassin;
        } else if (type.startsWith("hydra")) {
            position = position.asLayer(23);
            int damageMultiplier = entityInfo.getInt("damageMultiplier");
            String movement= entityInfo.getString("movementState");
            int movementTick = entityInfo.getInt("movementTick");
            int health = entityInfo.getInt("health");
            String direct = entityInfo.getString("movingDirection");
            Boolean preventHeadRespawn = entityInfo.getBoolean("preventHeadRespawn");
            Hydra newHydra = new Hydra(position, damageMultiplier, currentPlayer);
            newHydra.setMovementTick(movementTick);
            newHydra.setHealth(health);
            newHydra.setPreventHeadRespawn(preventHeadRespawn);
            Direction direction = extractDirection(direct);
            newHydra.setDirection(direction);
            MovementState movementState = extractMovementState(movement, newHydra);
            newHydra.setMovementState(movementState);
            return newHydra;
        } else if (type.startsWith("swamp_tile")) {
            position = position.asLayer(1);
            int movementFactor = entityInfo.getInt("movementFactor");
            SwampTile newSwampTile = new SwampTile(position, movementFactor);
            return newSwampTile;
        } else if (type.startsWith("sun_stone")) {
            position = position.asLayer(25);
            return new SunStone(position);
        } else if (type.startsWith("anduril")) {
            Anduril newAnduril = new Anduril(position);
            position = position.asLayer(26);
            int durability = entityInfo.getInt("durability");
            newAnduril.setDurability(durability);
            return newAnduril;
        } else if (type.startsWith("sceptre")) {
            Sceptre newSceptre = new Sceptre();
            return newSceptre;
        } else if (type.startsWith("midnight_armour")) {
            MidnightArmour newMidnightArmour = new MidnightArmour();
            int durability = entityInfo.getInt("durability");
            newMidnightArmour.setDurability(durability);
            return newMidnightArmour;
        } else if (type.startsWith("time_turner")) {
            position = position.asLayer(29);
            return new TimeTurner(position);
        } else if (type.startsWith("older_player")) {
            position = position.asLayer(31);   
            int health = entityInfo.getInt("health");  
            JSONArray moves = entityInfo.getJSONArray("moves");  
            List<Position> history = new ArrayList<> ();
            for (int i = 0; i < moves.length(); i++) {
                JSONObject pastPosition = moves.getJSONObject(i);
                int x = pastPosition.getInt("x");
                int y = pastPosition.getInt("y");               
                Position newPosition = new Position (x, y);
                history.add (newPosition);
            }
            OlderPlayer olderPlayer = new OlderPlayer(position, health, history); 
            return olderPlayer;     
        } else if (type.startsWith("player")) {
            position = position.asLayer(31);
            int health = entityInfo.getInt("health");
            Player player = new Player(position, health);
            JSONArray inventory = entityInfo.getJSONArray("inventory");
            for (int i = 0; i < inventory.length(); i++) {
                JSONObject item = inventory.getJSONObject(i);
                Item inventoryItem = (Item) extractEntity(item, null, mode);
                player.addInventoryItem(inventoryItem);
            }
            String state = entityInfo.getString("playerState");
            PlayerState playerState = null;
            if (state.equals("PlayerDefaultState")) {
                playerState = new PlayerDefaultState(player);
            } else if (state.equals ("PlayerInvincibleState")) {
                playerState = new PlayerInvincibleState(player);
            } else if (state.equals ("PlayerInvisibleState")) {
                playerState = new PlayerInvisibleState(player);
            }
            int ticksLeft = entityInfo.getInt("ticksLeft");
            playerState.setTicksLeft(ticksLeft);
            player.setState(playerState);
            return player;
        } else if (type.startsWith("bow")) {
            Bow newBow = new Bow();
            int durability = entityInfo.getInt("durability");
            newBow.setDurability(durability);
            return newBow;
        } else if (type.startsWith("shield")) {
            Shield newShield = new Shield();
            int durability = entityInfo.getInt("durability");
            newShield.setDurability(durability);
            return newShield;
        }
        return null;
    }

    private static final MovementState extractMovementState(String movement, Enemy enemy) {
        MovementState movementState = null;
        if (movement != null && movement.equals("CircularMovementState")) {
            movementState = new CircularMovementState(enemy);
        } else if (movement != null && movement.equals("FollowPlayerMovementState")) {
            movementState = new FollowPlayerMovementState(enemy);
        } else if (movement != null && movement.equals("RandomMovementState")) {
            movementState = new RandomMovementState(enemy);
        } else if (movement != null && movement.equals("RunMovementState")) {
            movementState = new RunMovementState(enemy);     
        }
        return movementState;
    }

    private static final Direction extractDirection (String direct) {
        Direction direction = Direction.NONE;
        if (direct.equals("UP")) direction = Direction.UP; 
        else if (direct.equals("DOWN")) direction = Direction.DOWN; 
        else if (direct.equals("RIGHT")) direction = Direction.RIGHT;
        else if (direct.equals("LEFT")) direction = Direction.LEFT; 
        return direction;       
    }

    public static final Mode extractMode(JSONObject dungeon) {
        String gameMode = dungeon.getString("mode");
        if (gameMode.equals("hard")) return new Hard();
        else if (gameMode.equals("standard")) return new Standard();
        else if (gameMode.equals("peaceful")) return new Peaceful();
        return null;
    }

    /**
     * Extracts the name of the dungeon from a saved game
     *
     * @param savedGame JSONObject for saved game
     *
     * @return name of the dungeon
     */
    public static final String extractDungeonName(JSONObject savedGame) {
        return savedGame.getString("dungeonName");
    }

    /**
     * Extracts the goal from
     *
     * @param savedGame game to be extracted from
     *
     * @return Goal of the game
     */
    public static final Goal extractGoal(JSONObject savedGame) {
        return EntityFactory.extractGoal(savedGame);
    }
}
