package dungeonmania.movings;

import dungeonmania.response.models.DungeonResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(value = Lifecycle.PER_CLASS)
public class MercenaryTest {

    static final String CHARACTER_TYPE = "player";
    static final String DUNGEON_NAME = "advanced";
    static final String GAME_MODE = "peaceful";

    @Test
    public void testSpawnPosition() {
        // mercenaries spawn at the entry location periodically with at least one enemy
        // https://edstem.org/au/courses/7065/discussion/656701
        // although note that mercenaries can pre-exist in the world
        // and after this they periodically spawn
    }

    @Test
    public void testDoesNotSpawnWithNoEnemies() {
        // mercenaries only spawn in dungeons with at least one enemy
    }

    @Test
    public void testSimpleHostility() {
        // mercenaries constantly move towards the character
    }

    @Test
    public void testMercenaryStopIfCannotMoveCloserToCharacter() {
        // e.g. blocked by wall etc.
    }

    @Test
    public void testBribedMercenaryMovement() {}

    @Test
    public void testCannotMoveThroughExit() {}

    @Test
    public void testCannotMoveThroughClosedDoor() {}
}
