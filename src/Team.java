import java.util.Set;
import java.util.TreeSet;

public class Team implements Comparable<Team> {

    private String name;
    private int foundationYear;
    private double budget;

    private int points;
    private int goalsScored;
    private int concededGoals;

    private int wins;
    private int draws;
    private int losses;

    private Set<Player> squad;
    private Player[] startingSix;
    private Manager manager;
    private Stadium stadium;

    public Team(String name, int foundationYear, double budget) {
        this.name = name;
        this.foundationYear = foundationYear;
        this.budget = budget;

        this.points = 0;
        this.goalsScored = 0;
        this.concededGoals = 0;

        this.squad = new TreeSet<>();
        this.startingSix = new Player[6];
    }

    public void addPlayerToStartingSix(Player player) {
        if (!this.squad.contains(player)) {
            throw new IllegalArgumentException("Error: " + player.getLastName() + " is not in the squad of team " + this.name);
        }

        for (int i = 0; i < this.startingSix.length; i++) {
            if (this.startingSix[i] == null) {
                this.startingSix[i] = player;
                return;
            }
        }
        throw new IllegalStateException("Error: The starting six of team " + this.name + " is already full ");
    }

    public void addPlayerToStartingSix(Player player, int position) {
        if (!this.squad.contains(player)) {
            throw new IllegalArgumentException("Error: " + player.getLastName() + " is not in the squad of team " + this.name );
        }

        if (position < 0 || position >= 6) {
            throw new IllegalArgumentException("Error: Invalid position (" + position + ")! It must be between 0 and 5");
        }
        this.startingSix[position] = player;
    }

    public boolean isStartingSixFull() {
        for (int i = 0; i < this.startingSix.length; i++) {
            if (this.startingSix[i] == null) {
                return false;
            }
        }
        return true;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }
    public void addPlayer(Player player) {
        this.squad.add(player);
    }

    public void setStadium(Stadium stadium) {
        this.stadium = stadium;
    }
    public Stadium getStadium() { return stadium; }

    public void addWin() { wins++; }
    public void addDraw() { draws++; }
    public void addLose() { losses++; }

    public void addPoints(int pointsToAdd) { this.points += pointsToAdd; }

    public void updateGoals(int scored, int conceded) {
        this.goalsScored += scored;
        this.concededGoals += conceded;
    }

    public String getName() { return name; }
    public int getPoints() { return points; }
    public Set<Player> getSquad() { return squad; }

    public int getGoalDifference() {
        return goalsScored - concededGoals;
    }

    @Override
    public int compareTo(Team otherTeam) {
        if (this.points != otherTeam.points) {
            return Integer.compare(otherTeam.points, this.points);
        }

        if (this.getGoalDifference() != otherTeam.getGoalDifference()) {
            return Integer.compare(otherTeam.getGoalDifference(), this.getGoalDifference());
        }

        return this.name.compareTo(otherTeam.name);
    }

    public Player[] getStartingSix() {
        return this.startingSix;
    }
    public int getGoalsScored() { return this.goalsScored; }
    public int getGoalsConceded() { return this.concededGoals; }
}