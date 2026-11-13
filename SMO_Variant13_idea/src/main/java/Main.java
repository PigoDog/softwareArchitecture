import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        boolean step = args.length == 0 || !args[0].equals("auto");
        double time = args.length > 1 ? Double.parseDouble(args[1]) : 120;
        int devices = args.length > 2 ? Integer.parseInt(args[2]) : 2;
        int buffer = args.length > 3 ? Integer.parseInt(args[3]) : 3;
        long seed = args.length > 4 ? Long.parseLong(args[4]) : 42;
        new SimulationController(devices, buffer, 9.0, seed, step).run(time);
    }
}

