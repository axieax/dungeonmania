package dungeonmania.model.goal;

import org.json.JSONObject;

public abstract class GoalLeaf implements Goal {

    private int quantity;

    public String getOperator() {
        return getClass().getSimpleName();
    }

    @Override
    public String toString() {
        String modifier = (quantity != 0) ? String.format("(%d)", quantity) : "";
        return ":" + getOperator() + modifier;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("goal", getOperator());
        return json;
    }
}
