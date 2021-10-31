package dungeonmania.collectables;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import dungeonmania.model.Game;
import dungeonmania.model.entities.collectables.Bomb;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class BombTest {

    /**
     * Test whether the entity instance has been created with the correct positions
     */
    @Test
    public void instanceTest() {
        Game game = new Game(3, 3);
        Bomb bomb = new Bomb(new Position(1, 1));
        game.addEntity(bomb);
        
        assertTrue(new Position(1, 1).equals(game.getEntity(bomb.getId()).getPosition()));
    }

    /**
     * Test whether the collectable entity can be picked up by the Player.
     */
    @Test
    public void collectTest() {
        Game game = new Game(3, 3);
        Bomb bomb = new Bomb(new Position(1, 1));
        game.addEntity(bomb);

        Player player = new Player(new Position(0, 1));
        player.move(game, Direction.RIGHT);

        assertTrue(new Position(1, 1).equals(player.getPosition()));        

        assertTrue(game.getEntity(bomb.getId()) == null);
        assertTrue(player.getInventoryItem(bomb.getId()).equals(bomb));
    }

    /**
     * Test if Bomb cannot destroy entities if it is not cardinally adjacent to a switch
     */
    @Test
    public void notDestroyTest() {
        fail();
    }

    /**
     * Test if Bomb destroys entities in the radius
     */
    @Test
    public void destroyRadius() {
        fail();
    }
}