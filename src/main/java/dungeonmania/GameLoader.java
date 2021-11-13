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
import dungeonmania.model.entities.movings.Hydra;
import dungeonmania.model.entities.movings.Mercenary;
import dungeonmania.model.entities.movings.Spider;
import dungeonmania.model.entities.movings.ZombieToast;
import dungeonmania.model.entities.movings.player.Player;
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

    public static final JSONObject loadSavedDungeon(String dungeonName)
        throws IllegalArgumentException {
        try {
            String content = new String(
                Files.readAllBytes(Paths.get("./bin/savedGames/" + dungeonName + ".json"))
            );
            return new JSONObject(content);
        } catch (IOException e) {
            throw new IllegalArgumentException(dungeonName);
        }
    }

    public static final Game JSONObjectToGame(JSONObject json) {
        Mode mode = GameLoader.extractMode(json);
        List<Entity> entities = GameLoader.extractEntities(json, mode);
        Goal goal = GameLoader.extractGoal(json);
        String dungeonName = GameLoader.extractDungeonName(json);

        // load game and set this game as the current game
        return new Game(dungeonName, entities, goal, mode);
    }

    public static final List<Entity> extractEntities(String dungeonName, Mode mode) {
        JSONObject json = loadSavedDungeon(dungeonName);
        return extractEntities(json, mode);
    }

    public static final List<Entity> extractEntities(JSONObject jsonInfo, Mode mode)
        throws IllegalArgumentException {
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
            entities.add(extractEntity(entityInfo, (Player) playerEntity, mode));
        }

        return entities;
    }

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
            position = position.asLayer(4);
            return new Door(position, key);
        } else if (type.startsWith("time_travelling_portal")) {
            position = position.asLayer(5);
            return new TimeTravellingPortal(position);
        } else if (type.startsWith("portal")) {
            String colour = entityInfo.getString("colour");
            position = position.asLayer(6);
            return new Portal(position, colour);
        } else if (type.startsWith("zombie_toast_spawner")) {
            position = position.asLayer(7);
            return new ZombieToastSpawner(position, mode.tickRate());
            // Moving Entities
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
            return new Bomb(position);
        } else if (type.startsWith("sword")) { ///////
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
        } else if (type.startsWith("spider")) { ////
            position = position.asLayer(19);
            Spider newSpider = new Spider(position, mode.damageMultiplier(), currentPlayer);
            int health = entityInfo.getInt("health");
            newSpider.setHealth(health);
            return newSpider;
        } else if (type.startsWith("zombie_toast")) { ////
            position = position.asLayer(20);
            ZombieToast newZombieToast = new ZombieToast(
                position,
                mode.damageMultiplier(),
                currentPlayer
            );
            int health = entityInfo.getInt("health");
            newZombieToast.setHealth(health);
            return newZombieToast;
        } else if (type.startsWith("mercenary")) { ////
            position = position.asLayer(21);
            Boolean bribed = entityInfo.getBoolean("bribed");
            Boolean mindControlled = entityInfo.getBoolean("mindControlled");
            Boolean moveTwice = entityInfo.getBoolean("moveTwice");
            int mindControlTicks = entityInfo.getInt("mindControlTicks");
            int damageMultiplier = entityInfo.getInt("damageMultiplier");
            // String movementState = entityInfo.getString("movementState"); // TODO more
            int movementTick = entityInfo.getInt("movementTick");
            int health = entityInfo.getInt("health");
            String direct = entityInfo.getString("movingDirection"); // TODO
            Mercenary newMercenary = new Mercenary(
                position,
                mode.damageMultiplier(),
                currentPlayer
            );
            newMercenary.setBribed(bribed);
            newMercenary.update(currentPlayer);
            newMercenary.setMoveTwice(moveTwice);
            newMercenary.setMindControlTicks(mindControlTicks);
            newMercenary.setMovementTick(movementTick);
            newMercenary.setHealth(health);
            Direction direction = Direction.NONE;
            if (direct.equals("UP")) direction = Direction.UP; else if (
                direct.equals("DOWN")
            ) direction = Direction.DOWN; else if (direct.equals("RIGHT")) direction =
                Direction.RIGHT; else if (direct.equals("LEFT")) direction = Direction.LEFT;
            newMercenary.setDirection(direction);
            return newMercenary;
            // Collectable Entities
        } else if (type.startsWith("assassin")) { /////
            position = position.asLayer(22);
            Boolean bribed = entityInfo.getBoolean("bribed");
            Boolean mindControlled = entityInfo.getBoolean("mindControlled");
            Boolean moveTwice = entityInfo.getBoolean("moveTwice");
            int mindControlTicks = entityInfo.getInt("mindControlTicks");
            int damageMultiplier = entityInfo.getInt("damageMultiplier");
            // String movementState = entityInfo.getString("movementState"); // TODO more
            int movementTick = entityInfo.getInt("movementTick");
            int health = entityInfo.getInt("health");
            String direct = entityInfo.getString("movingDirection"); // TODO more
            Assassin newAssassin = new Assassin(position, damageMultiplier, currentPlayer);
            newAssassin.setBribed(bribed);
            newAssassin.update(currentPlayer);
            newAssassin.setMoveTwice(moveTwice);
            newAssassin.setMindControlTicks(mindControlTicks);
            newAssassin.setMovementTick(movementTick);
            newAssassin.setHealth(health);
            Direction direction = Direction.NONE;
            if (direct.equals("UP")) direction = Direction.UP; else if (
                direct.equals("DOWN")
            ) direction = Direction.DOWN; else if (direct.equals("RIGHT")) direction =
                Direction.RIGHT; else if (direct.equals("LEFT")) direction = Direction.LEFT;
            newAssassin.setDirection(direction);
            return newAssassin;
        } else if (type.startsWith("hydra")) { ////////
            position = position.asLayer(23);
            int damageMultiplier = entityInfo.getInt("damageMultiplier");
            // String movementState = entityInfo.getString("movementState"); // TODO more
            int movementTick = entityInfo.getInt("movementTick");
            int health = entityInfo.getInt("health");
            String direct = entityInfo.getString("movingDirection"); // TODO more
            Boolean preventHeadRespawn = entityInfo.getBoolean("preventHeadRespawn");
            Hydra newHydra = new Hydra(position, damageMultiplier, currentPlayer);
            newHydra.setMovementTick(movementTick);
            newHydra.setHealth(health);
            newHydra.setPreventHeadRespawn(preventHeadRespawn);
            Direction direction = Direction.NONE;
            if (direct.equals("UP")) direction = Direction.UP; else if (
                direct.equals("DOWN")
            ) direction = Direction.DOWN; else if (direct.equals("RIGHT")) direction =
                Direction.RIGHT; else if (direct.equals("LEFT")) direction = Direction.LEFT;
            newHydra.setDirection(direction);
            return newHydra;
        } else if (type.startsWith("swamp_tile")) { /////////
            position = position.asLayer(24);
            int movementFactor = entityInfo.getInt("movementFactor");
            SwampTile newSwampTile = new SwampTile(position, movementFactor);
            return newSwampTile;
        } else if (type.startsWith("sun_stone")) {
            position = position.asLayer(25);
            return new SunStone(position);
        } else if (type.startsWith("anduril")) { /////
            Anduril newAnduril = new Anduril(position);
            position = position.asLayer(26);
            int durability = entityInfo.getInt("durability");
            newAnduril.setDurability(durability);
            return newAnduril;
        } else if (type.startsWith("sceptre")) { /////////
            Sceptre newSceptre = new Sceptre();
            int durability = entityInfo.getInt("durability");
            newSceptre.setDurability(durability);
            return newSceptre;
        } else if (type.startsWith("midnight_armour")) { ///////
            MidnightArmour newMidnightArmour = new MidnightArmour();
            int durability = entityInfo.getInt("durability");
            int bonusAttackDamage = entityInfo.getInt("bonusAttackDamage");
            newMidnightArmour.setDurability(durability);
            newMidnightArmour.setAttackDamage(bonusAttackDamage);
            return newMidnightArmour;
        } else if (type.startsWith("time_turner")) {
            position = position.asLayer(29);
            return new TimeTurner(position);
        } else if (type.startsWith("older_player")) { ///////
            position = position.asLayer(30);
            // return new OlderPlayer(position); // TODO
        } else if (type.startsWith("player")) {
            position = position.asLayer(31);
            Player player = new Player(position, mode.initialHealth());
            int health = entityInfo.getInt("health");
            player.setHealth(health);
            JSONArray inventory = entityInfo.getJSONArray("inventory");
            for (int i = 0; i < inventory.length(); i++) {
                JSONObject item = inventory.getJSONObject(i);
                Item inventoryItem = (Item) extractEntity(item, null, mode);
                player.addInventoryItem(inventoryItem);
            }
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

    public static final Mode extractMode(JSONObject dungeon) {
        String gameMode = dungeon.getString("mode");
        if (gameMode.equals("hard")) return new Hard(); else if (
            gameMode.equals("standard")
        ) return new Standard(); else if (gameMode.equals("peaceful")) return new Peaceful();
        return null;
    }

    public static final String extractDungeonName(JSONObject dungeon) {
        return dungeon.getString("dungeonName");
    }

    public static final Goal extractGoal(JSONObject savedDungeon) throws IllegalArgumentException {
        return (savedDungeon.has("goal-condition"))
            ? EntityFactory.doExtractGoal(savedDungeon.getJSONObject("goal-condition"))
            : null;
    }
}
