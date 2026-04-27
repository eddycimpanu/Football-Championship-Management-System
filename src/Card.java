public class Card extends MatchEvent {
    private Person person;
    private String color;

    public Card(int minute, Person person, String color) {
        super(minute);

        if (person == null) {
            throw new IllegalArgumentException("Error: A card must be given to Player or Manager");
        }
        if (!color.equalsIgnoreCase("Yellow") && !color.equalsIgnoreCase("Red")) {
            throw new IllegalArgumentException("Error: Card color must be 'Yellow' or 'Red'!");
        }

        this.person = person;
        this.color = color;
    }

    public Person getPerson() { return person; }
    public String getColor() { return color; }
}