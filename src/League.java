import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class League {
    private String name;
    private List<Team> teams;
    private List<Match> matches;
    private List<Referee> referees;
    private Random random_generator;
    private boolean is_schedule_generated = false;
    private double goal_chance = 0.02;
    private double yellow_card_chance = 0.01;
    private double substitution_base_chance = 0.005;
    private int last_simulated_round = 0;

    public int getLastSimulatedRound() { return last_simulated_round; }
    public void setLastSimulatedRound(int round) { this.last_simulated_round = round; }

    public League(String name) {
        this.name = name;
        this.teams = new ArrayList<>();
        this.matches = new ArrayList<>();
        this.referees = new ArrayList<>();
        this.random_generator = new Random();
    }

    public void addTeam(Team team) {
        if (!teams.contains(team)) {
            teams.add(team);
        }
    }

    public void addReferee(Referee referee) {
        if (!referees.contains(referee)) {
            referees.add(referee);
        }
    }

    public boolean isScheduleGenerated() {
        return is_schedule_generated;
    }

    public void setScheduleGenerated(boolean status) {
        this.is_schedule_generated = status;
    }

    public String getName() { return name; }
    public List<Team> getTeams() { return teams; }
    public List<Match> getMatches() { return matches; }
    public List<Referee> getReferees() { return referees; }
    public Random getRandomGenerator() { return random_generator; }

    public double getGoalChance() { return goal_chance; }
    public void setGoalChance(double val) { this.goal_chance = val; }
    public double getYellowCardChance() { return yellow_card_chance; }
    public void setYellowCardChance(double val) { this.yellow_card_chance = val; }
    public double getSubstitutionBaseChance() { return substitution_base_chance; }
    public void setSubstitutionBaseChance(double val) { this.substitution_base_chance = val; }
}