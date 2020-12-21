import Clock.CustomClock;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ClockTest {

    private final long INITIAL_SECONDS = 21;

    private CustomClock clock;

    @Before
    public void setupDB() {
        clock = new CustomClock();
    }

    @Test
    public void testCustomClockSetNow() {
        clock.setCurrentSecondsSince1970(INITIAL_SECONDS);
        assertEquals(INITIAL_SECONDS, clock.secondsSince1970());
    }

    @Test
    public void testCustomClockAddSeconds() {
        clock.setCurrentSecondsSince1970(INITIAL_SECONDS);
        clock.addSecondsToCurrent(10);
        assertEquals(INITIAL_SECONDS + 10, clock.secondsSince1970());
    }

    @Test
    public void testCustomClockTakeSeconds() {
        clock.setCurrentSecondsSince1970(INITIAL_SECONDS);
        clock.takeSecondsFromCurrent(10);
        assertEquals(INITIAL_SECONDS - 10, clock.secondsSince1970());
    }
}
