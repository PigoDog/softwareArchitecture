import java.util.List;

public class DispatcherVybora {
    private final Buffer buffer;
    private final StatisticsReport statistics;
    private final EventCalendar calendar;
    private final int sourceCount;

    public DispatcherVybora(Buffer buffer, StatisticsReport statistics,
                            EventCalendar calendar, int sourceCount) {
        this.buffer = buffer;
        this.statistics = statistics;
        this.calendar = calendar;
        this.sourceCount = sourceCount;
    }

    public void onDeviceFreed(Device device, double now) {
        Request selected = selectBySourcePriority();
        if (selected == null) {
            calendar.logEvent(now, "Буфер пуст, бокс " + device.getId() + " простаивает");
            return;
        }
        buffer.removeRequest(selected);
        device.startService(selected, now);
        statistics.recordServiceCompletion(now - selected.getArrivalTime());
        calendar.schedule(new Event(device.getFinishTime(), "DEVICE_FREE", device.getId(),
            "Бокс " + device.getId() + " освободится (заявка " + selected.label() + ")"));
        calendar.logEvent(now, "Д2Б4: заявка " + selected.label() + " из буфера -> бокс " + device.getId());
    }

    private Request selectBySourcePriority() {
        for (int p = 1; p <= sourceCount; p++) {
            List<Request> waiting = buffer.getWaitingBySource(p);
            if (waiting.isEmpty()) continue;
            Request earliest = waiting.get(0);
            for (Request r : waiting) if (r.getArrivalTime() < earliest.getArrivalTime()) earliest = r;
            return earliest;
        }
        return null;
    }
}
