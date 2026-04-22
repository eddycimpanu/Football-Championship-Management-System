public class Substitution extends MatchEvent {
    private Player playerOut;
    private Player playerIn;

    public Substitution(int minute, Player playerOut, Player playerIn) {
        super(minute);

        if (playerOut == null || playerIn == null) {
            throw new IllegalArgumentException("Error: Substitution requires both players (in and out)!");
        }

        if (playerOut.equals(playerIn)) {
            throw new IllegalArgumentException("Error: A player cannot substitute themselves!");
        }

        this.playerOut = playerOut;
        this.playerIn = playerIn;
    }

    public Player getPlayerOut() {
        return playerOut;
    }

    public Player getPlayerIn() {
        return playerIn;
    }
}