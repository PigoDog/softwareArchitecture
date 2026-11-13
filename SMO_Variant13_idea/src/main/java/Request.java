public class Request {
    private final int id;
    private final int sourceId;
    private final int sourcePriority;
    private final double arrivalTime;
    private RequestStatus status;
    private int bufferSlotIndex = -1;

    public Request(int id, int sourceId, int sourcePriority, double arrivalTime) {
        this.id = id;
        this.sourceId = sourceId;
        this.sourcePriority = sourcePriority;
        this.arrivalTime = arrivalTime;
        this.status = RequestStatus.WAITING;
    }

    public int getId() { return id; }
    public int getSourceId() { return sourceId; }
    public int getPriority() { return sourcePriority; }
    public double getArrivalTime() { return arrivalTime; }
    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }
    public int getBufferSlotIndex() { return bufferSlotIndex; }
    public void setBufferSlotIndex(int index) { this.bufferSlotIndex = index; }
    public String label() { return "(" + sourceId + "." + id + ")"; }
}
