public class Manager extends Person{

    private int yellowCards;
    private int redCards;

    public Manager(String lastName, String firstName, int age, String nationality)
    {
        super(lastName, firstName, age, nationality);

        this.yellowCards = 0;
        this.redCards = 0;
    }
    public void addYellowCard() {
        this.yellowCards++;
    }

    public void addRedCard() {
        this.redCards++;
    }

    public int getYellowCards() { return yellowCards; }
    public int getRedCards() { return redCards; }
}
