public class Referee extends Person {
    private int matchesOfficiated;
    private int yellowCardsGiven;
    private int redCardsGiven;

    public Referee(String lastName, String firstName, int age, String nationality) {
        super(lastName, firstName, age, nationality);

        this.matchesOfficiated = 0;
        this.yellowCardsGiven = 0;
        this.redCardsGiven = 0;
    }

    public void addMatch() {
        this.matchesOfficiated++;
    }

    public void addYellowCard() {
        this.yellowCardsGiven++;
    }

    public void addRedCard() {
        this.redCardsGiven++;
    }

    public int getMatchesOfficiated() { return matchesOfficiated; }
    public int getYellowCardsGiven() { return yellowCardsGiven; }
    public int getRedCardsGiven() { return redCardsGiven; }
}