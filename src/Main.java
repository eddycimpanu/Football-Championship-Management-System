import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        League league = new League("Super League");
        LeagueService service = new LeagueService(league);

        service.populateLeague(league);

        while (true) {
            System.out.println("\nMAIN MENU");
            System.out.println("1. Teams Management");
            System.out.println("2. Generate Matches");
            System.out.println("3. Auto-complete Starting Lineups");
            System.out.println("4. Referees Management");
            System.out.println("5. Manage Probabilities");
            System.out.println("6. Simulate Next Round");
            System.out.println("7. View Match Details");
            System.out.println("8. Show Standings");
            System.out.println("9. View Player Statistics");
            System.out.println("10. Player Statistics Rankings");
            System.out.println("0. Exit");
            System.out.print("Selection: ");

            int choice = getInt(scanner);
            if (choice == 0) break;

            switch (choice) {
                case 1 -> handleTeamsManagement(service.getLeague(), service, scanner);
                case 2 -> handleLeagueSchedule(service, scanner);
                case 3 -> {
                    System.out.println("\n--- FILLING STARTING LINEUPS ---");
                    service.autoFillAllTeams();

                    System.out.println("\n0. Back");
                    while (true) {
                        try {
                            if (Integer.parseInt(scanner.nextLine()) == 0) break;
                        } catch (Exception e) {
                            System.out.print("Selection: ");
                        }
                    }
                }
                case 4 -> handleRefereesManagement(service.getLeague(), scanner);
                case 5 -> handleProbabilities(service.getLeague(), scanner);
                case 6 -> {
                    if (!service.getLeague().isScheduleGenerated()) {
                        System.out.println("[ERROR] You must generate the schedule first (Option 2)!");
                    } else {
                        service.simulateNextRound();
                    }
                    System.out.println("\nPress 0 to go back.");
                    while (!scanner.nextLine().equals("0"));
                }
                case 7 -> {
                    System.out.print("Enter Home Team: ");
                    String home = scanner.nextLine();
                    System.out.print("Enter Away Team: ");
                    String away = scanner.nextLine();

                    service.showMatchDetails(home, away);

                    System.out.println("\n0. Back");
                    while (true) {
                        try {
                            if (Integer.parseInt(scanner.nextLine()) == 0) break;
                        } catch (Exception e) {
                            System.out.print("Selection: ");
                        }
                    }
                }
                case 8 -> {
                    service.printStandings();
                    System.out.println("\n0. Back");
                    while (true) {
                        try {
                            if (Integer.parseInt(scanner.nextLine()) == 0) break;
                        } catch (Exception e) {
                            System.out.print("Selection: ");
                        }
                    }
                }
                case 9 -> handleViewPlayerStats(service.getLeague(), scanner);
                case 10 -> service.handleStatisticsMenu(scanner);
                default -> System.out.println("Invalid selection.");
            }
        }
        System.out.println("Application closed.");
    }

    private static void handleTeamsManagement(League league, LeagueService service, Scanner scanner) {
        while (true) {
            System.out.println("\nMANAGEMENT MENU");
            System.out.println("1. Add Team");
            System.out.println("2. Remove Team");
            System.out.println("3. Manage Team");
            System.out.println("0. Back");
            System.out.print("Selection: ");

            int choice = getInt(scanner);
            if (choice == 0) break;

            switch (choice) {
                case 1 -> {
                    System.out.println("\nENTER TEAM DETAILS");
                    System.out.print("Team Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Founding Year: ");
                    int year = getInt(scanner);
                    System.out.print("Initial Budget: ");
                    double budget = scanner.nextDouble();
                    scanner.nextLine();
                    Team newTeam = new Team(name, year, budget);
                    league.addTeam(newTeam);
                }
                case 2 -> {
                    if (league.getTeams().isEmpty()) {
                        System.out.println("No teams to delete.");
                    } else {
                        System.out.println("\nSELECT TEAM TO REMOVE");
                        for (int i = 0; i < league.getTeams().size(); i++) {
                            System.out.println((i + 1) + ". " + league.getTeams().get(i).getName());
                        }
                        System.out.println("0. Cancel");
                        System.out.print("Write the number of the team to remove: ");
                        int teamIdx = getInt(scanner);
                        if (teamIdx > 0 && teamIdx <= league.getTeams().size()) {
                            Team removed = league.getTeams().remove(teamIdx - 1);
                            System.out.println("Team '" + removed.getName() + "' has been removed.");
                        } else if (teamIdx != 0) {
                            System.out.println("Invalid selection.");
                        }
                    }
                }
                case 3 -> {
                    if (league.getTeams().isEmpty()) {
                        System.out.println("No teams available to manage.");
                    } else {
                        System.out.println("\nSELECT TEAM TO MANAGE");
                        for (int i = 0; i < league.getTeams().size(); i++) {
                            System.out.println((i + 1) + ". " + league.getTeams().get(i).getName());
                        }
                        System.out.println("0. Back");
                        System.out.print("Selection: ");
                        int teamIdx = getInt(scanner);
                        if (teamIdx > 0 && teamIdx <= league.getTeams().size()) {
                            handleTeamActions(league.getTeams().get(teamIdx - 1), scanner);
                        }
                    }
                }
                default -> System.out.println("Invalid selection.");
            }
        }
    }

    private static void handleTeamActions(Team team, Scanner scanner) {
        while (true) {
            System.out.println("\n--- " + team.getName().toUpperCase() + " MANAGEMENT ---");
            System.out.println("1. Show Full Squad");
            System.out.println("2. Show Starting Lineup");
            System.out.println("3. Edit Starting Lineup");
            System.out.println("4. Add New Player");
            System.out.println("5. Remove Player");
            System.out.println("6. Change Manager");
            System.out.println("7. Change Budget");
            System.out.println("8. Change Stadium");
            System.out.println("0. Back");
            System.out.print("Selection: ");

            int choice = getInt(scanner);
            if (choice == 0) break;

            switch (choice) {
                case 1 -> {
                    while (true) {
                        System.out.println("\n--- FULL SQUAD VIEW ---");
                        if (team.getSquad().isEmpty()) {
                            System.out.println("The squad is currently empty.");
                        } else {
                            for (Player player : team.getSquad()) {
                                System.out.println("#" + player.getKitNumber() + " " +
                                        player.getLastName() + " [" + player.getPosition() + "]");
                            }
                        }
                        System.out.println("\n0. Back");
                        System.out.print("Selection: ");
                        if (getInt(scanner) == 0) break;
                    }
                }
                case 2 -> {
                    while (true) {
                        System.out.println("\n--- STARTING LINEUP VIEW ---");
                        Player[] starters = team.getStartingSix();
                        for (int i = 0; i < starters.length; i++) {
                            String info = (starters[i] != null)
                                    ? "#" + starters[i].getKitNumber() + " " + starters[i].getLastName()
                                    : "[EMPTY SLOT]";
                            System.out.println((i + 1) + ". " + info);
                        }
                        System.out.println("\n0. Back");
                        System.out.print("Selection: ");
                        if (getInt(scanner) == 0) break;
                    }
                }
                case 3 -> {
                    Player[] starters = team.getStartingSix();
                    java.util.List<Player> reserves = new java.util.ArrayList<>();
                    for (Player p : team.getSquad()) {
                        boolean isStarting = false;
                        for (Player s : starters) {
                            if (s != null && s.equals(p)) {
                                isStarting = true;
                                break;
                            }
                        }
                        if (!isStarting) reserves.add(p);
                    }
                    if (reserves.isEmpty()) {
                        System.out.println("No reserves available for substitution!");
                    } else {
                        System.out.println("\n--- AVAILABLE RESERVES ---");
                        for (int i = 0; i < reserves.size(); i++) {
                            System.out.println((i + 1) + ". " + reserves.get(i).getLastName() + " #" + reserves.get(i).getKitNumber());
                        }
                        System.out.print("\nSelect RESERVE to enter (0 to cancel): ");
                        int resIdx = getInt(scanner) - 1;
                        if (resIdx >= 0 && resIdx < reserves.size()) {
                            Player reserveEntering = reserves.get(resIdx);
                            System.out.println("\n--- CURRENT STARTERS ---");
                            for (int i = 0; i < starters.length; i++) {
                                String info = (starters[i] != null)
                                        ? starters[i].getLastName() + " (#" + starters[i].getKitNumber() + ")"
                                        : "[EMPTY SLOT]";
                                System.out.println((i + 1) + ". " + info);
                            }
                            System.out.print("\nSelect STARTER position to replace (1-6): ");
                            int starIdx = getInt(scanner) - 1;
                            if (starIdx >= 0 && starIdx < 6) {
                                Player playerExiting = starters[starIdx];
                                team.addPlayerToStartingSix(reserveEntering, starIdx);
                                if (playerExiting != null) {
                                    System.out.println("\nSubstitution: " + reserveEntering.getLastName() + " IN, " + playerExiting.getLastName() + " OUT.");
                                } else {
                                    System.out.println("\n" + reserveEntering.getLastName() + " is now a starter.");
                                }
                            }
                        }
                    }
                }
                case 4 -> {
                    System.out.println("\n--- REGISTER NEW PLAYER ---");
                    System.out.print("Last Name: ");
                    String ln = scanner.nextLine();
                    System.out.print("First Name: ");
                    String fn = scanner.nextLine();
                    System.out.print("Age: ");
                    int age = getInt(scanner);
                    System.out.print("Nationality: ");
                    String nat = scanner.nextLine();
                    System.out.print("Position: ");
                    String pos = scanner.nextLine();
                    int kit;
                    while (true) {
                        System.out.print("Kit Number: ");
                        kit = getInt(scanner);
                        boolean isTaken = false;
                        for (Player p : team.getSquad()) {
                            if (p.getKitNumber() == kit) {
                                isTaken = true;
                                break;
                            }
                        }
                        if (!isTaken) break;
                        System.out.println("Error: Kit number already taken!");
                    }
                    team.addPlayer(new Player(ln, fn, age, nat, pos, kit));
                    System.out.println("Player registered successfully!");
                }
                case 5 -> {
                    if (team.getSquad().isEmpty()) {
                        System.out.println("Squad is empty.");
                    } else {
                        java.util.List<Player> players = new java.util.ArrayList<>(team.getSquad());
                        for (int i = 0; i < players.size(); i++) {
                            System.out.println((i + 1) + ". " + players.get(i).getLastName() + " #" + players.get(i).getKitNumber());
                        }
                        System.out.print("Select player to remove (0 to cancel): ");
                        int pIdx = getInt(scanner);
                        if (pIdx > 0 && pIdx <= players.size()) {
                            team.getSquad().remove(players.get(pIdx - 1));
                            System.out.println("Player removed from squad.");
                        }
                    }
                }
                case 6 -> {
                    System.out.println("\n--- ASSIGN NEW MANAGER ---");
                    System.out.print("Last Name: ");
                    String last_name = scanner.nextLine();
                    System.out.print("First Name: ");
                    String first_name = scanner.nextLine();
                    System.out.print("Age: ");
                    int age = getInt(scanner);
                    System.out.print("Nationality: ");
                    String nationality = scanner.nextLine();
                    team.setManager(new Manager(last_name, first_name, age, nationality));
                    System.out.println("Manager " + last_name + " " + first_name + " is now officially in charge of " + team.getName() + "!");
                }
                case 7 -> {
                    System.out.print("Enter new budget: ");
                    double b = scanner.nextDouble();
                    scanner.nextLine();
                    team.setBudget(b);
                    System.out.println("Budget updated.");
                }
                case 8 -> {
                    System.out.println("\n--- UPDATE STADIUM INFO ---");
                    System.out.print("Stadium Name: ");
                    String stadium_name = scanner.nextLine();
                    System.out.print("City: ");
                    String city = scanner.nextLine();
                    System.out.print("Capacity: ");
                    int capacity = getInt(scanner);
                    team.setStadium(new Stadium(stadium_name, city, capacity));
                    System.out.println("Success: " + team.getName() + " now plays at " + stadium_name + " in " + city + " (" + capacity + " seats).");
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void handleLeagueSchedule(LeagueService service, java.util.Scanner scanner) {
        try {
            if (!service.getLeague().isScheduleGenerated()) {
                service.generateSchedule();
                System.out.println("\n[SUCCESS] Season schedule created!");
            } else {
                System.out.println("\n[INFO] Schedule is already active.");
            }
            service.printMatches();
        } catch (IllegalStateException e) {
            System.out.println("\n[ERROR] " + e.getMessage());
        }
        System.out.println("\nPress 0 to go back.");
        while (true) {
            try {
                String input = scanner.nextLine();
                if (input.equals("0")) break;
            } catch (Exception e) {
                System.out.println("Press 0 to exit.");
            }
        }
    }

    private static void handleRefereesManagement(League league, Scanner scanner) {
        while (true) {
            System.out.println("\n--- REFEREES MANAGEMENT ---");
            System.out.println("1. Add Referee");
            System.out.println("2. Remove Referee");
            System.out.println("3. Show Referee Statistics");
            System.out.println("0. Back");
            System.out.print("Selection: ");

            int choice = getInt(scanner);
            if (choice == 0) break;

            switch (choice) {
                case 1 -> {
                    System.out.println("\n--- ADD NEW REFEREE ---");
                    System.out.print("First Name: ");
                    String fn = scanner.nextLine();
                    System.out.print("Last Name: ");
                    String ln = scanner.nextLine();
                    System.out.print("Age: ");
                    int age = getInt(scanner);
                    System.out.print("Nationality: ");
                    String nat = scanner.nextLine();
                    league.addReferee(new Referee(fn, ln, age, nat));
                    System.out.println("[SUCCESS] Referee added.");
                }
                case 2 -> {
                    if (league.getReferees().isEmpty()) {
                        System.out.println("No referees to remove.");
                    } else {
                        for (int i = 0; i < league.getReferees().size(); i++) {
                            System.out.println((i + 1) + ". " + league.getReferees().get(i).getLastName());
                        }
                        System.out.print("Select index to remove (0 to cancel): ");
                        int idx = getInt(scanner) - 1;
                        if (idx >= 0 && idx < league.getReferees().size()) league.getReferees().remove(idx);
                    }
                }
                case 3 -> {
                    if (league.getReferees().isEmpty()) {
                        System.out.println("No referees in the league.");
                    } else {
                        System.out.println("\n--- SELECT REFEREE TO VIEW STATS ---");
                        for (int i = 0; i < league.getReferees().size(); i++) {
                            System.out.println((i + 1) + ". " + league.getReferees().get(i).getLastName());
                        }
                        System.out.print("Selection: ");
                        int idx = getInt(scanner) - 1;
                        if (idx >= 0 && idx < league.getReferees().size()) {
                            Referee r = league.getReferees().get(idx);
                            System.out.println("\n--- STATS FOR " + r.getLastName() + " " + r.getFirstName() + " ---");
                            System.out.println("Matches Officiated: " + r.getMatchesOfficiated());
                            System.out.println("Yellow Cards Given: " + r.getYellowCardsGiven());
                            System.out.println("Red Cards Given:    " + r.getRedCardsGiven());
                        }
                    }
                    System.out.println("\nPress 0 to go back.");
                    while (!scanner.nextLine().equals("0"));
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void handleProbabilities(League league, java.util.Scanner scanner) {
        while (true) {
            System.out.println("\n--- PROBABILITY SETTINGS ---");
            System.out.println("1. Goal Chance [" + league.getGoalChance() + "]");
            System.out.println("2. Yellow Card Chance [" + league.getYellowCardChance() + "]");
            System.out.println("3. Substitution Base Chance [" + league.getSubstitutionBaseChance() + "]");
            System.out.println("0. Back to Main Menu");
            System.out.print("Selection: ");
            String choice = scanner.nextLine();
            if (choice.equals("0")) break;
            try {
                switch (choice) {
                    case "1" -> {
                        System.out.print("New Goal Chance (e.g., 0.02): ");
                        league.setGoalChance(Double.parseDouble(scanner.nextLine()));
                    }
                    case "2" -> {
                        System.out.print("New Yellow Card Chance (e.g., 0.01): ");
                        league.setYellowCardChance(Double.parseDouble(scanner.nextLine()));
                    }
                    case "3" -> {
                        System.out.print("New Sub Base Chance (e.g., 0.005): ");
                        league.setSubstitutionBaseChance(Double.parseDouble(scanner.nextLine()));
                    }
                    default -> System.out.println("Invalid option!");
                }
                System.out.println("[SUCCESS] Setting updated!");
            } catch (Exception e) {
                System.out.println("[ERROR] Please enter a valid number (e.g., 0.05)");
            }
        }
    }

    private static void handleViewPlayerStats(League league, Scanner scanner) {
        System.out.print("\nEnter Player Last Name to search: ");
        String searchName = scanner.nextLine();
        boolean found = false;
        System.out.println("\n--- SEARCH RESULTS ---");
        for (Team team : league.getTeams()) {
            for (Player p : team.getSquad()) {
                if (p.getLastName().equalsIgnoreCase(searchName)) {
                    found = true;
                    System.out.println("Player: " + p.getFirstName() + " " + p.getLastName());
                    System.out.println("Team: " + team.getName());
                    System.out.println("Position: " + p.getPosition() + " | Kit: #" + p.getKitNumber());
                    System.out.println("-------------------------");
                    System.out.println("Minutes Played: " + p.getMinutesPlayed());
                    System.out.println("Goals Scored:   " + p.getGoals());
                    System.out.println("Assists:        " + p.getAssists());
                    System.out.println("Yellow Cards:   " + p.getYellowCards());
                    System.out.println("Red Cards:      " + p.getRedCards());
                    System.out.println("-------------------------");
                }
            }
        }
        if (!found) System.out.println("[INFO] No player found with the name: " + searchName);
        System.out.println("\nPress 0 to go back.");
        while (!scanner.nextLine().equals("0"));
    }

    private static int getInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a number: ");
            scanner.next();
        }
        int val = scanner.nextInt();
        scanner.nextLine();
        return val;
    }
}