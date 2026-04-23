public class Referee extends Person {
    private int matchesRefereed;
    private int yellowCardsGiven;
    private int redCardsGiven;

    public Referee(String lastName, String firstName, int age, String nationality) {
        super(lastName, firstName, age, nationality);

        this.matchesRefereed = 0;
        this.yellowCardsGiven = 0;
        this.redCardsGiven = 0;
    }


    public void addMatch() {
        this.matchesRefereed++;
    }

    public void giveYellowCard() {
        this.yellowCardsGiven++;
    }

    public void giveRedCard() {
        this.redCardsGiven++;
    }

    public int getMatchesRefereed() { return matchesRefereed; }
    public int getYellowCardsGiven() { return yellowCardsGiven; }
    public int getRedCardsGiven() { return redCardsGiven; }
}