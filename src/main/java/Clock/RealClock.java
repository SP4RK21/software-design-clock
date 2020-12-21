package Clock;

public class RealClock implements Clock {
    @Override
    public long secondsSince1970() {
        return System.currentTimeMillis() / 1000;
    }
}
