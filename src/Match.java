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
        if (!homeTeam.isStartingSixFull()) {
            throw new IllegalStateException("Error: Home team " + homeTeam.getName() + " does not have a full starting six!");
        }
        if (!awayTeam.isStartingSixFull()) {
            throw new IllegalStateException("Error: Away team " + awayTeam.getName() + " does not have a full starting six!");
        }

        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.referee = referee;

        this.stadium = homeTeam.getStadium();

        this.homeStarters = Arrays.copyOf(homeTeam.getStartingSix(), homeTeam.getStartingSix().length);
        this.awayStarters = Arrays.copyOf(awayTeam.getStartingSix(), awayTeam.getStartingSix().length);

        this.homeOnField = new ArrayList<>(Arrays.asList(this.homeStarters));
        this.awayOnField = new ArrayList<>(Arrays.asList(this.awayStarters));

        this.homeScore = 0;
        this.awayScore = 0;
        this.isFinished = false;

        this.referee.addMatch();
    }

    private void _validateMatchState() {
        if (isFinished) {
            throw new IllegalStateException("Error: Cannot add events. The match is already finished!");
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
            throw new IllegalArgumentException("Error: The scorer " + scorer.getLastName() + " is not currently on the field!");
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
                throw new IllegalArgumentException("Error: Player " + playerIn.getLastName() + " does not belong to the home team's squad!");
            }

            if (homeOnField.contains(playerIn)) {
                throw new IllegalArgumentException("Error: Player " + playerIn.getLastName() + " is already on the field!");
            }

            homeOnField.remove(playerOut);
            homeOnField.add(playerIn);

        } else if (awayOnField.contains(playerOut)) {
            if (!awayTeam.getSquad().contains(playerIn)) {
                throw new IllegalArgumentException("Error: Player " + playerIn.getLastName() + " does not belong to the away team's squad!");
            }

            if (awayOnField.contains(playerIn)) {
                throw new IllegalArgumentException("Error: Player " + playerIn.getLastName() + " is already on the field!");
            }

            awayOnField.remove(playerOut);
            awayOnField.add(playerIn);

        } else {
            throw new IllegalArgumentException("Error: Player " + playerOut.getLastName() + " is not currently on the field!");
        }

        Substitution subEvent = new Substitution(minute, playerOut, playerIn);
        events.add(subEvent);
    }

    public void finishMatch() {
        if (isFinished) {
            return;
        }

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