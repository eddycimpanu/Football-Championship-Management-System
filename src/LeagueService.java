import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LeagueService {
    private League league;

    public LeagueService(League league) {
        this.league = league;
    }

    public void generateSchedule() {
        if (league.isScheduleGenerated()) {
            throw new IllegalStateException("Matches already generated!");
        }

        List<Team> teams = league.getTeams();
        List<Referee> referees = league.getReferees();

        if (teams.size() < 2 || teams.size() % 2 != 0) {
            throw new IllegalStateException("Error: Need an even number of teams!");
        }

        if (referees.isEmpty()) {
            throw new IllegalStateException("Error: No referees in the league!");
        }

        int n = teams.size();
        int rounds = n - 1;
        List<Team> pool = new ArrayList<>(teams);

        for (int r = 0; r < rounds; r++) {
            for (int i = 0; i < n / 2; i++) {
                Team home = pool.get(i);
                Team away = pool.get(n - 1 - i);
                Referee ref = referees.get(league.getRandomGenerator().nextInt(referees.size()));

                Match m = new Match(home, away, ref);
                m.setMatchday(r + 1);
                league.getMatches().add(m);
            }
            Team last = pool.remove(n - 1);
            pool.add(1, last);
        }

        pool = new ArrayList<>(teams);

        for (int r = 0; r < rounds; r++) {
            for (int i = 0; i < n / 2; i++) {
                Team home = pool.get(n - 1 - i);
                Team away = pool.get(i);
                Referee ref = referees.get(league.getRandomGenerator().nextInt(referees.size()));

                Match m = new Match(home, away, ref);
                m.setMatchday(r + 1 + rounds);
                league.getMatches().add(m);
            }
            Team last = pool.remove(n - 1);
            pool.add(1, last);
        }

        league.setScheduleGenerated(true);
    }

    public void showMatchDetails(String homeName, String awayName) {
        Match foundMatch = null;

        for (Match m : league.getMatches()) {
            if (m.getHomeTeam().getName().equalsIgnoreCase(homeName) &&
                    m.getAwayTeam().getName().equalsIgnoreCase(awayName)) {
                foundMatch = m;
                break;
            }
        }

        if (foundMatch == null) {
            System.out.println("[ERROR] Match not found!");
            return;
        }

        if (!foundMatch.isFinished()) {
            System.out.println("[INFO] This match hasn't been played yet.");
        } else {
            foundMatch.displayEvents();
        }
    }

    public void simulateNextRound() {
        int nextRound = league.getLastSimulatedRound() + 1;

        List<Match> roundMatches = new ArrayList<>();
        for (Match m : league.getMatches()) {
            if (m.getMatchday() == nextRound && !m.isFinished()) {
                roundMatches.add(m);
            }
        }

        if (roundMatches.isEmpty()) {
            System.out.println("[INFO] No more matches to simulate or schedule not generated.");
            return;
        }

        System.out.println("\n--- SIMULATING ROUND " + nextRound + " ---");
        Random dice = new Random();

        for (Match match : roundMatches) {
            for (int min = 1; min <= 90; min++) {
                if (dice.nextDouble() < league.getGoalChance()) {
                    Team t = dice.nextBoolean() ? match.getHomeTeam() : match.getAwayTeam();
                    Player[] starters = (t == match.getHomeTeam()) ? match.getHomeStarters() : match.getAwayStarters();
                    if (starters.length > 0) {
                        Player p = starters[dice.nextInt(starters.length)];
                        if (p != null) match.addGoal(min, p);
                    }
                }

                if (dice.nextDouble() < league.getYellowCardChance()) {
                    Team t = dice.nextBoolean() ? match.getHomeTeam() : match.getAwayTeam();
                    Player[] starters = (t == match.getHomeTeam()) ? match.getHomeStarters() : match.getAwayStarters();
                    if (starters.length > 0) {
                        Player p = starters[dice.nextInt(starters.length)];
                        if (p != null) match.addCard(min, p, "Yellow");
                    }
                }

                double dynamicSubChance = league.getSubstitutionBaseChance() + ((double) min / 90.0) * 0.02;
                if (dice.nextDouble() < dynamicSubChance) {
                    simulateRandomSubstitution(match, min, dice);
                }
            }
            match.finishMatch();
            System.out.println(match.getHomeTeam().getName() + " " + match.getHomeScore() + " - " +
                    match.getAwayScore() + " " + match.getAwayTeam().getName());
        }

        league.setLastSimulatedRound(nextRound);
    }

    private void simulateRandomSubstitution(Match match, int min, Random dice) {
        try {
            Team t = dice.nextBoolean() ? match.getHomeTeam() : match.getAwayTeam();
            Player[] starters = (t == match.getHomeTeam()) ? match.getHomeStarters() : match.getAwayStarters();
            List<Player> squadList = new ArrayList<>(t.getSquad());

            if (starters.length > 0 && squadList.size() > 6) {
                Player pOut = starters[dice.nextInt(starters.length)];
                Player pIn = squadList.get(dice.nextInt(squadList.size()));

                boolean isAlreadyIn = false;
                for(Player s : starters) if(s == pIn) isAlreadyIn = true;

                if (pOut != null && pIn != null && !isAlreadyIn) {
                    match.addSubstitution(min, pOut, pIn);
                }
            }
        } catch (Exception ignored) {}
    }

    public void autoFillAllTeams() {
        boolean any_incomplete = false;
        for (Team team : league.getTeams()) {
            if (!team.fillStartingSix()) any_incomplete = true;
            System.out.println("[OK] Checked " + team.getName());
        }
        if (any_incomplete) System.out.println("\n[WARNING] Some teams are short on players!");
        else System.out.println("\n[SUCCESS] All lineups are full!");
    }

    public void printMatches() {
        if (league.getMatches().isEmpty()) {
            System.out.println("No schedule found.");
            return;
        }
        int current_day = 0;
        for (Match m : league.getMatches()) {
            if (m.getMatchday() != current_day) {
                current_day = m.getMatchday();
                System.out.println("\n--- ROUND " + current_day + " ---");
            }
            System.out.println(m.getHomeTeam().getName() + " vs " + m.getAwayTeam().getName());
        }
    }

    public void printStandings() {
        List<Team> teams = new ArrayList<>(league.getTeams());

        teams.sort((t1, t2) -> {
            if (t2.getPoints() != t1.getPoints()) {
                return Integer.compare(t2.getPoints(), t1.getPoints());
            }
            int gd1 = t1.getGoalsScored() - t1.getGoalsConceded();
            int gd2 = t2.getGoalsScored() - t2.getGoalsConceded();
            return Integer.compare(gd2, gd1);
        });

        System.out.println("\n--- LEAGUE STANDINGS ---");
        for (int i = 0; i < teams.size(); i++) {
            Team t = teams.get(i);
            System.out.println((i + 1) + ". " + t.getName() + " ( " +
                    t.getPoints() + " points | " +
                    t.getGoalsScored() + " goals scored | " +
                    t.getGoalsConceded() + " goals conceded )");
        }
    }

    public static void populateLeague(League league) {
        league.addReferee(new Referee("Kovacs", "Istvan", 41, "Romanian"));
        league.addReferee(new Referee("Coltescu", "Sebastian", 46, "Romanian"));

        Team ajax = new Team("Ajax", 1900, 6000000.0);
        ajax.setManager(new Manager("Michels", "Rinus", 70, "Dutch"));
        ajax.setStadium(new Stadium("Johan Cruyff Arena", "Amsterdam", 55000));
        ajax.addPlayer(new Player("Cruyff", "Johan", 70, "Dutch", "FW", 14));
        ajax.addPlayer(new Player("van Basten", "Marco", 59, "Dutch", "ST", 9));
        ajax.addPlayer(new Player("Rijkaard", "Frank", 61, "Dutch", "CDM", 4));
        ajax.addPlayer(new Player("Bergkamp", "Dennis", 54, "Dutch", "CAM", 10));
        ajax.addPlayer(new Player("van der Sar", "Edwin", 53, "Dutch", "GK", 1));
        ajax.addPlayer(new Player("Litmanen", "Jari", 53, "Finnish", "CAM", 10));
        ajax.addPlayer(new Player("Keizer", "Piet", 73, "Dutch", "LW", 11));
        ajax.addPlayer(new Player("Krol", "Ruud", 75, "Dutch", "LB", 5));

        Team barcelona = new Team("Barcelona", 1899, 9000000.0);
        barcelona.setManager(new Manager("Guardiola", "Pep", 53, "Spanish"));
        barcelona.setStadium(new Stadium("Camp Nou", "Barcelona", 99000));
        barcelona.addPlayer(new Player("Messi", "Lionel", 36, "Argentinian", "RW", 10));
        barcelona.addPlayer(new Player("Gaucho", "Ronaldinho", 44, "Brazilian", "LW", 10));
        barcelona.addPlayer(new Player("Hernandez", "Xavi", 44, "Spanish", "CM", 6));
        barcelona.addPlayer(new Player("Iniesta", "Andres", 39, "Spanish", "CM", 8));
        barcelona.addPlayer(new Player("Puyol", "Carles", 45, "Spanish", "CB", 5));
        barcelona.addPlayer(new Player("Pique", "Gerard", 37, "Spanish", "CB", 3));
        barcelona.addPlayer(new Player("Eto'o", "Samuel", 43, "Cameroonian", "ST", 9));
        barcelona.addPlayer(new Player("Enrique", "Luis", 53, "Spanish", "CM", 21));

        Team aston = new Team("Aston Valea", 2020, 1000.0);
        aston.setManager(new Manager("Mihai", "Manager", 30, "Romanian"));
        aston.setStadium(new Stadium("Valea Arena", "Aston", 500));
        aston.addPlayer(new Player("Cimpanu", "Eddy", 20, "Romanian", "FW", 7));
        aston.addPlayer(new Player("Barbu", "David", 21, "Romanian", "MF", 8));
        aston.addPlayer(new Player("Sqlcianu", "Stefan", 22, "Romanian", "DF", 4));
        aston.addPlayer(new Player("Filote", "Andrei", 20, "Romanian", "MF", 10));
        aston.addPlayer(new Player("Marele", "Sebi", 23, "Romanian", "GK", 1));
        aston.addPlayer(new Player("Airinei", "Vlad", 21, "Romanian", "DF", 3));
        aston.addPlayer(new Player("Pislariu", "Mario", 22, "Romanian", "FW", 9));
        aston.addPlayer(new Player("Vasile", "Portaru", 45, "Romanian", "DF", 2));

        Team dumbraveni = new Team("FC Dumbraveni", 1975, 5000.0);
        dumbraveni.setManager(new Manager("Gheorghe", "Manager", 55, "Romanian"));
        dumbraveni.setStadium(new Stadium("Municipal Dumbraveni", "Dumbraveni", 2000));
        dumbraveni.addPlayer(new Player("Cimpanu", "Eddy", 25, "Romanian", "FW", 11));
        dumbraveni.addPlayer(new Player("Aioanei", "Andrei", 24, "Romanian", "GK", 12));
        dumbraveni.addPlayer(new Player("Morisca", "Alexandru", 23, "Romanian", "MF", 6));
        dumbraveni.addPlayer(new Player("Ifrim", "Vladut", 22, "Romanian", "DF", 5));
        dumbraveni.addPlayer(new Player("Rapa", "Robert", 21, "Romanian", "DF", 2));
        dumbraveni.addPlayer(new Player("Nechifor", "Cristi", 22, "Romanian", "MF", 8));
        dumbraveni.addPlayer(new Player("Hlamaga", "Ciprian", 23, "Romanian", "FW", 7));
        dumbraveni.addPlayer(new Player("Ciorcea", "Ionut", 24, "Romanian", "MF", 10));

        league.addTeam(ajax);
        league.addTeam(barcelona);
        league.addTeam(aston);
        league.addTeam(dumbraveni);
    }

    public League getLeague() { return league; }
}