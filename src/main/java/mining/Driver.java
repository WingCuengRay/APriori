package mining;

import model.Item;
import model.Itemset;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Driver {
    public static void main(String[] args) throws IOException {
        testSmallSizeDataset();
        testMediumSizeDataset();
    }

    private static void testSmallSizeDataset() throws IOException {
        List<Item> itemList = IntStream.range(0, 10).boxed().map(Item::new).collect(Collectors.toList());

        APrior aPrior = new APrior(itemList, "test5.txt", 3);
        List<Itemset> itemsets = aPrior.getKItemsets(3);

        for(Itemset each: itemsets)
            System.out.println(each.toString());
        System.out.println("--------------------------------------------------\n");
    }

    private static void testMediumSizeDataset() throws IOException {
        List<Item> itemList = IntStream.range(0, 100).boxed().map(Item::new).collect(Collectors.toList());

        APrior aPrior = new APrior(itemList, "test100.txt", 2);
        List<Itemset> itemsets = aPrior.getKItemsets(4);

        for(Itemset each: itemsets)
            System.out.println(each.toString());
        System.out.println("--------------------------------------------------\n");
    }
}
