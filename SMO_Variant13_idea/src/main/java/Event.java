public class Event implements Comparable<Event> {
    private static int counter = 0;
    private final int order = counter++;
    private final double time;
    private final String type;
    private final int targetId;        // номер источника или прибора
    private final String description;

    public Event(double time, String type, int targetId, String description) {
        this.time = time;
        this.type = type;
        this.targetId = targetId;
        this.description = description;
    }

    public double getTime() { return time; }
    public String getType() { return type; }
    public int getTargetId() { return targetId; }
    public String getDescription() { return description; }

    @Override
    public int compareTo(Event o) {
        int c = Double.compare(time, o.time);
        return c != 0 ? c : Integer.compare(order, o.order);
    }
}
