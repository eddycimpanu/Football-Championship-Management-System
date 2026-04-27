public class Player extends Person {

    private String position;
    private int kitNumber;

    private int matchesPlayed;
    private int minutesPlayed;
    private int goals;
    private int concededGoals;
    private int assists;
    private int yellowCards;
    private int redCards;
    private int cleanSheets;

    public Player(String lastName, String firstName, int age, String nationality, String position, int kitNumber) {
        super(lastName, firstName, age, nationality);
        this.position = position;
        this.kitNumber = kitNumber;

        this.matchesPlayed = 0;
        this.minutesPlayed = 0;
        this.goals = 0;
        this.concededGoals = 0;
        this.assists = 0;
        this.yellowCards = 0;
        this.redCards = 0;
        this.cleanSheets = 0;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean setKitNumber(int kitNumber) {
        if (kitNumber < 100 && kitNumber > 0) {
            this.kitNumber = kitNumber;
            return true;
        } else {
            return false;
        }
    }

    public void addGoal() {
        this.goals++;
    }
    public void addConcededGoal() {
        this.concededGoals++;
    }
    public void addAssist() {
        this.assists++;
    }
    public void addYellowCard() {
        this.yellowCards++;
    }
    public void addRedCard() {
        this.redCards++;
    }
    public void addCleanSheet() {
        this.cleanSheets++;
    }

    public String getPosition() { return position; }
    public int getKitNumber() { return kitNumber; }
    public int getMatchesPlayed() { return matchesPlayed; }
    public int getMinutesPlayed() { return minutesPlayed; }
    public int getGoals() { return goals; }
    public int getConcededGoals() { return concededGoals; }
    public int getAssists() { return assists; }
    public int getYellowCards() { return yellowCards; }
    public int getRedCards() { return redCards; }
    public int getCleanSheets() { return cleanSheets; }
}