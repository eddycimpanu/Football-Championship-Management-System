import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        League league = new League("Liga de carton");
        LeagueService service = new LeagueService(league);

        service.populateLeague(league);

        boolean running = true;
        while (running) {
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1. League Statistics (Standings, Matches)");
            System.out.println("2. Manage a Specific Team");
            System.out.println("3. Generate/Reset Schedule");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    showLeagueMenu(service, scanner);
                    break;
                case 2:
                    showTeamManagementMenu(league, scanner);
                    break;
                case 3:
                    league.generateSchedule();
                    System.out.println("-> Schedule generated successfully!");
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private static void showLeagueMenu(LeagueService service, Scanner scanner) {
        System.out.println("\n--- LEAGUE STATISTICS ---");
        System.out.println("1. Show Standings (Detailed)");
        System.out.println("2. Show Upcoming Matches");
        System.out.println("3. Show Match History");
        System.out.print("Choice: ");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1 -> service.printDetailedStandings();
            case 2 -> service.printMatches(false);
            case 3 -> service.printMatches(true);
        }
    }

    private static void showTeamManagementMenu(League league, Scanner scanner) {
        System.out.print("Enter the name of the team you want to manage: ");
        String teamName = scanner.nextLine();

        Team team = null;
        for (Team t : league.getTeams()) {
            if (t.getName().equalsIgnoreCase(teamName)) {
                team = t;
                break;
            }
        }

        if (team == null) {
            System.out.println("Team not found!");
            return;
        }

        boolean back = false;
        while (!back) {
            System.out.println("\n--- MANAGING: " + team.getName().toUpperCase() + " ---");
            System.out.println("1. Change Manager");
            System.out.println("2. Add Player to Squad");
            System.out.println("3. Show Squad (Sorted)");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choice: ");

            int subChoice = scanner.nextInt();
            scanner.nextLine();

            switch (subChoice) {
                case 1:
                    System.out.print("New Manager Last Name: ");
                    String ln = scanner.nextLine();
                    System.out.print("New Manager First Name: ");
                    String fn = scanner.nextLine();
                    team.setManager(new Manager(ln, fn, 45, "Unknown"));
                    System.out.println("Manager updated!");
                    break;
                case 2:
                    System.out.println("Feature coming soon: Add Player via Scanner");
                    break;
                case 3:
                    System.out.println("Squad for " + team.getName() + ":");
                    for (Player p : team.getSquad()) {
                        System.out.println("- " + p.getLastName() + " " + p.getFirstName());
                    }
                    break;
                case 0:
                    back = true;
                    break;
            }
        }
    }

}