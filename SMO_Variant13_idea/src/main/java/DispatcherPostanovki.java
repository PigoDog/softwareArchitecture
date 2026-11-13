import java.util.List;

public class DispatcherPostanovki {
    private final Buffer buffer;
    private final List<Device> devices;
    private final StatisticsReport statistics;
    private final EventCalendar calendar;

    public DispatcherPostanovki(Buffer buffer, List<Device> devices,
                                StatisticsReport statistics, EventCalendar calendar) {
        this.buffer = buffer;
        this.devices = devices;
        this.statistics = statistics;
        this.calendar = calendar;
    }

    public void onRequestArrived(Request request, double now) {
        calendar.logEvent(now, "Заявка " + request.label() + " прибыла");
        Device device = findFreeDeviceByNumber();
        if (device != null) {                                   // HappyFlow
            device.startService(request, now);
            statistics.recordArrival();
            statistics.recordServiceCompletion(0);
            calendar.schedule(new Event(device.getFinishTime(), "DEVICE_FREE", device.getId(),
                "Бокс " + device.getId() + " освободится (заявка " + request.label() + ")"));
            calendar.logEvent(now, "Д2П1: заявка " + request.label() + " -> бокс " + device.getId());
        } else if (buffer.hasFreeSlot()) {                      // Д1ОЗ3
            buffer.placeRequest(request);
            statistics.recordArrival();
            calendar.logEvent(now, "Д1ОЗ3: заявка " + request.label()
                + " поставлена в буфер, место " + (request.getBufferSlotIndex() + 1));
        } else {
            handleOverflow(request, now);                       // Д1ОО5
        }
    }

    private Device findFreeDeviceByNumber() {
        for (Device d : devices) if (d.isFree()) return d;
        return null;
    }

    private void handleOverflow(Request request, double now) {
        request.setStatus(RequestStatus.REJECTED);
        statistics.recordRejection();
        calendar.logEvent(now, "Д1ОО5: ОТКАЗ заявке " + request.label() + " (боксы и буфер заняты)");
    }
}
