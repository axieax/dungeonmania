package dungeonmania;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.goal.Goal;
import dungeonmania.model.mode.Hard;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Peaceful;
import dungeonmania.model.mode.Standard;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class DungeonManiaController {

    private List<Game> games = new ArrayList<>();
    private Game currentGame;

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
     * "Standard", "Peaceful" or "Hard".
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

        // determine game mode
        Mode mode = null;
        if (gameMode.equals("Hard")) mode = new Hard(); 
        else if (gameMode.equals("Standard")) mode = new Standard(); 
        else if (gameMode.equals("Peaceful")) mode = new Peaceful();
        
        // get game entities
        List<Entity> entities = EntityFactory.extractEntities (dungeonName, mode);
        // get goal
        Goal goal = EntityFactory.extractGoal (dungeonName);
        
        // create new game
        Game newGame = new Game(dungeonName, entities, goal, mode);
        games.add(newGame);
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

        // save all entities in the game
        JSONArray entities = new JSONArray();
        for (Entity entity : currentGame.getEntities()) {
            entities.put(entity.toJSON());
        }
        currGame.put("entities", entities);

        // save the mode of the game and the goal of the game
        currGame.put("mode", currentGame.getMode().getClass().getSimpleName());
        Goal goal = currentGame.getGoal();
        if (goal != null) currGame.put("goal-condition", goal.toJSON());

        // save the dungeon name of the game
        currGame.put("dungeonName", currentGame.getDungeonName());

        // get a pretty printed json string
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement je = JsonParser.parseString(currGame.toString());
        String prettyString = gson.toJson(je);
        try { // write the json string to a file
            String path = "./src/main/java/dungeonmania/savedGames/" + name + ".json";
            FileWriter myFileWriter = new FileWriter (path, false);
            myFileWriter.write(prettyString);
            myFileWriter.close();            
        } catch (IOException e) {
            return null;
        }

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
        // name must not have length = 0 and not be contained already in saved games
        if (name.length() == 0) throw new IllegalArgumentException();
        if (!allGames().contains(name)) throw new IllegalArgumentException();

        // extract details of the game
        Mode mode = GameLoader.extractMode(name);
        List<Entity> entities = GameLoader.extractEntities(name, mode);
        Goal goal = GameLoader.extractGoal(name);
        String dungeonName = GameLoader.extractDungeonName(name);

        // load game and set this game as the current game
        Game newGame = new Game(dungeonName, entities, goal, mode);
        games.add(newGame);
        currentGame = newGame;
        return newGame.getDungeonResponse();
    }

    /**
     * Returns a list containing all the saved games that are currently stored.
     *
     * @return
     */
    public List<String> allGames() {
        try { // the name of files in a directory
            String directory = "./src/main/java/dungeonmania/savedGames/";
            return FileLoader.listFileNamesInDirectoryOutsideOfResources(directory);
        } catch (IOException e) {
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
     * @throws IllegalArgumentException If itemUsed is not a bomb, health_potion
     *                                  invincibility_potion, or an
     *                                  invisibility_potion
     * @throws InvalidActionException   If itemUsed is not in the player's inventory
     */
    public DungeonResponse tick(String itemUsedId, Direction movementDirection)
        throws IllegalArgumentException, InvalidActionException {
        if (itemUsedId != null && itemUsedId.length() == 0) throw new IllegalArgumentException (itemUsedId);
        return currentGame.tick(itemUsedId, movementDirection);
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
        return currentGame.build(buildable);
    }
}
