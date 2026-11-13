import java.util.List;

public class StatisticsReport {
    private int totalArrived = 0;
    private int totalServed = 0;
    private int totalRejected = 0;
    private double totalWaitTime = 0;

    public void recordArrival() { totalArrived++; }

    public void recordRejection() { totalArrived++; totalRejected++; }

    public void recordServiceCompletion(double waitTime) { totalServed++; totalWaitTime += waitTime; }

    public void printReport(double totalTime, List<Device> devices) {
        System.out.println("\n=== ОР1: сводная таблица результатов ===");
        System.out.println("Время моделирования:        " + String.format("%.2f", totalTime));
        System.out.println("Сгенерировано заявок:       " + totalArrived);
        System.out.println("Принято на обслуживание:    " + totalServed);
        System.out.println("Отказов:                    " + totalRejected);
        System.out.println("Вероятность отказа:         "
            + String.format("%.3f", totalArrived == 0 ? 0 : (double) totalRejected / totalArrived));
        System.out.println("Среднее ожидание в буфере:  "
            + String.format("%.2f", totalServed == 0 ? 0 : totalWaitTime / totalServed));
        for (Device d : devices) {
            System.out.println("Загрузка бокса " + d.getId() + ":        "
                + String.format("%.3f", d.getBusyTime(totalTime) / totalTime));
        }
    }
}
