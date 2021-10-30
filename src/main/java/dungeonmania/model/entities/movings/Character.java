package dungeonmania.model.entities.movings;

import java.util.List;

import dungeonmania.model.Game;
import dungeonmania.model.entities.AttackEquipment;
import dungeonmania.model.entities.DefenceEquipment;
import dungeonmania.model.entities.Equipment;
import dungeonmania.model.entities.Item;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;

public interface Character {
    public void battle(Game game, MovingEntity opponent);

    public void collect(Item item);

    public void build(String itemId);

    public Item getInventoryItem(String itemId);

    public void removeInventoryItem(String itemId);
    
    public List<Equipment> getEquipmentList();

    public List<AttackEquipment> getAttackEquipmentList();
    
    public List<DefenceEquipment> getDefenceEquipmentList();
    
    public boolean canCraft(String prefix);

    public List<ItemResponse> getInventoryResponses();

    public int getCurrentAttackDamage();

    public int applyDefenceToOpponentAttack(int opponentAttackDamag);

    public void addAlly(MovingEntity ally);

    public List<MovingEntity> getAllies();
    
    public void move(Game game, Direction direction);

}
