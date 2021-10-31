package dungeonmania.model.goal;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.statics.Exit;
import dungeonmania.util.Position;

public class ExitCondition extends GoalLeaf {

    public ExitCondition() {
        super("exit");
    }

    public int numRemaining(Game game) {
        // number of exits without a player on them
        Entity character = game.getCharacter();
        Position characterPosition = character.getPosition();
        return (int) game
            .getEntities()
            .stream()
            .filter(e ->
                (e != character) &&
                (e instanceof Exit) &&
                !(e.getPosition().equals(characterPosition))
            )
            .count();
    }
}
