package dungeonmania.statics;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import dungeonmania.DungeonManiaController;
import dungeonmania.model.Game;
import dungeonmania.model.entities.statics.Exit;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class ExitTest {

    /**
     * Test whether the entity instance has been created with the correct positions
     */
    @Test
    public void instanceTest() {
        Game game = new Game(3, 3);
        Exit exit = new Exit(new Position(1, 1));
        game.addEntity(exit);
        
        assertTrue(new Position(1, 1).equals(game.getEntity(exit.getId()).getPosition()));
    }

    /**
     * Test Exit when player completes the goal.
     */
    @Test
    public void exitCompleteGoal() {
        fail();
    }

    /**
     * Test Exit when player has not completes the goal.
     */
    @Test
    public void exitIncompleteGoal() {
        fail();
    }
}
