import java.util.Collections;
import java.util.List;

public class LeagueService {
    private League league;

    public LeagueService(League league) {
        if (league == null) {
            throw new IllegalArgumentException("Error: League cannot be null!");
        }
        this.league = league;
    }

    public void printDetailedStandings() {
        List<Team> teams = league.getTeams();

        if (teams.isEmpty()) {
            System.out.println("No teams in the league!");
            return;
        }

        Collections.sort(teams);

        System.out.println("\n=== DETAILED STANDINGS ===");
        for (int i = 0; i < teams.size(); i++) {
            Team t = teams.get(i);

            System.out.println((i + 1) + ". " + t.getName() + " - "
                    + t.getPoints() + " points, "
                    + t.getGoalsScored() + " goals scored, "
                    + t.getGoalsConceded() + " goals conceded");
        }
        System.out.println("==========================\n");
    }

    public void printStandings() {
        List<Team> teams = league.getTeams();

        if (teams.isEmpty()) {
            System.out.println("No teams in the league!");
            return;
        }

        Collections.sort(teams);
        System.out.println("\n=== LEAGUE STANDINGS ===");
        for (int i = 0; i < teams.size(); i++) {
            Team t = teams.get(i);

            System.out.println((i + 1) + ". " + t.getName());
        }
        System.out.println("========================\n");
    }


    public void printMatches(boolean showFinished) {
        String title = showFinished ? "MATCH HISTORY (RESULTS)" : "UPCOMING MATCHES (SCHEDULE)";
        System.out.println("\n=== " + title + " ===");

        boolean found = false;
        for (Match m : league.getMatches()) {
            if (m.isFinished() == showFinished) {
                String score = m.isFinished() ? " | " + m.getHomeScore() + " - " + m.getAwayScore() : " | VS ";
                System.out.println("Round " + m.getMatchday() + ": " +
                        m.getHomeTeam().getName() + score +
                        m.getAwayTeam().getName() +
                        " (Referee: " + m.getReferee().getLastName() + ")");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matches to display in this category.");
        }
        System.out.println("===============================\n");
    }

    public League getLeague() { return league; }

    public static void populateLeague(League league) {
        Referee ref1 = new Referee("Kovacs", "Istvan", 41, "Romanian");
        league.addReferee(ref1);

        Team ajax = new Team("Ajax", 1900, 6000000.0);
        Team aston = new Team("Aston Valea", 2020, 1000.0);
        Team dumbraveni = new Team("FC Dumbraveni", 1975, 5000.0);
        Team barcelona = new Team("Barcelona", 1899, 9000000.0);

        ajax.addPlayer(new Player("Cruyff", "Johan", 70, "Dutch", "FW", 14));
        ajax.addPlayer(new Player("van Basten", "Marco", 59, "Dutch", "ST", 9));
        ajax.addPlayer(new Player("Rijkaard", "Frank", 61, "Dutch", "CDM", 4));
        ajax.addPlayer(new Player("Bergkamp", "Dennis", 54, "Dutch", "CAM", 10));
        ajax.addPlayer(new Player("van der Sar", "Edwin", 53, "Dutch", "GK", 1));
        ajax.addPlayer(new Player("Litmanen", "Jari", 53, "Finnish", "CAM", 10));
        ajax.addPlayer(new Player("Keizer", "Piet", 73, "Dutch", "LW", 11));
        ajax.addPlayer(new Player("Krol", "Ruud", 75, "Dutch", "LB", 5));

        aston.addPlayer(new Player("Cimpanu", "Eddy", 20, "Romanian", "FW", 7));
        aston.addPlayer(new Player("Barbu", "David", 21, "Romanian", "MF", 8));
        aston.addPlayer(new Player("Sqlcianu", "Stefan", 22, "Romanian", "DF", 4));
        aston.addPlayer(new Player("Filote", "Andrei", 20, "Romanian", "MF", 10));
        aston.addPlayer(new Player("Marele", "Sebi", 23, "Romanian", "GK", 1));
        aston.addPlayer(new Player("Airinei", "Vlad", 21, "Romanian", "DF", 3));
        aston.addPlayer(new Player("Pislariu", "Mario", 22, "Romanian", "FW", 9));
        aston.addPlayer(new Player("Vasile", "Portaru", 45, "Romanian", "DF", 2));

        dumbraveni.addPlayer(new Player("Cimpanu", "Eddy", 25, "Romanian", "FW", 11));
        dumbraveni.addPlayer(new Player("Aioanei", "Andrei", 24, "Romanian", "GK", 12));
        dumbraveni.addPlayer(new Player("Morisca", "Alexandru", 23, "Romanian", "MF", 6));
        dumbraveni.addPlayer(new Player("Ifrim", "Vladut", 22, "Romanian", "DF", 5));
        dumbraveni.addPlayer(new Player("Rapa", "Robert", 21, "Romanian", "DF", 2));
        dumbraveni.addPlayer(new Player("Nechifor", "Cristi", 22, "Romanian", "MF", 8));
        dumbraveni.addPlayer(new Player("Hlamaga", "Ciprian", 23, "Romanian", "FW", 7));
        dumbraveni.addPlayer(new Player("Ciorcea", "Ionut", 24, "Romanian", "MF", 10));

        barcelona.addPlayer(new Player("Messi", "Lionel", 36, "Argentinian", "RW", 10));
        barcelona.addPlayer(new Player("Gaucho", "Ronaldinho", 44, "Brazilian", "LW", 10));
        barcelona.addPlayer(new Player("Hernandez", "Xavi", 44, "Spanish", "CM", 6));
        barcelona.addPlayer(new Player("Iniesta", "Andres", 39, "Spanish", "CM", 8));
        barcelona.addPlayer(new Player("Puyol", "Carles", 45, "Spanish", "CB", 5));
        barcelona.addPlayer(new Player("Pique", "Gerard", 37, "Spanish", "CB", 3));
        barcelona.addPlayer(new Player("Eto'o", "Samuel", 43, "Cameroonian", "ST", 9));
        barcelona.addPlayer(new Player("Enrique", "Luis", 53, "Spanish", "CM", 21));

        league.addTeam(ajax);
        league.addTeam(aston);
        league.addTeam(dumbraveni);
        league.addTeam(barcelona);
    }
}
