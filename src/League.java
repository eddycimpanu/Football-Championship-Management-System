import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class League {
    private String name;
    private List<Team> teams;
    private List<Match> matches;
    private List<Referee> referees;
    private Random randomGenerator;

    public League(String name) {
        this.name = name;
        this.teams = new ArrayList<>();
        this.matches = new ArrayList<>();
        this.referees = new ArrayList<>();
        this.randomGenerator = new Random();
    }

    public void addTeam(Team team) {
        if (teams.contains(team)) {
            throw new IllegalArgumentException("Error: Team already exists in the league!");
        }
        teams.add(team);
    }

    public void addReferee(Referee referee) {
        if (referees.contains(referee)) {
            throw new IllegalArgumentException("Error: Referee already exists in the league!");
        }
        referees.add(referee);
    }

    public void generateSchedule() {
        if (teams.size() < 2 || teams.size() % 2 != 0) {
            throw new IllegalStateException("Error: Need an even number of teams (at least 2) to generate a schedule!");
        }
        if (referees.isEmpty()) {
            throw new IllegalStateException("Error: Cannot generate schedule without any referees in the league!");
        }

        int numTeams = teams.size();
        int numRounds = numTeams - 1;
        int matchesPerRound = numTeams / 2;

        List<Team> tempTeams = new ArrayList<>(teams);

        for (int round = 0; round < numRounds; round++) {
            for (int j = 0; j < matchesPerRound; j++) {
                Team home = tempTeams.get(j);
                Team away = tempTeams.get(numTeams - 1 - j);

                Referee randomRef = referees.get(randomGenerator.nextInt(referees.size()));

                Match match = new Match(home, away, randomRef);
                match.setMatchday(round + 1);

                matches.add(match);
            }

            Team last = tempTeams.remove(tempTeams.size() - 1);
            tempTeams.add(1, last);
        }
    }

    public String getName() { return name; }
    public List<Team> getTeams() { return teams; }
    public List<Match> getMatches() { return matches; }
    public List<Referee> getReferees() { return referees; }
}