package dungeonmania.model.goal;

import dungeonmania.model.Game;
import org.json.JSONObject;

public abstract class GoalLeaf implements Goal {

    private String operator;

    public GoalLeaf(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }

    /**
     * Returns number of conditions remaining for GoalLeaf to be satisfied
     *
     * @param game Game state
     *
     * @return number of conditions remaining (0 if satisfied)
     */
    abstract int numRemaining(Game game);

    public boolean isComplete(Game game) {
        return numRemaining(game) == 0;
    }

    @Override
    public String toString(Game game) {
        int quantity = numRemaining(game);
        return (quantity != 0) ? String.format(":%s(%d)", getOperator(), quantity) : "";
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("goal", getOperator());
        return json;
    }
}
