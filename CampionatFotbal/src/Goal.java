public class Goal extends MatchEvent {
    private Player scorer;
    private Player assistant;

    public Goal(int minute, Player scorer, Player assistant) {
        super(minute);
        if (scorer == null) {
            throw new IllegalArgumentException("Error: A goal must have a scorer!");
        }
        this.scorer = scorer;
        this.assistant = assistant;
    }

    public Goal(int minute, Player scorer) {
        this(minute, scorer, null);
    }

    public Player getScorer() { return scorer; }
    public Player getAssistant() { return assistant; }
}