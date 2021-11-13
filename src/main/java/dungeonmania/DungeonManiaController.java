package dungeonmania;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.GameWrapper;
import dungeonmania.model.Maze;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.goal.Goal;
import dungeonmania.model.mode.Hard;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Peaceful;
import dungeonmania.model.mode.Standard;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.json.JSONObject;

public class DungeonManiaController {

    private List<GameWrapper> games = new ArrayList<>();
    private GameWrapper currentGame;

    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        // return "zh_CN";
        return "en_US";
    }

    public List<String> getGameModes() {
        return Arrays.asList("standard", "peaceful", "hard");
    }

    /**
     * Get the mode object given the mode string
     * @param mode what mode it is
     * @return mode object
     * @throws IllegalArgumentException if the mode does not exist
     */
    private Mode getMode(String mode) throws IllegalArgumentException {
        mode = mode.toLowerCase();
        if (!getGameModes().contains(mode)) throw new IllegalArgumentException(mode);
        switch (mode) {
            case "peaceful":
                return new Peaceful();
            case "standard":
                return new Standard();
            case "hard":
                return new Hard();
            default:
                return null;
        }
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
        // checks
        if (!dungeons().contains(dungeonName)) throw new IllegalArgumentException();

        // get game mode
        Mode mode = getMode(gameMode);
        // get game entities
        List<Entity> entities = EntityFactory.extractEntities(dungeonName, mode);
        // get game goal
        Goal goal = EntityFactory.extractGoal(dungeonName);

        // create new game
        GameWrapper newGame = new GameWrapper(dungeonName, entities, goal, mode);
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
        if (name.length() == 0) throw new IllegalArgumentException(name);

        // convert game to JSONObject
        JSONObject currGame = GameLoader.gameToJSONObject(currentGame.getActiveGame());

        // get a pretty printed json string
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement je = JsonParser.parseString(currGame.toString());
        String prettyString = gson.toJson(je);
        try {
            // write the json string to a file
            String directoryPath = "./bin/savedGames";
            File pathAsFile = new File(directoryPath);
            if (!pathAsFile.exists()) {
                pathAsFile.mkdir();
            }

            String path = "./bin/savedGames/" + name + ".json";
            FileWriter myFileWriter = new FileWriter(path, false);
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
        if (name.length() == 0) throw new IllegalArgumentException(name);
        if (!allGames().contains(name)) throw new IllegalArgumentException(name);

        // load dungeon
        JSONObject dungeon = GameLoader.loadSavedDungeon(name);
        GameWrapper newGame = new GameWrapper(GameLoader.JSONObjectToGame(dungeon));

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
        try {
            // the name of files in the savedGames directory
            String directory = "./bin/savedGames/";
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
     * @param itemUsedId
     * @param movementDirection
     * @return
     * @throws IllegalArgumentException If itemUsed is not a bomb, health_potion
     *                                  invincibility_potion, or an
     *                                  invisibility_potion
     * @throws InvalidActionException   If itemUsed is not in the player's inventory
     */
    public DungeonResponse tick(String itemUsedId, Direction movementDirection)
        throws IllegalArgumentException, InvalidActionException {
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
        // check for valid buildable
        List<String> buildables = Arrays.asList("bow", "shield", "sceptre", "midnight_armour");
        if (!buildables.contains(buildable)) throw new IllegalArgumentException(buildable);

        return currentGame.build(buildable);
    }

    /**
     * Generate a random maze dungeon
     * @param xStart x coordinate of start position
     * @param yStart y coordinate of starte position
     * @param xEnd x coordinate of end position
     * @param yEnd y coordinate of end position
     * @param gameMode mode of game
     * @return
     * @throws IllegalArgumentException if mode does not exist
     */
    public DungeonResponse generateDungeon(
        int xStart,
        int yStart,
        int xEnd,
        int yEnd,
        String gameMode
    )
        throws IllegalArgumentException {
        // get game mode
        Mode mode = getMode(gameMode);

        // get height and width for maze
        Random random = new Random();
        int widthMinBound = ((xStart > xEnd) ? xStart : xEnd) + 2;
        int heightMinBound = ((yStart > yEnd) ? yStart : yEnd) + 2;
        int width = random.nextInt(5) + widthMinBound;
        int height = random.nextInt(5) + heightMinBound;

        // make a new maze
        Maze maze = new Maze(width, height, new Position(xStart, yStart), new Position(xEnd, yEnd));
        JSONObject mazeJSON = maze.toJSON();
        List<Entity> entities = EntityFactory.extractEntities(mazeJSON, mode);
        Goal goal = EntityFactory.extractGoal(mazeJSON);

        // generate the new game
        GameWrapper newGame = new GameWrapper("Dungeon Builder", entities, goal, mode);
        games.add(newGame);
        currentGame = newGame;
        return newGame.getDungeonResponse();
    }

    public DungeonResponse rewind(int ticks) throws IllegalArgumentException {
        if (ticks <= 0) throw new IllegalArgumentException(String.valueOf(ticks));
        return currentGame.rewind(ticks);
    }
}
