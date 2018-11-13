package mining;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataGenerator {
    public static void main(String ...args) throws IOException {
        generateData(100, 10, 100, "test100.txt");
    }

    private static void generateData(int numOfBucket, int size, int maxValue, String outputFile) throws IOException {
        PrintWriter writer = new PrintWriter(new File(outputFile));

        for(int i=1; i<=numOfBucket; i++){
            int[] items = new Random().ints(0, maxValue).distinct().limit(size).toArray();
            String line = String.join(",", IntStream.of(items).mapToObj(String::valueOf).collect(Collectors.toList()));

            writer.println(i+","+line);
        }

        writer.close();
    }
}
