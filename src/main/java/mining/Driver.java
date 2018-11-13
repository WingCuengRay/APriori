package mining;

import model.Itemset;
import model.Pair;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Driver {
    public static void main(String[] args) throws IOException {
        List<Integer> itemList = IntStream.range(0, 100).boxed().collect(Collectors.toList());

        long start = System.nanoTime();
        APrior aPrior = new APrior(itemList, "test200000.txt");
        List<Pair<Itemset, Integer>> allFreqItemsets = aPrior.getAllFreqItemsets();

        long end = System.nanoTime();
        System.out.println("Time: " + (end - start) / 1000 / 1000 + "ms");
        for (Pair<Itemset, Integer> each : allFreqItemsets)
            System.out.println(each.getLeft() + "-" + each.getRight());


//        testSmallSizeDataset();
//        testMediumSizeDataset();
//        testLargeSizeDataset();
    }

    private static void testSmallSizeDataset() throws IOException {
        long start = System.nanoTime();
        List<Integer> itemList = IntStream.range(0, 10).boxed().collect(Collectors.toList());

        APrior aPrior = new APrior(itemList, "test5.txt");
        List<Pair<Itemset, Integer>> itemsets = aPrior.getKItemsets(3);
        long end = System.nanoTime();
        System.out.println("Time: " + (end - start) / 1000 / 1000 + "ms");

        for (Pair<Itemset, Integer> each : itemsets)
            System.out.println(each.getLeft() + "-" + each.getRight());
        System.out.println("--------------------------------------------------\n");
    }

    private static void testMediumSizeDataset() throws IOException {
        long start = System.nanoTime();

        List<Integer> itemList = IntStream.range(0, 100).boxed().collect(Collectors.toList());

        APrior aPrior = new APrior(itemList, "test100.txt");
        List<Pair<Itemset, Integer>> itemsets = aPrior.getKItemsets(3);

        long end = System.nanoTime();
        System.out.println("Time: " + (end - start) / 1000 / 1000 + "ms");

        for (Pair<Itemset, Integer> each : itemsets)
            System.out.println(each.getLeft().toString() + "-" + each.getRight());
        System.out.println("--------------------------------------------------\n");
    }

    private static void testLargeSizeDataset() throws IOException {
        long start = System.nanoTime();

        List<Integer> itemList = IntStream.range(0, 100).boxed().collect(Collectors.toList());

        APrior aPrior = new APrior(itemList, "test200000.txt");
        List<Pair<Itemset, Integer>> itemsets = aPrior.getKItemsets(2);

        long end = System.nanoTime();
        System.out.println("Time: " + (end - start) / 1000 / 1000 + "ms");

        for (Pair<Itemset, Integer> each : itemsets)
            System.out.println(each.getLeft() + "-" + each.getRight());
        System.out.println("--------------------------------------------------\n");
    }
}
