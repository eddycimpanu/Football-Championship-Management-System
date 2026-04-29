import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class LeagueService {
    private League league;

    public LeagueService(League league) {
        if (league == null) {
            throw new IllegalArgumentException("Error: League cannot be null!");
        }
        this.league = league;
    }

    public void printStandings() {
        if (league.getTeams().isEmpty()) {
            System.out.println("No teams in the league.");
            return;
        }

        Set<Team> sortedTeams = new TreeSet<>(league.getTeams());

        System.out.println("\nLEAGUE STANDINGS");
        int pos = 1;
        for (Team t : sortedTeams) {
            System.out.println(pos + ". " + t.getName() + " | Points: " + t.getPoints() +
                    " | GD: " + t.getGoalDifference() +
                    " (Scored: " + t.getGoalsScored() + " / Conceded: " + t.getGoalsConceded() + ")");
            pos++;
        }
    }

    public void printMatches(boolean showFinished) {
        if (league.getMatches().isEmpty()) {
            System.out.println("No matches scheduled yet.");
            return;
        }

        System.out.println(showFinished ? "\nMATCH HISTORY" : "\nUPCOMING FIXTURES");
        boolean found = false;
        for (Match m : league.getMatches()) {
            if (m.isFinished() == showFinished) {
                String result = m.isFinished() ? m.getHomeScore() + " - " + m.getAwayScore() : "VS";
                System.out.println("Round " + m.getMatchday() + ": " +
                        m.getHomeTeam().getName() + " " + result + " " +
                        m.getAwayTeam().getName());
                found = true;
            }
        }
        if (!found) System.out.println("No matches found for this category.");
    }

    public void printTopScoringTeam() {
        if (league.getTeams().isEmpty()) {
            System.out.println("No teams in the league.");
            return;
        }

        Team topAttacker = league.getTeams().get(0);
        for (Team t : league.getTeams()) {
            if (t.getGoalsScored() > topAttacker.getGoalsScored()) {
                topAttacker = t;
            }
        }

        System.out.println("\nTOP SCORING TEAM");
        System.out.println("The team with the best attack is " + topAttacker.getName() +
                " with " + topAttacker.getGoalsScored() + " goals scored!");
    }

    public void printAllTeams() {
        if (league.getTeams().isEmpty()) {
            System.out.println("No teams registered in the league.");
            return;
        }

        System.out.println("\nEXISTING TEAMS");
        for (Team t : league.getTeams()) {
            System.out.println("- " + t.getName() + " (Manager: " +
                    (t.getManager() != null ? t.getManager().getLastName() : "No manager") + ")");
        }
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
