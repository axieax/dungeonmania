package dungeonmania.model.entities.collectables;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.statics.Consumable;
import dungeonmania.util.Position;

public class SunStone extends Item implements Consumable {

    public SunStone(Position position) {
        super("sun_stone", position);
    }

    @Override
    public void consume(Game game, Player player) {
        // TODO Auto-generated method stub
        
    }
    
}
