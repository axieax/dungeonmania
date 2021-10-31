package dungeonmania;

import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.buildables.Bow;
import dungeonmania.model.entities.buildables.Shield;
import dungeonmania.model.entities.collectables.Arrow;
import dungeonmania.model.entities.collectables.Bomb;
import dungeonmania.model.entities.collectables.Key;
import dungeonmania.model.entities.collectables.TheOneRing;
import dungeonmania.model.entities.collectables.Treasure;
import dungeonmania.model.entities.collectables.Wood;
import dungeonmania.model.entities.collectables.equipment.Armour;
import dungeonmania.model.entities.collectables.equipment.Sword;
import dungeonmania.model.entities.collectables.potion.HealthPotion;
import dungeonmania.model.entities.collectables.potion.InvincibilityPotion;
import dungeonmania.model.entities.collectables.potion.InvisibilityPotion;
import dungeonmania.model.entities.movings.Mercenary;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.model.entities.movings.Spider;
import dungeonmania.model.entities.movings.ZombieToast;
import dungeonmania.model.entities.statics.Boulder;
import dungeonmania.model.entities.statics.Door;
import dungeonmania.model.entities.statics.Exit;
import dungeonmania.model.entities.statics.FloorSwitch;
import dungeonmania.model.entities.statics.Portal;
import dungeonmania.model.entities.statics.Wall;
import dungeonmania.model.entities.statics.ZombieToastSpawner;
import dungeonmania.model.goal.Goal;
import dungeonmania.model.mode.Hard;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Peaceful;
import dungeonmania.model.mode.Standard;
import dungeonmania.util.Position;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class GameLoader {
    private static final JSONObject loadSavedDungeon(String dungeonName)
        throws IllegalArgumentException {
        try {
            String content = new String(
                Files.readAllBytes(
                    Paths.get("./src/main/java/dungeonmania/savedGames/" + dungeonName + ".json")
                )
            );
            return new JSONObject(content);
        } catch (IOException e) {
            throw new IllegalArgumentException(dungeonName);
        }
    }  


    public static final List<Entity> extractEntities(String dungeonName, Mode mode)
        throws IllegalArgumentException {
        // extract JSON
        JSONObject json = loadSavedDungeon(dungeonName);
        JSONArray entitiesInfo = json.getJSONArray("entities");

        // extract entities
        List<Entity> entities = new ArrayList<>();

        Entity playerEntity = null;
        for (int i = 0; i < entitiesInfo.length(); ++i) {
            JSONObject entityInfo = entitiesInfo.getJSONObject(i);
            if (entityInfo.getString ("type").startsWith("player")) {
                playerEntity = extractEntity(entityInfo, playerEntity, mode);
                entities.add(playerEntity);
            }
        }

        for (int i = 0; i < entitiesInfo.length(); ++i) {
            JSONObject entityInfo = entitiesInfo.getJSONObject(i);
            if (entityInfo.getString ("type").startsWith("player")) continue;
            entities.add(extractEntity(entityInfo, playerEntity, mode));
        }

        return entities;
    }

    public static final Entity extractEntity(JSONObject entityInfo, Entity playerEntity, Mode mode) {
        // Extract / generate basic parameters
        Position position = new Position(entityInfo.getInt("x"), entityInfo.getInt("y"));
        String type = entityInfo.getString("type");
        // Static Entities
        if (type.startsWith("wall")) {
            return new Wall(position);
        } else if (type.startsWith("exit")) {
            return new Exit(position);
        } else if (type.startsWith("boulder")) {
            return new Boulder(position);
        } else if (type.startsWith("switch")) {
            return new FloorSwitch(position);
        } else if (type.startsWith("door")) {
            int key = entityInfo.getInt("key");
            return new Door(position, key);
        } else if (type.startsWith("portal")) {
            String colour = entityInfo.getString("colour");
            return new Portal(position, colour);
        } else if (type.startsWith("zombie_toast_spawner")) {
            return new ZombieToastSpawner(position);
            // Moving Entities
        } else if (type.startsWith("spider")) {
            Spider newSpider = new Spider (position);
            int health = entityInfo.getInt("health");
            newSpider.setHealth (health);
            return newSpider;
        } else if (type.startsWith("zombie_toast")) {
            Player currentPlayer = (Player) playerEntity;
            ZombieToast newZombieToast = new ZombieToast(position, currentPlayer);
            int health = entityInfo.getInt("health");
            newZombieToast.setHealth(health);
            return newZombieToast;
        } else if (type.startsWith("mercenary")) {
            Mercenary newMercenary = new Mercenary(position);
            int health = entityInfo.getInt("health");
            newMercenary.setHealth(health);
            return newMercenary;
            // Collectable Entities
        } else if (type.startsWith("treasure")) {
            return new Treasure(position);
        } else if (type.startsWith("key")) {
            int key = entityInfo.getInt("key");
            return new Key(position, key);
        } else if (type.startsWith("health_potion")) {
            return new HealthPotion(position);
        } else if (type.startsWith("invincibility_potion")) {
            return new InvincibilityPotion(position);
        } else if (type.startsWith("invisibility_potion")) {
            return new InvisibilityPotion(position);
        } else if (type.startsWith("wood")) {
            return new Wood(position);
        } else if (type.startsWith("arrow")) {
            return new Arrow(position);
        } else if (type.startsWith("bomb")) {
            return new Bomb(position);
        } else if (type.startsWith("sword")) {
            Sword newSword = new Sword(position);
            int durability = entityInfo.getInt("durability");
            newSword.setDurability(durability);
            return newSword;
        } else if (type.startsWith("armour")) {
            Armour newArmour = new Armour(position);
            int durability = entityInfo.getInt("durability");
            newArmour.setDurability(durability);
            return newArmour;
        } else if (type.startsWith("one_ring")) {
            return new TheOneRing(position);
        } else if (type.startsWith("player")) {
            Player player = new Player (position);
            int health = entityInfo.getInt("health");
            player.setHealth(health);
            return player;
        } else if (type.startsWith("bow")) {
            Bow newBow = new Bow();
            int durability = entityInfo.getInt("durability");
            newBow.setDurability(durability);
            return newBow;
        } else if (type.startsWith ("shield")) {
            Shield newShield = new Shield();
            int durability = entityInfo.getInt("durability");
            newShield.setDurability(durability);
            return newShield;
        }
        return null;
    }

    public static final Mode extractMode (String name) {
        JSONObject json = loadSavedDungeon(name);
        String gameMode = json.getString ("mode");
        if (gameMode.equals ("Hard")) return new Hard ();
        else if (gameMode.equals ("Standard")) return new Standard ();
        else if (gameMode.equals ("Peaceful")) return new Peaceful ();
        return null;
    }

    public static final String extractDungeonName (String name) {
        JSONObject json = loadSavedDungeon(name);
        return json.getString ("dungeonName");
    }

    public static final Goal extractGoal(String dungeonName) throws IllegalArgumentException {
        JSONObject json = loadSavedDungeon(dungeonName);
        return EntityFactory.extractGoal(json.getJSONObject("goal-condition"));
    }
}
