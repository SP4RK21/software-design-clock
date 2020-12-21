package Events;

import Clock.Clock;

import java.util.*;
import java.util.stream.Collectors;

public class EventsManager implements EventsStatistic {

    private final long MINUTES_IN_HOUR = 60;
    private final long SECONDS_IN_HOUR = MINUTES_IN_HOUR * 60;

    private Clock clock;
    private Map<String, List<Long>> events;

    public EventsManager(Clock clock) {
        this.clock = clock;
        events = new HashMap<>();
    }

    private void deleteExpiredEvents(long currentTimestamp) {
        events.replaceAll((k, v) ->
                        events.get(k).stream().
                        filter(time -> currentTimestamp - time <= SECONDS_IN_HOUR).
                        collect(Collectors.toList())
        );

        for (String key : events.keySet()) {
            List<Long> validTimestamps = events.get(key).stream().
                                         filter(time -> currentTimestamp - time <= SECONDS_IN_HOUR).
                                         collect(Collectors.toList());

            if (validTimestamps.isEmpty()) {
                events.remove(key);
            } else {
                events.put(key, validTimestamps);
            }
        }
    }

    @Override
    public void incEvent(String name) {
        long currentTimestamp = clock.secondsSince1970();
        deleteExpiredEvents(currentTimestamp);
        events.computeIfAbsent(name, k -> new ArrayList<>());
        events.get(name).add(currentTimestamp);
    }

    @Override
    public double getEventStatisticByName(String name) {
        long currentTimestamp = clock.secondsSince1970();
        deleteExpiredEvents(currentTimestamp);
        return (double)events.getOrDefault(name, Collections.emptyList()).size() / MINUTES_IN_HOUR;
    }

    @Override
    public Map<String, Double> getAllEventStatistic() {
        long currentTimestamp = clock.secondsSince1970();
        deleteExpiredEvents(currentTimestamp);
        return events.entrySet().stream().
                collect(Collectors.
                        toMap(Map.Entry::getKey, entry ->
                                (double)entry.getValue().size() / MINUTES_IN_HOUR
                        )
                );
    }

    @Override
    public void printStatistic() {
        getAllEventStatistic().forEach((key, value) -> System.out.println(key + ": " + value));
    }
}
