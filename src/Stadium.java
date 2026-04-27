public class Stadium {

    private String name;
    private String city;
    private int capacity;

    private int matchesHosted;

    public Stadium(String name, String city, int capacity) {
        this.name = name;
        this.city = city;

        this.capacity = capacity;

        this.matchesHosted = 0;
    }
    public void hostMatch() {
        this.matchesHosted++;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getName() { return name; }
    public String getCity() { return city; }
    public int getCapacity() { return capacity; }
    public int getMatchesHosted() { return matchesHosted; }
}