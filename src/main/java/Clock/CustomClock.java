package Clock;

public class CustomClock implements Clock {
    private long currentTimestamp;

    public void setCurrentSecondsSince1970(long currentTimestamp) {
        this.currentTimestamp = currentTimestamp;
    }

    public void addSecondsToCurrent(long seconds) {
        this.currentTimestamp += seconds;
    }

    public void takeSecondsFromCurrent(long seconds) {
        this.currentTimestamp -= seconds;
    }

    @Override
    public long secondsSince1970() {
        return currentTimestamp;
    }
}
