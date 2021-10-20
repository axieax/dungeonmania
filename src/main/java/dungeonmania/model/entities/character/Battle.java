package dungeonmania.model.entities.character;

import dungeonmania.model.entities.character.player.Player;
import dungeonmania.model.entities.character.enemy.Enemy;

public interface Battle {

    public void battle(Player player, Enemy enemy);

}
