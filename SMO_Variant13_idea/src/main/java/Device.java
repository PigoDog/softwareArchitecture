import java.util.Random;

public class Device {
    private final int id;
    private final double meanServiceTime;
    private final Random rng;
    private DeviceStatus status = DeviceStatus.FREE;
    private Request currentRequest;
    private double startTime;
    private double finishTime;
    private double busyTime = 0;

    public Device(int id, double meanServiceTime, Random rng) {
        this.id = id;
        this.meanServiceTime = meanServiceTime;
        this.rng = rng;
    }

    public boolean isFree() { return status == DeviceStatus.FREE; }

    public void startService(Request request, double now) {
        currentRequest = request;
        status = DeviceStatus.BUSY;
        request.setStatus(RequestStatus.IN_SERVICE);
        startTime = now;
        finishTime = now - meanServiceTime * Math.log(1 - rng.nextDouble());
    }

    public Request completeService() {
        Request done = currentRequest;
        busyTime += finishTime - startTime;
        currentRequest = null;
        status = DeviceStatus.FREE;
        done.setStatus(RequestStatus.COMPLETED);
        return done;
    }

    public int getId() { return id; }
    public double getFinishTime() { return finishTime; }

    public double getBusyTime(double now) {
        return busyTime + (status == DeviceStatus.BUSY ? now - startTime : 0);
    }

    public String describe() {
        return "Бокс " + id + ": " + (isFree() ? "свободен"
            : "занят " + currentRequest.label() + " до t=" + String.format("%.2f", finishTime));
    }
}
