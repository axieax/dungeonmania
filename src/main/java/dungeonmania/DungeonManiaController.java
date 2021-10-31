package dungeonmania;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.Game;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DungeonManiaController {
    private List<Game> games = new ArrayList <Game> ();
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
        String path = "/dungeons/" + dungeonName + ".java";
        try {
            String content = FileLoader.loadResourceFile(path);
            Game newGame  = new Game (gameMode, content);
            games.add (newGame);
            currentGame = newGame;
            return newGame.getDungeonResponse();
        } catch (IOException e) {
            return null;
        }
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
        return currentGame.saveGame(name);
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
        String path = "/savedGames" + name;
        try {
            String content = new String(Files.readAllBytes(Path.of(getClass().getResource(path).toURI())));
            Game newGame  = new Game (content);
            games.add (newGame);
            currentGame = newGame;
            return newGame.getDungeonResponse();        
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException();
        } catch (IOException a) {
            return null;
        }
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