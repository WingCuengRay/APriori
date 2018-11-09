package mining;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

public class DataGenerator {
    public static void main(String ...args) throws IOException {
        generateData(100, 10, 100, "test100.txt");
    }

    private static void generateData(int numOfBucket, int size, int maxValue, String outputFile) throws IOException {
        PrintWriter writer = new PrintWriter(new File(outputFile));

        for(int i=1; i<=numOfBucket; i++){
            StringBuilder builder = new StringBuilder();
            builder.append(i);
            builder.append(',');

            int[] items = new Random().ints(0, maxValue).distinct().limit(size).toArray();
            Arrays.sort(items);
            for(int item: items){
                builder.append(item);
                builder.append(',');
            }

            writer.println(builder.toString());
        }

        writer.close();
    }
}
