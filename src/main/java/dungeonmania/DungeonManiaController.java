package dungeonmania;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DungeonManiaController {

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
        /* Stub from FRONTEND.md */

        List<EntityResponse> entities = new ArrayList<>();

        // let's generate some walls
        // all maps have to be fully surrounded by walls
        // so the map we'll genreate is going to be
        /**
         *
         * WWWWW W P W W D W WWWWW
         *
         * where P is the player, D is a door, and W is some walls
         */

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 4; y++) {
                if (x == 0 || y == 0 || x == 4 || y == 3) {
                    // only generate borders
                    entities.add(
                        new EntityResponse(
                            "entity-" + x + " - " + y,
                            "wall",
                            new Position(x, y, 2),
                            false
                        )
                    );
                }
            }
        }

        // now to add the player and coin
        entities.add(new EntityResponse("entity-player", "player", new Position(2, 1, 2), false));
        entities.add(new EntityResponse("entity-door", "door", new Position(2, 2, 2), false));

        return new DungeonResponse(
            "some-random-id",
            dungeonName,
            entities,
            Arrays.asList(new ItemResponse("item-1", "bow"), new ItemResponse("item-2", "sword")),
            new ArrayList<>(),
            ""
        );
    }

    /**
     * Saves the current game state with the given name.
     *
     * @param name
     * @return
     * @throws IllegalArgumentException
     */
    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        return null;
    }

    /**
     * Loads the game with the given id.
     *
     * @param name
     * @return
     * @throws IllegalArgumentException - If id is not a valid game id
     */
    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        return null;
    }

    /**
     * Returns a list containing all the saved games that are currently stored.
     *
     * @return
     */
    public List<String> allGames() {
        return new ArrayList<>();
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
        return null;
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
        return null;
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
        return null;
    }
}
