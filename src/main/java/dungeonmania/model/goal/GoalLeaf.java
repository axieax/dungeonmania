package dungeonmania.model.goal;

import dungeonmania.model.Game;

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
        return String.format(":%s(%d)", getOperator(), numRemaining(game));
    }
}
