import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class SimulationController {
    private final List<Source> sources = new ArrayList<>();
    private final Buffer buffer;
    private final List<Device> devices = new ArrayList<>();
    private final DispatcherPostanovki dispatcherPostanovki;
    private final DispatcherVybora dispatcherVybora;
    private final EventCalendar calendar = new EventCalendar();
    private final StatisticsReport statistics = new StatisticsReport();
    private final boolean stepMode;

    public SimulationController(int deviceCount, int bufferSize, double meanService,
                                long seed, boolean stepMode) {
        Random rng = new Random(seed);
        // источники: чем меньше номер, тем выше приоритет (мин, макс интервала между заявками)
        double[][] intervals = {{8, 16}, {10, 20}, {15, 25}};
        for (int i = 0; i < intervals.length; i++) {
            sources.add(new Source(i + 1, intervals[i][0], intervals[i][1], rng));
        }
        for (int i = 1; i <= deviceCount; i++) devices.add(new Device(i, meanService, rng));
        buffer = new Buffer(bufferSize);
        dispatcherPostanovki = new DispatcherPostanovki(buffer, devices, statistics, calendar);
        dispatcherVybora = new DispatcherVybora(buffer, statistics, calendar, sources.size());
        this.stepMode = stepMode;
    }

    public void run(double simulationTime) {
        Scanner in = new Scanner(System.in);
        for (Source s : sources) scheduleArrival(s, 0);
        calendar.schedule(new Event(simulationTime, "END", 0, "Окончание моделирования"));

        int step = 0;
        double now = 0;
        while (true) {
            Event e = calendar.pollNext();
            now = e.getTime();
            step++;
            if (stepMode) System.out.println("\n--- Шаг " + step + ": " + e.getDescription() + " ---");

            if (e.getType().equals("ARRIVAL")) {
                Source s = sources.get(e.getTargetId() - 1);
                dispatcherPostanovki.onRequestArrived(s.createRequest(now), now);
                scheduleArrival(s, now);
            } else if (e.getType().equals("DEVICE_FREE")) {
                Device d = devices.get(e.getTargetId() - 1);
                Request done = d.completeService();
                calendar.logEvent(now, "Бокс " + d.getId() + " освободился, заявка " + done.label() + " обслужена");
                dispatcherVybora.onDeviceFreed(d, now);
            }
            if (stepMode) {
                printState(now);
                if (e.getType().equals("END")) break;
                System.out.print("[Enter — следующий шаг] ");
                if (in.hasNextLine()) in.nextLine();
            } else if (e.getType().equals("END")) break;
        }
        statistics.printReport(now, devices);
    }

    private void scheduleArrival(Source s, double now) {
        double t = now + s.generateNextArrival();
        calendar.schedule(new Event(t, "ARRIVAL", s.getId(), "Заявка от источника " + s.getId()));
    }

    private void printState(double now) {
        System.out.println(String.format("Модельное время: %.2f", now));
        calendar.printJournal();
        calendar.printCalendar();
        System.out.println("  Буфер:   " + buffer.describe());
        for (Device d : devices) System.out.println("  " + d.describe());
    }
}
