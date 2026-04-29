import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        League league = new League("Super League");
        LeagueService service = new LeagueService(league);

        service.populateLeague(league);

        while (true) {
            System.out.println("\nMAIN MENU");
            System.out.println("1. Management");
            System.out.println("2. Tournament Simulation");
            System.out.println("3. Statistics and Reports");
            System.out.println("0. Exit");
            System.out.print("Selection: ");

            int choice = getInt(scanner);
            if (choice == 0) break;

            switch (choice) {
                case 1 -> handleManagement(league, service, scanner);
                case 2 -> handleSimulation(league, service, scanner);
                case 3 -> handleStatistics(service, scanner);
                default -> System.out.println("Invalid selection.");
            }
        }
        System.out.println("Application closed.");
    }

    private static void handleManagement(League league, LeagueService service, Scanner scanner) {
        while (true) {
            System.out.println("\nMANAGEMENT MENU");
            System.out.println("1. Add New Team");
            System.out.println("2. Manage Specific Team (Squad/Rename/Delete)");
            System.out.println("3. Show All Teams");
            System.out.println("0. Back");
            System.out.print("Selection: ");

            int choice = getInt(scanner);
            if (choice == 0) break;

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Team Name: ");
                    String name = scanner.nextLine();
                    league.addTeam(new Team(name, 2024, 50000.0));
                }
                case 2 -> handleSpecificTeam(league, scanner);
                case 3 -> service.printAllTeams();
                default -> System.out.println("Invalid selection.");
            }
        }
    }

    private static void handleSpecificTeam(League league, Scanner scanner) {
        System.out.print("Enter team name to manage: ");
        String name = scanner.nextLine();
        Team team = findTeam(league, name);

        if (team == null) {
            System.out.println("Team not found.");
            return;
        }

        while (true) {
            System.out.println("\nMANAGE TEAM: " + team.getName().toUpperCase());
            System.out.println("1. View Squad ");
            System.out.println("2. Rename Team");
            System.out.println("3. Delete Team");
            System.out.println("4. Add Player");
            System.out.println("5. Add New Manager");
            System.out.println("0. Back");
            System.out.print("Selection: ");

            int choice = getInt(scanner);
            if (choice == 0) break;

            switch (choice) {
                case 1 -> {
                    System.out.println("Squad for " + team.getName() + ":");
                    for (Player p : team.getSquad()) {
                        System.out.println("- " + p.getLastName() + " " + p.getFirstName() + " (" + p.getPosition() + ")");
                    }
                }
                case 2 -> {
                    System.out.print("Enter new name: ");
                    String newName = scanner.nextLine();
                    team.setName(newName);
                }
                case 3 -> {
                    league.getTeams().remove(team);
                    System.out.println("Team deleted.");
                    return;
                }
                case 4 -> {
                    System.out.println("Adding new player to " + team.getName());

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

                        boolean numberExists = false;
                        for (Player p : team.getSquad()) {
                            if (p.getKitNumber() == kit) {
                                numberExists = true;
                                break;
                            }
                        }

                        if (!numberExists) {
                            break;
                        }
                        System.out.println("Error: Number " + kit + " is already taken. Please choose another one.");
                    }
                    Player newPlayer = new Player(ln, fn, age, nat, pos, kit);

                    team.addPlayer(newPlayer);
                }
                case 5 -> {
                    System.out.print("Last Name: ");
                    String ln = scanner.nextLine();

                    System.out.print("First Name: ");
                    String fn = scanner.nextLine();

                    System.out.print("Age: ");
                    int age = getInt(scanner);

                    System.out.print("Nationality: ");
                    String nat = scanner.nextLine();

                    Manager newManager = new Manager(ln, fn, age, nat);

                    team.setManager(newManager);
                }
                default -> System.out.println("Invalid selection.");
            }
        }
    }

    private static void handleSimulation(League league, LeagueService service, Scanner scanner) {
        while (true) {
            System.out.println("\nSIMULATION MENU");
            System.out.println("1. Generate Season Schedule");
            System.out.println("2. Play Next Match");
            System.out.println("0. Back");
            System.out.print("Selection: ");

            int choice = getInt(scanner);
            if (choice == 0) break;

            switch (choice) {
                case 1 -> {
                    league.generateSchedule();
                    System.out.println("Schedule generated for all rounds.");
                }
                case 2 -> System.out.println("Match simulation logic goes here.");
                default -> System.out.println("Invalid selection.");
            }
        }
    }

    // --- 3. STATISTICS ---
    private static void handleStatistics(LeagueService service, Scanner scanner) {
        while (true) {
            System.out.println("\nSTATISTICS MENU");
            System.out.println("1. View Standings");
            System.out.println("2. View Match History");
            System.out.println("3. Best Attack Team");
            System.out.println("0. Back");
            System.out.print("Selection: ");

            int choice = getInt(scanner);
            if (choice == 0) break;

            switch (choice) {
                case 1 -> service.printStandings();
                case 2 -> service.printMatches(true);
                case 3 -> service.printTopScoringTeam();
                default -> System.out.println("Invalid selection.");
            }
        }
    }

    // --- HELPERS ---
    private static Team findTeam(League league, String name) {
        for (Team t : league.getTeams()) {
            if (t.getName().equalsIgnoreCase(name)) return t;
        }
        return null;
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