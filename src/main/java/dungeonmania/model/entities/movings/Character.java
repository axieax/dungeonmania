package dungeonmania.model.entities.movings;

import java.util.List;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Equipment;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.buildables.BuildableEquipment;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;

public interface Character {
    public void battle(Game game, MovingEntity opponent);

    public void collect(Item item);

    public void craft(BuildableEquipment item);

    public Item getInventoryItem(String itemId);

    public void removeInventoryItem(String itemId);
    
    public List<Equipment> getEquipmentList();

    public List<Equipment> getAttackEquipmentList();
    
    public List<Equipment> getDefenceEquipmentList();
    
    public List<ItemResponse> getInventoryResponses();

    public int applyDefenceToOpponentAttack(int opponentAttackDamag);

    public void addAlly(MovingEntity ally);

    public List<MovingEntity> getAllies();
    
    public void move(Game game, Direction direction);

}
