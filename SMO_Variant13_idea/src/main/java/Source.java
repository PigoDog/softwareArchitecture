import java.util.Random;

public class Source {
    private final int id;
    private final int priority;        // приоритет = номер источника
    private final double intervalMin;
    private final double intervalMax;
    private final Random rng;
    private int generated = 0;

    public Source(int id, double intervalMin, double intervalMax, Random rng) {
        this.id = id;
        this.priority = id;
        this.intervalMin = intervalMin;
        this.intervalMax = intervalMax;
        this.rng = rng;
    }

    public double generateNextArrival() {
        return intervalMin + rng.nextDouble() * (intervalMax - intervalMin);
    }

    public Request createRequest(double now) {
        generated++;
        return new Request(generated, id, priority, now);
    }

    public int getId() { return id; }
    public int getPriority() { return priority; }
}
