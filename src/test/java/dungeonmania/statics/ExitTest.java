package dungeonmania.statics;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.statics.Exit;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Standard;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class ExitTest {

    /**
     * Test whether the entity instance has been created with the correct positions.
     */
    @Test
    public void instanceTest() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        Exit exit = new Exit(new Position(1, 1));
        game.addEntity(exit);
                
        assertTrue(new Position(1, 1).equals(game.getEntity(exit.getId()).getPosition()));
    }

    /**
     * Test if the player can move to an Exit tile.
     */
    @Test
    public void exitMoveTo() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        Exit exit = new Exit(new Position(1, 1));
        game.addEntity(exit);
                
        Player player = new Player(new Position(0, 1));
        game.addEntity(player);

        player.move(game, Direction.RIGHT);

        assertTrue(new Position(1, 1).equals(game.getEntity(exit.getId()).getPosition()));
        assertTrue(new Position(1, 1).equals(game.getEntity(player.getId()).getPosition()));
    }
}
