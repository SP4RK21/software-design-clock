import Clock.CustomClock;
import Events.EventsManager;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

public class EventsManagerTest {

    private final double EPS = 1e-5;

    private boolean checkStatsEqual(Map<String, Double> first, Map<String, Double> second) {
        if (!first.keySet().equals(second.keySet())) {
            return false;
        }

        for (Map.Entry<String, Double> entry : first.entrySet()) {
            if (second.get(entry.getKey()) == null ||
                    Math.abs(second.get(entry.getKey()) - entry.getValue()) > EPS) {
                return false;
            }
        }

        return true;
    }

    @Test
    public void basicFunctionalityTest() {
        CustomClock clock = new CustomClock();
        clock.setCurrentSecondsSince1970(System.currentTimeMillis() / 1000);
        EventsManager manager = new EventsManager(clock);

        manager.incEvent("EventA");
        manager.incEvent("EventB");
        manager.incEvent("EventA");

        assertTrue(checkStatsEqual(
                manager.getAllEventStatistic(),
                Map.of("EventA", 2.0 / 60,
                        "EventB", 1.0 / 60)
                )
        );
    }

    @Test
    public void eventStatByNameTest() {
        CustomClock clock = new CustomClock();
        clock.setCurrentSecondsSince1970(System.currentTimeMillis() / 1000);
        EventsManager manager = new EventsManager(clock);

        manager.incEvent("EventA");
        manager.incEvent("EventB");
        manager.incEvent("EventA");

        assertTrue(Math.abs(2.0 / 60 - manager.getEventStatisticByName("EventA")) < EPS);
    }

    @Test
    public void nonexistentEventStatByNameTest() {
        CustomClock clock = new CustomClock();
        clock.setCurrentSecondsSince1970(System.currentTimeMillis() / 1000);
        EventsManager manager = new EventsManager(clock);

        manager.incEvent("EventA");
        manager.incEvent("EventA");

        assertEquals(0, manager.getEventStatisticByName("EventB"), EPS);
    }

    @Test
    public void expiredSingleEventTest() {
        CustomClock clock = new CustomClock();
        clock.setCurrentSecondsSince1970(System.currentTimeMillis() / 1000);
        EventsManager manager = new EventsManager(clock);

        manager.incEvent("EventA");

        clock.addSecondsToCurrent(60 * 60 + 1);

        manager.incEvent("EventB");
        manager.incEvent("EventA");


        assertTrue(checkStatsEqual(
                manager.getAllEventStatistic(),
                Map.of("EventA", 1.0 / 60,
                        "EventB", 1.0 / 60)
                )
        );
    }

    @Test
    public void expiredAllEventsTest() {
        CustomClock clock = new CustomClock();
        clock.setCurrentSecondsSince1970(System.currentTimeMillis() / 1000);
        EventsManager manager = new EventsManager(clock);

        manager.incEvent("EventA");
        manager.incEvent("EventA");

        clock.addSecondsToCurrent(60 * 60 + 1);

        manager.incEvent("EventB");


        assertTrue(checkStatsEqual(
                manager.getAllEventStatistic(),
                Map.of("EventB", 1.0 / 60))
        );
    }

    @Test
    public void complexExpiredEventsTest() {
        CustomClock clock = new CustomClock();
        clock.setCurrentSecondsSince1970(System.currentTimeMillis() / 1000);
        EventsManager manager = new EventsManager(clock);

        manager.incEvent("EventA");
        manager.incEvent("EventA");

        clock.addSecondsToCurrent(10 * 60);

        assertTrue(checkStatsEqual(
                manager.getAllEventStatistic(),
                Map.of("EventA", 2.0 / 60))
        );

        manager.incEvent("EventA");

        assertTrue(checkStatsEqual(
                manager.getAllEventStatistic(),
                Map.of("EventA", 3.0 / 60))
        );

        clock.addSecondsToCurrent(50 * 60 + 1);

        assertTrue(checkStatsEqual(
                manager.getAllEventStatistic(),
                Map.of("EventA", 1.0 / 60))
        );
    }
}
