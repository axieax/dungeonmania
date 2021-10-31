package dungeonmania;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.buildables.Bow;
import dungeonmania.model.entities.buildables.BuildableEquipment;
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
import dungeonmania.model.goal.AndComposite;
import dungeonmania.model.goal.CollectTreasure;
import dungeonmania.model.goal.DestroyEnemies;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.goal.Goal;
import dungeonmania.model.goal.GoalComposite;
import dungeonmania.model.goal.OrComposite;
import dungeonmania.model.goal.ToggleSwitch;
import dungeonmania.model.mode.Mode;
import dungeonmania.util.Position;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONObject;

public class EntityFactory {

    private static final List<String> entityLayers = Arrays.asList("wall");

    private static final JSONObject loadDungeon(String dungeonName)
        throws IllegalArgumentException {
        try {
            String content = new String(
                Files.readAllBytes(
                    Paths.get("src/main/resources/dungeons/" + dungeonName + ".json")
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
        JSONObject json = loadDungeon(dungeonName);
        JSONArray entitiesInfo = json.getJSONArray("entities");
    
        // extract entities
        List<Entity> entities = new ArrayList<>();
        
        Entity playerEntity = null;
        for (int i = 0; i < entitiesInfo.length(); ++i) {
            JSONObject entityInfo = entitiesInfo.getJSONObject(i);
            if (entityInfo.getString ("type").startsWith("player")) {
                playerEntity = extractEntity(entityInfo, null, mode);
                entities.add(playerEntity);
            }
        }

        for (int i = 0; i < entitiesInfo.length(); ++i) {
            JSONObject entityInfo = entitiesInfo.getJSONObject(i);
            entities.add(extractEntity(entityInfo, playerEntity, mode));
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
            position = position.asLayer(0);
            return new Wall(position);
        } else if (type.startsWith("exit")) {
            position = position.asLayer(1);
            return new Exit(position);
        } else if (type.startsWith("switch")) {
            position = position.asLayer(2);
            return new FloorSwitch(position);
        } else if (type.startsWith("boulder")) {
            position = position.asLayer(3);
            return new Boulder(position);
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
            return new ZombieToastSpawner(position);
            // Moving Entities
        } else if (type.startsWith("spider")) {
            return new Spider(position);
        } else if (type.startsWith("zombie_toast")) {
            Player currentPlayer = (Player) playerEntity;
            return new ZombieToast(position, currentPlayer);
        } else if (type.startsWith("mercenary")) {
            return new Mercenary(position);
            // Collectable Entities
        } else if (type.startsWith("treasure")) {
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
            return new Sword(position);
        } else if (type.startsWith("armour")) {
            position = position.asLayer(16);
            return new Armour(position);
        } else if (type.startsWith("one_ring")) {
            position = position.asLayer(17);
            return new TheOneRing(position);
        } else if (type.startsWith("player")) {
            return new Player(position);
        }
        return null;
    }

    private Map<String, BuildableEquipment> buildableMap() {
        // dummy objects
        Map<String, BuildableEquipment> map = new HashMap<>();
        map.put("bow", new Bow());
        map.put("shield", new Shield());
        return map;
    }

    public List<BuildableEquipment> allBuildables() {
        return new ArrayList<>(buildableMap().values());
    }

    public BuildableEquipment getBuildable(String buildable) {
        BuildableEquipment item = buildableMap().get(buildable);
        return item.clone();
    }

    public static final Goal extractGoal(String dungeonName) throws IllegalArgumentException {
        JSONObject json = loadDungeon(dungeonName);
        return (json.has("goal-condition"))
            ? extractGoal(json.getJSONObject("goal-condition"))
            : null;
    }

    public static final Goal extractGoal(JSONObject json) {
        switch (json.getString("goal")) {
            case "enemies":
                return new DestroyEnemies();
            case "treasure":
                return new CollectTreasure();
            case "exit":
                return new ExitCondition();
            case "boulders":
                return new ToggleSwitch();
            case "AND":
                GoalComposite and = new AndComposite();
                JSONArray andSubgoals = json.getJSONArray("subgoals");
                for (int i = 0; i < andSubgoals.length(); ++i) {
                    JSONObject subgoal = andSubgoals.getJSONObject(i);
                    and.addGoal(extractGoal(subgoal));
                }
                return and;
            case "OR":
                GoalComposite or = new OrComposite();
                JSONArray orSubgoals = json.getJSONArray("subgoals");
                for (int i = 0; i < orSubgoals.length(); ++i) {
                    JSONObject subgoal = orSubgoals.getJSONObject(i);
                    or.addGoal(extractGoal(subgoal));
                }
                return or;
            default:
                return null;
        }
    }
}
