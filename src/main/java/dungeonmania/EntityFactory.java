package dungeonmania;

import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.buildables.Bow;
import dungeonmania.model.entities.buildables.Buildable;
import dungeonmania.model.entities.buildables.MidnightArmour;
import dungeonmania.model.entities.buildables.Sceptre;
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
import dungeonmania.model.entities.movings.Spider;
import dungeonmania.model.entities.movings.ZombieToast;
import dungeonmania.model.entities.movings.player.Player;
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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class EntityFactory {

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

    public static final List<Entity> extractEntities (String dungeonName, Mode mode) {
        JSONObject json = loadDungeon(dungeonName); 
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

        if (playerEntity != null && playerEntity instanceof Player) {
            for (int i = 0; i < entitiesInfo.length(); i++) {
                JSONObject entityInfo = entitiesInfo.getJSONObject(i);
                if (entityInfo.getString("type").startsWith("player")) continue;
                entities.add(extractEntity(entityInfo, (Player) playerEntity, mode));
            }
        }
        return entities;
    }

    public static final Entity extractEntity(JSONObject entityInfo, Player player, Mode mode) {
        // Extract / generate basic parameters
        Position position = new Position(entityInfo.getInt("x"), entityInfo.getInt("y"));
        String type = entityInfo.getString("type");
        if (type.startsWith("wall")) {
            // Static Entities
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
        } else if (type.startsWith("time_travelling_portal")) {
            position = position.asLayer(5);
            return new TimeTravellingPortal(position);
        } else if (type.startsWith("portal")) {
            String colour = entityInfo.getString("colour");
            position = position.asLayer(6);
            return new Portal(position, colour);
        }  else if (type.startsWith("zombie_toast_spawner")) {
            position = position.asLayer(7);
            return new ZombieToastSpawner(position, mode.tickRate());
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
            return new Bomb(position);
        } else if (type.startsWith("sword")) {
            position = position.asLayer(16);
            return new Sword(position);
        } else if (type.startsWith("armour")) {
            position = position.asLayer(17);
            return new Armour(position);
        } else if (type.startsWith("one_ring")) {
            position = position.asLayer(18);
            return new TheOneRing(position);
            // Moving Entities
        } else if (type.startsWith("spider")) {
            position = position.asLayer(18);
            return new Spider(position, mode.damageMultiplier(), player);
        } else if (type.startsWith("zombie_toast")) {
            position = position.asLayer(20);
            return new ZombieToast(position, mode.damageMultiplier(), player);
        } else if (type.startsWith("mercenary")) {
            position = position.asLayer(21);
            return new Mercenary(position, mode.damageMultiplier(), player);
        } else if (type.startsWith("assassin")) { 
            position = position.asLayer(22);
            return new Assassin(position);
        }else if (type.startsWith("hydra")) {
            position = position.asLayer(23);
            return new Hydra(position);
        }else if (type.startsWith("swamp_tile")) {
            position = position.asLayer(24);
            return new SwampTile(position);
        }else if (type.startsWith("sun_stone")) {
            position = position.asLayer(25);
            return new SunStone(position);
        } else if (type.startsWith("anduril")) {
            position = position.asLayer(26);
            return new Anduril(position);
        } else if (type.startsWith("sceptre")) {
            position = position.asLayer(27);
            return new Sceptre(position);
        } else if (type.startsWith("midnight_armour")) {
            position = position.asLayer(28);
            return new MidnightArmour(position);
        } else if (type.startsWith("time_turner")) {
            position = position.asLayer(29);
            return new TimeTurner(position);
        } else if (type.startsWith("older_player")) {
            position = position.asLayer(30);
            return new OlderPlayer(position);
        } else if (type.startsWith("player")) {
            position = position.asLayer(21);
            return new Player(position, mode.initialHealth());
        }
        return null;
    }

    private static Map<String, Buildable> buildableMap() {
        // dummy objects
        Map<String, Buildable> map = new HashMap<>();
        map.put("bow", new Bow());
        map.put("shield", new Shield());
        map.put("sceptre", new Sceptre());
        map.put("midnight_armour", new MidnightArmour());
        return map;
    }

    public static List<Buildable> allBuildables() {
        return new ArrayList<>(buildableMap().values());
    }

    public static Buildable getBuildable(String buildable) {
        Buildable item = buildableMap().get(buildable);
        return item.clone();
    }
    public static final Goal extractGoal (String dungeonName) throws IllegalArgumentException {
        return extractGoal(loadDungeon(dungeonName));
    }
    public static final Goal extractGoal(JSONObject dungeon) throws IllegalArgumentException {
        return (dungeon.has("goal-condition"))
            ? doExtractGoal(dungeon.getJSONObject("goal-condition"))
            : null;
    }

    public static final Goal doExtractGoal(JSONObject json) {
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
                for (int i = 0; i < andSubgoals.length(); i++) {
                    JSONObject subgoal = andSubgoals.getJSONObject(i);
                    and.addGoal(doExtractGoal(subgoal));
                }
                return and;
            case "OR":
                GoalComposite or = new OrComposite();
                JSONArray orSubgoals = json.getJSONArray("subgoals");
                for (int i = 0; i < orSubgoals.length(); i++) {
                    JSONObject subgoal = orSubgoals.getJSONObject(i);
                    or.addGoal(doExtractGoal(subgoal));
                }
                return or;
            default:
                return null;
        }
    }
}
