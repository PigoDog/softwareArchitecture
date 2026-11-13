import java.util.ArrayList;
import java.util.List;

public class Buffer {
    private final int capacity;
    private final Request[] slots;

    public Buffer(int capacity) {
        this.capacity = capacity;
        this.slots = new Request[capacity];
    }

    public boolean hasFreeSlot() {
        for (Request r : slots) if (r == null) return true;
        return false;
    }

    public boolean isFull() { return !hasFreeSlot(); }

    public boolean placeRequest(Request request) {
        for (int i = 0; i < capacity; i++) {
            if (slots[i] == null) {
                slots[i] = request;
                request.setBufferSlotIndex(i);
                request.setStatus(RequestStatus.WAITING);
                return true;
            }
        }
        return false;
    }

    public void removeRequest(Request request) {
        slots[request.getBufferSlotIndex()] = null;
        request.setBufferSlotIndex(-1);
    }

    public List<Request> getWaitingBySource(int priority) {
        List<Request> result = new ArrayList<>();
        for (Request r : slots) if (r != null && r.getPriority() == priority) result.add(r);
        return result;
    }

    public String describe() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < capacity; i++) {
            sb.append("[").append(i + 1).append(": ")
              .append(slots[i] == null ? "--" : slots[i].label()).append("] ");
        }
        return sb.toString().trim();
    }
}
