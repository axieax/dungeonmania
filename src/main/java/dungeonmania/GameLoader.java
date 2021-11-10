package dungeonmania;

import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.Item;
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
                    Paths.get("./bin/savedGames/" + dungeonName + ".json")
                )
            );
            return new JSONObject(content);
        } catch (IOException e) {
            throw new IllegalArgumentException(dungeonName);
        }
    }  


    public static final List<Entity> extractEntities(String dungeonName, Mode mode)
        throws IllegalArgumentException {
        // Extract JSON
        JSONObject json = loadSavedDungeon(dungeonName);
        JSONArray entitiesInfo = json.getJSONArray("entities");

        // Extract entities
        List<Entity> entities = new ArrayList<>();

        Entity playerEntity = null;

        for (int i = 0; i < entitiesInfo.length(); i++) {
            JSONObject entityInfo = entitiesInfo.getJSONObject(i);
            if (entityInfo.getString ("type").startsWith("player")) {
                playerEntity = extractEntity(entityInfo, null, mode);
                entities.add(playerEntity);
            }
        }

        for (int i = 0; i < entitiesInfo.length(); i++) {
            JSONObject entityInfo = entitiesInfo.getJSONObject(i);
            if (entityInfo.getString ("type").startsWith("player")) continue;
            entities.add(extractEntity(entityInfo, (Player) playerEntity, mode));
        }

        return entities;
    }

    public static final Entity extractEntity(JSONObject entityInfo, Player currentPlayer, Mode mode) {
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
        } else if (type.startsWith("portal")) {
            String colour = entityInfo.getString("colour");
            position = position.asLayer(5);
            return new Portal(position, colour);
        } else if (type.startsWith("zombie_toast_spawner")) {
            position = position.asLayer(6);
            return new ZombieToastSpawner(position, mode.tickRate());
            // Moving Entities
        }  else if (type.startsWith("treasure")) {
            position = position.asLayer(7);
            return new Treasure(position);
        } else if (type.startsWith("key")) {
            int key = entityInfo.getInt("key");
            position = position.asLayer(8);
            return new Key(position, key);
        } else if (type.startsWith("health_potion")) {
            position = position.asLayer(9);
            return new HealthPotion(position);
        } else if (type.startsWith("invincibility_potion")) {
            position = position.asLayer(10);
            return new InvincibilityPotion(position);
        } else if (type.startsWith("invisibility_potion")) {
            position = position.asLayer(11);
            return new InvisibilityPotion(position);
        } else if (type.startsWith("wood")) {
            position = position.asLayer(12);
            return new Wood(position);
        } else if (type.startsWith("arrow")) {
            position = position.asLayer(13);
            return new Arrow(position);
        } else if (type.startsWith("bomb")) {
            position = position.asLayer(14);
            return new Bomb(position);
        } else if (type.startsWith("sword")) {
            position = position.asLayer(15);
            Sword newSword = new Sword(position);
            int durability = entityInfo.getInt("durability");
            newSword.setDurability(durability);
            return newSword;
        } else if (type.startsWith("armour")) {
            position = position.asLayer(16);
            Armour newArmour = new Armour(position);
            int durability = entityInfo.getInt("durability");
            newArmour.setDurability(durability);
            return newArmour;
        } else if (type.startsWith("one_ring")) {
            position = position.asLayer(17);
            return new TheOneRing(position);
        } else if (type.startsWith("player")) {
            position = position.asLayer(0);
            Player player = new Player (position);
            int health = entityInfo.getInt("health");
            player.setHealth(health);
            JSONArray inventory = entityInfo.getJSONArray("inventory");
            for (int i = 0; i < inventory.length(); i++) {
                JSONObject item = inventory.getJSONObject(i);
                Item inventoryItem = (Item) extractEntity (item, currentPlayer, mode);
                currentPlayer.addInventoryItem (inventoryItem);
            }
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
        } else if (type.startsWith("spider")) {
            position = position.asLayer(18);
            Spider newSpider = new Spider(position);
            int health = entityInfo.getInt("health");
            newSpider.setHealth (health);
            return newSpider;
        } else if (type.startsWith("zombie_toast")) {
            position = position.asLayer(19);
            ZombieToast newZombieToast = new ZombieToast(position, mode.damageMultiplier(), currentPlayer); 
            int health = entityInfo.getInt("health");
            newZombieToast.setHealth(health);
            return newZombieToast;
        } else if (type.startsWith("mercenary")) {
            position = position.asLayer(20);
            Mercenary newMercenary = new Mercenary(position, mode.damageMultiplier(), currentPlayer);
            int health = entityInfo.getInt("health");
            newMercenary.setHealth(health);
            return newMercenary;
            // Collectable Entities
        }
        return null;
    }

    public static final Mode extractMode (String name) {
        JSONObject json = loadSavedDungeon(name);
        String gameMode = json.getString ("mode");
        if (gameMode.equals("Hard")) return new Hard();
        else if (gameMode.equals("Standard")) return new Standard();
        else if (gameMode.equals("Peaceful")) return new Peaceful();
        return null;
    }

    public static final String extractDungeonName (String name) {
        JSONObject json = loadSavedDungeon(name);
        return json.getString ("dungeonName");
    }

    public static final Goal extractGoal(String dungeonName) throws IllegalArgumentException {
        JSONObject json = loadSavedDungeon(dungeonName);
        return (json.has("goal-condition")) ? EntityFactory.extractGoal(json.getJSONObject("goal-condition")):null;
    }
}
