package dungeonmania.collectables;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import dungeonmania.model.Game;
import dungeonmania.model.entities.collectables.Arrow;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Standard;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class ArrowTest {

    /**
     * Test whether the entity instance has been created with the correct positions.
     */
    @Test
    public void instanceTest() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        Arrow arrow = new Arrow(new Position(1, 1));
        game.addEntity(arrow);

        assertTrue(new Position(1, 1).equals(game.getEntity(arrow.getId()).getPosition()));
    }

    /**
     * Test whether the collectable entity can be picked up by the Player.
     */
    @Test
    public void collectTest() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        Arrow arrow = new Arrow(new Position(1, 1));
        game.addEntity(arrow);

        Player player = new Player(new Position(0, 1));
        player.move(game, Direction.RIGHT);

        assertTrue(new Position(1, 1).equals(player.getPosition()));        

        assertTrue(game.getEntity(arrow.getId()) == null);
        assertTrue(player.getInventoryItem(arrow.getId()).equals(arrow));
    }
}