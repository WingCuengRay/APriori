package mining;

import model.Item;
import model.Itemset;
import model.Pair;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Driver {
    public static void main(String[] args) throws IOException {
//        testSmallSizeDataset();

//        testMediumSizeDataset();
        testLargeSizeDataset();
    }

    private static void testSmallSizeDataset() throws IOException {
        List<Item> itemList = IntStream.range(0, 10).boxed().map(Item::new).collect(Collectors.toList());

        APrior aPrior = new APrior(itemList, "test5.txt", 2);
        List<Pair<Itemset, Integer>> itemsets = aPrior.getKItemsets(3);

        for(Pair<Itemset, Integer> each: itemsets)
            System.out.println(each.getLeft() + "-" + each.getRight());
        System.out.println("--------------------------------------------------\n");
    }

    private static void testMediumSizeDataset() throws IOException {
        long start = System.nanoTime();

        List<Item> itemList = IntStream.range(0, 100).boxed().map(Item::new).collect(Collectors.toList());

        APrior aPrior = new APrior(itemList, "test100.txt", 2);
        List<Pair<Itemset, Integer>> itemsets = aPrior.getKItemsets(4);

        long end = System.nanoTime();
        System.out.println("Time: " + (end-start)/1000/1000 + "ms");

        for(Pair<Itemset, Integer> each: itemsets)
            System.out.println(each.getLeft() + "-" + each.getRight());
        System.out.println("--------------------------------------------------\n");
    }

    private static void testLargeSizeDataset() throws IOException {
        long start = System.nanoTime();

        List<Item> itemList = IntStream.range(0, 100).boxed().map(Item::new).collect(Collectors.toList());

        APrior aPrior = new APrior(itemList, "test200000.txt", 1500);
        List<Pair<Itemset, Integer>> itemsets = aPrior.getKItemsets(3);

        long end = System.nanoTime();
        System.out.println("Time: " + (end-start)/1000/1000 + "ms");

        for(Pair<Itemset, Integer> each: itemsets)
            System.out.println(each.getLeft() + "-" + each.getRight());
        System.out.println("--------------------------------------------------\n");
    }
}
