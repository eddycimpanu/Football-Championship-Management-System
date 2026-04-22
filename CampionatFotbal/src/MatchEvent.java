public abstract class MatchEvent {
    private int minute;

    public MatchEvent(int minute) {
        if (minute < 0) {
            throw new IllegalArgumentException("Error: Minute cannot be negative");
        }
        this.minute = minute;
    }

    public int getMinute() {
        return minute;
    }
}