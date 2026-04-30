import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Match {
    private Team homeTeam;
    private Team awayTeam;
    private Stadium stadium;
    private Referee referee;
    private Player[] homeStarters;
    private Player[] awayStarters;
    private List<Player> homeOnField;
    private List<Player> awayOnField;
    private List<MatchEvent> events;
    private int matchday;
    private int homeScore;
    private int awayScore;
    private boolean isFinished;

    public Match(Team homeTeam, Team awayTeam, Referee referee) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.referee = referee;
        this.stadium = homeTeam.getStadium();
        this.events = new ArrayList<>();

        if (homeTeam.getStartingSix() != null) {
            this.homeStarters = Arrays.copyOf(homeTeam.getStartingSix(), homeTeam.getStartingSix().length);
        } else {
            this.homeStarters = new Player[0];
        }

        if (awayTeam.getStartingSix() != null) {
            this.awayStarters = Arrays.copyOf(awayTeam.getStartingSix(), awayTeam.getStartingSix().length);
        } else {
            this.awayStarters = new Player[0];
        }

        this.homeOnField = new ArrayList<>();
        for (Player p : homeStarters) {
            if (p != null) homeOnField.add(p);
        }

        this.awayOnField = new ArrayList<>();
        for (Player p : awayStarters) {
            if (p != null) awayOnField.add(p);
        }

        this.homeScore = 0;
        this.awayScore = 0;
        this.isFinished = false;

        if (this.referee != null) {
            this.referee.addMatch();
        }
    }

    public void displayEvents() {
        System.out.println("\n=======================================");
        System.out.println("   MATCH REPORT: " + homeTeam.getName() + " vs " + awayTeam.getName());
        System.out.println("   FINAL SCORE: " + homeScore + " - " + awayScore);
        System.out.println("=======================================");

        if (events.isEmpty()) {
            System.out.println("No major events recorded.");
        } else {
            for (MatchEvent event : events) {
                int min = event.getMinute();

                if (event instanceof Goal g) {
                    System.out.println("Min " + min + ": GOAL! Scored by " + g.getScorer().getLastName());
                }
                else if (event instanceof Card c) {
                    if (c.getColor().equalsIgnoreCase("Yellow")) {
                        System.out.println("Min " + min + ": Yellow Card - " + c.getPerson().getLastName());
                    } else {
                        System.out.println("Min " + min + ": RED CARD! - " + c.getPerson().getLastName());
                    }
                }
                else if (event instanceof Substitution s) {
                    System.out.println("Min " + min + ": Sub: Out " + s.getPlayerOut().getLastName() + " / In " + s.getPlayerIn().getLastName());
                }
            }
        }
        System.out.println("=======================================");
    }

    private void _validateMatchState() {
        if (isFinished) {
            throw new IllegalStateException("Error: The match is already finished!");
        }
    }

    public void setMatchday(int matchday) {
        if (matchday <= 0) {
            throw new IllegalArgumentException("Error: Matchday must be a positive number!");
        }
        this.matchday = matchday;
    }

    public int getMatchday() {
        return matchday;
    }

    public void addGoal(int minute, Player scorer, Player assistant) {
        _validateMatchState();

        if (homeOnField.contains(scorer)) {
            homeScore++;
        } else if (awayOnField.contains(scorer)) {
            awayScore++;
        } else {
            throw new IllegalArgumentException("Error: Scorer " + scorer.getLastName() + " is not on the field!");
        }

        Goal goalEvent;
        if (assistant == null) {
            goalEvent = new Goal(minute, scorer);
        } else {
            assistant.addAssist();
            goalEvent = new Goal(minute, scorer, assistant);
        }

        events.add(goalEvent);
        scorer.addGoal();
    }

    public void addGoal(int minute, Player scorer) {
        this.addGoal(minute, scorer, null);
    }

    public void addCard(int minute, Person offender, String color) {
        _validateMatchState();

        int yellowCountInThisMatch = 0;
        for (MatchEvent event : events) {
            if (event instanceof Card) {
                Card previousCard = (Card) event;
                if (previousCard.getPerson().equals(offender) && previousCard.getColor().equalsIgnoreCase("Yellow")) {
                    yellowCountInThisMatch++;
                }
            }
        }

        Card cardEvent = new Card(minute, offender, color);
        events.add(cardEvent);

        if (offender instanceof Player) {
            Player p = (Player) offender;
            if (color.equalsIgnoreCase("Yellow")) {
                p.addYellowCard();
                if (yellowCountInThisMatch == 1) {
                    p.addRedCard();
                    homeOnField.remove(p);
                    awayOnField.remove(p);
                    events.add(new Card(minute, p, "Red"));
                }
            } else if (color.equalsIgnoreCase("Red")) {
                p.addRedCard();
                homeOnField.remove(p);
                awayOnField.remove(p);
            }
        } else if (offender instanceof Manager) {
            Manager m = (Manager) offender;
            if (color.equalsIgnoreCase("Yellow")) {
                m.addYellowCard();
                if (yellowCountInThisMatch == 1) {
                    m.addRedCard();
                    events.add(new Card(minute, m, "Red"));
                }
            } else if (color.equalsIgnoreCase("Red")) {
                m.addRedCard();
            }
        }
    }

    public void addSubstitution(int minute, Player playerOut, Player playerIn) {
        _validateMatchState();

        if (homeOnField.contains(playerOut)) {
            if (!homeTeam.getSquad().contains(playerIn)) {
                throw new IllegalArgumentException("Error: Player does not belong to home team!");
            }
            if (homeOnField.contains(playerIn)) {
                throw new IllegalArgumentException("Error: Player already on field!");
            }
            homeOnField.remove(playerOut);
            homeOnField.add(playerIn);
        } else if (awayOnField.contains(playerOut)) {
            if (!awayTeam.getSquad().contains(playerIn)) {
                throw new IllegalArgumentException("Error: Player does not belong to away team!");
            }
            if (awayOnField.contains(playerIn)) {
                throw new IllegalArgumentException("Error: Player already on field!");
            }
            awayOnField.remove(playerOut);
            awayOnField.add(playerIn);
        } else {
            throw new IllegalArgumentException("Error: Player out is not on the field!");
        }

        events.add(new Substitution(minute, playerOut, playerIn));
    }

    public void finishMatch() {
        if (isFinished) return;
        this.isFinished = true;

        homeTeam.updateGoals(homeScore, awayScore);
        awayTeam.updateGoals(awayScore, homeScore);

        if (homeScore > awayScore) {
            homeTeam.addPoints(3);
        } else if (awayScore > homeScore) {
            awayTeam.addPoints(3);
        } else {
            homeTeam.addPoints(1);
            awayTeam.addPoints(1);
        }
    }

    public Team getHomeTeam() { return homeTeam; }
    public Team getAwayTeam() { return awayTeam; }
    public Stadium getStadium() { return stadium; }
    public Referee getReferee() { return referee; }
    public int getHomeScore() { return homeScore; }
    public int getAwayScore() { return awayScore; }
    public boolean isFinished() { return isFinished; }
    public Player[] getHomeStarters() { return homeStarters; }
    public Player[] getAwayStarters() { return awayStarters; }
}