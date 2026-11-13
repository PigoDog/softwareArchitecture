import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class EventCalendar {
    private final PriorityQueue<Event> events = new PriorityQueue<>();
    private final List<String> journal = new ArrayList<>();

    public void schedule(Event event) { events.add(event); }

    public Event pollNext() { return events.poll(); }

    public void logEvent(double time, String description) {
        journal.add(String.format("t=%.2f  %s", time, description));
    }

    public void printJournal() {
        for (String line : journal) System.out.println("  " + line);
        journal.clear();
    }

    public void printCalendar() {
        System.out.println("  Календарь событий (ближайшие):");
        List<Event> sorted = new ArrayList<>(events);
        sorted.sort(null);
        for (Event e : sorted) {
            System.out.println(String.format("    t=%.2f  %s", e.getTime(), e.getDescription()));
        }
    }
}
