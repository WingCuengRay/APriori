package mining;

import model.Bucket;
import model.Item;
import model.Itemset;
import model.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class APrior {
    private List<Item> itemList;
    private String fname;
    private int threshold;

    public APrior(List<Item> itemList, String fname, int threshold) {
        this.itemList = itemList;
        this.fname = fname;
        this.threshold = threshold;
    }

    public List<Pair<Itemset, Integer>> getKItemsets(int k) throws IOException {
        List<Itemset> candidates = itemList.stream().map(item -> {
            Itemset itemset = new Itemset();
            itemset.addItem(item);
            return itemset;
        }).collect(Collectors.toList());
        List<Pair<Itemset, Integer>> freqItemsets = null;

        for (int i = 0; i < k; i++) {
            freqItemsets = prune(candidates);
            freqItemsets.forEach(each -> each.getLeft().sort());

            candidates = kMinus2Merge( freqItemsets.stream().map(Pair::getLeft).collect(Collectors.toList()) );
        }

        return freqItemsets;
    }

    private List<Pair<Itemset, Integer>> prune(List<Itemset> candidates) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fname));
        List<Pair<Itemset, Integer>> counters = candidates.stream().map(set -> new Pair<>(set, 0)).collect(Collectors.toList());

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            Bucket bucket = stringToBucket(line);
            if(bucket == null)
                continue;

            for (Pair<Itemset, Integer> counter : counters) {
                if (bucket.getItemset().containsAll(counter.getLeft()))
                    counter.setRight(counter.getRight() + 1);
            }
        }

        bufferedReader.close();

        return counters.stream()
                .filter(counter -> counter.getRight() >= threshold)
                .collect(Collectors.toList());
    }


    private Bucket stringToBucket(String line) {
        String[] parts = line.split(",");
        if (parts.length == 0)
            return null;

        int bucketNum = Integer.parseInt(parts[0]);

        Itemset itemset = new Itemset();
        for (int i = 1; i < parts.length; i++) {
            Item item = new Item(Integer.parseInt(parts[i]));
            itemset.addItem(item);
        }

        return new Bucket(bucketNum, itemset);
    }

    private List<Itemset> kMinus2Merge(List<Itemset> freqSortedSets) {
        if (freqSortedSets.isEmpty())
            return Collections.emptyList();

        int k = freqSortedSets.get(0).size() + 1;

        List<Itemset> candidates = new ArrayList<>();
        for (int i = 0; i < freqSortedSets.size(); i++) {
            for (int j = i + 1; j < freqSortedSets.size(); j++) {
                Itemset firstSet = freqSortedSets.get(i);
                Itemset secondSet = freqSortedSets.get(j);

                if (firstSet.getFirstKItems(k - 2).equals(secondSet.getFirstKItems(k - 2))) {
                    Itemset mergedSet = Itemset.merge(firstSet, secondSet);
                    candidates.add(mergedSet);
                }
            }
        }

        return candidates;
    }
}
