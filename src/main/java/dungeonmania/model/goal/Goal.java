package dungeonmania.model.goal;

import dungeonmania.model.Game;

public interface Goal {
    /**
     * Returns the operator (name) of a Goal
     *
     * @return Goal operator
     */
    public String getOperator();

    /**
     * Checks whether a Goal has been satisfied
     *
     * @param game Game state to check
     *
     * @return true if Goal satisfied / complete, false otherwise
     */
    public boolean isComplete(Game game);

    /**
     * Gets a string representation of a Goal
     *
     * @param game Game state to check
     *
     * @return string representation of a Goal
     */
    public String toString(Game game);
}
