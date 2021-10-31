package dungeonmania;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.goal.Goal;
import dungeonmania.model.mode.Hard;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Peaceful;
import dungeonmania.model.mode.Standard;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

public class DungeonManiaController {
    private List<Game> games = new ArrayList <>();
    private Game currentGame;

    public DungeonManiaController() {}

    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    public List<String> getGameModes() {
        return Arrays.asList("Standard", "Peaceful", "Hard");
    }

    /**
     * /dungeons
     *
     * Done for you.
     */
    public static List<String> dungeons() {
        try {
            return FileLoader.listFileNamesInResourceDirectory("/dungeons");
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Creates a new game, where dungeonName is the name of the dungeon map
     * (corresponding to a JSON file stored in the model) and gameMode is one of
     * "standard", "peaceful" or "hard".
     *
     * @param dungeonName
     * @param gameMode
     * @return
     * @throws IllegalArgumentException If gameMode is not a valid game mode. If
     *                                  dungeonName is not a dungeon that exists
     */
    public DungeonResponse newGame(String dungeonName, String gameMode)
        throws IllegalArgumentException {
        if (!dungeons().contains(dungeonName)) throw new IllegalArgumentException();
        if (!getGameModes().contains(gameMode)) throw new IllegalArgumentException();

        Mode mode = null;
        if (gameMode.equals ("Hard")) mode  = new Hard ();
        else if (gameMode.equals ("Standard")) mode = new Standard ();
        else if (gameMode.equals ("Peaceful")) mode = new Peaceful ();

        List<Entity> entities = EntityFactory.extractEntities(dungeonName, mode);
        Goal goal = EntityFactory.extractGoal (dungeonName);

        Game newGame = new Game (dungeonName, entities, goal, mode);
        games.add (newGame);
        currentGame = newGame;
        return newGame.getDungeonResponse();
    }

    /**
     * Saves the current game state with the given name.
     *
     * @param name
     * @return
     * @throws IllegalArgumentException
     */
    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        if (name.length() == 0) throw new IllegalArgumentException("Invalid name");

        JSONObject currGame = new JSONObject();

        JSONArray entities = new JSONArray();
        for (Entity entity: currentGame.getEntities()) {
            entities.put (entity.toJSON());
        }
        currGame.put("entities", entities);
        currGame.put("mode", currentGame.getMode().getClass().getSimpleName());
        currGame.put ("goal-condition", currentGame.getGoal().toJSON());
        currGame.put ("dungeonName", currentGame.getDungeonName());
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement je = JsonParser.parseString(currGame.toString());
        String prettyString = gson.toJson(je);

        String path = "./src/main/java/dungeonmania/savedGames/" + name + ".json";
        FileWriter myFileWriter = new FileWriter (path, false);
        myFileWriter.write(prettyString);
        myFileWriter.close();

        return currentGame.getDungeonResponse();
    }

    /**
     * Loads the game with the given id.
     *
     * @param name
     * @return
     * @throws IllegalArgumentException - If id is not a valid game id
     */
    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        if (!allGames().contains(name)) throw new IllegalArgumentException();
        String path = "./src/main/java/dungeonmania/savedGames/" + name + ".json";
        Mode mode = GameLoader.extractMode(name);
        List<Entity> entities = GameLoader.extractEntities(name);
        Goal goal = GameLoader.extractGoal(name);
        String dungeonName = GameLoader.extractDungeonName(name);

        Game newGame = new Game(dungeonName, entities, goal, mode);
        games.add (newGame);
        currentGame = newGame;
        return newGame.getDungeonResponse();        
    }

    /**
     * Returns a list containing all the saved games that are currently stored.
     *
     * @return
     */
    public List<String> allGames() {
        try { // adapted from given code
            String directory = "/savedGames";
            Path root = Paths.get(getClass().getResource(directory).toURI());
            return Files.walk(root).filter(Files::isRegularFile).map(x -> {
                String nameAndExt = x.toFile().getName();
                int extIndex = nameAndExt.lastIndexOf('.');
                return nameAndExt.substring(0, extIndex > -1 ? extIndex : nameAndExt.length());
            }).collect(Collectors.toList());
        } catch (IOException e) {
            return new ArrayList<>();
        } catch (URISyntaxException a) {
            return new ArrayList<>();
        } 
    }

    /**
     * Ticks the game state. When a tick occurs: - The player moves in the specified
     * direction one square - All enemies move respectively - Any items which are
     * used are 'actioned' and interact with the relevant entity
     *
     * @param itemUsed
     * @param movementDirection
     * @return
     * @throws IllegalArgumentException If itemUsed is not a bomb,
     *                                  invincibility_potion, or an
     *                                  invisibility_potion
     * @throws InvalidActionException   If itemUsed is not in the player's inventory
     */
    public DungeonResponse tick(String itemUsed, Direction movementDirection)
        throws IllegalArgumentException, InvalidActionException {
            if (itemUsed!= null && !(itemUsed.equals("bomb")|| 
                itemUsed.equals("invincibility_potion")|| 
                itemUsed.equals("invisibility_potion")|| 
                itemUsed.equals("health_potion"))) {
                throw new IllegalArgumentException ();
            }
        
        return currentGame.tick(itemUsed, movementDirection);
    }

    /**
     * Interacts with a mercenary (where the character bribes the mercenary) or a
     * zombie spawner, where the character destroys the spawner.
     *
     * @param entityId
     * @return
     * @throws IllegalArgumentException If entityId is not a valid entity ID
     * @throws InvalidActionException   If the player is not cardinally adjacent to
     *                                  the given entity If the player does not have
     *                                  any gold and attempts to bribe a mercenary
     *                                  If the player does not have a weapon and
     *                                  attempts to destroy a spawner
     */
    public DungeonResponse interact(String entityId)
        throws IllegalArgumentException, InvalidActionException {
        return currentGame.interact(entityId);
    }

    /**
     * Builds the given entity, where buildable is one of bow and shield.
     *
     * @param buildable
     * @return
     * @throws IllegalArgumentException If buildable is not one of bow, shield
     * @throws InvalidActionException   If the player does not have sufficient items
     *                                  to craft the buildable
     */
    public DungeonResponse build(String buildable)
        throws IllegalArgumentException, InvalidActionException {
        if (!(buildable.equals("bow") || buildable.equals("shield"))) {
            throw new IllegalArgumentException();
        }
        return build(buildable);
    }
}