package mining;

import model.Bucket;
import model.Item;
import model.Itemset;
import model.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class APrior {
    private List<Item> itemList;
    private Map<Integer, Itemset> buckets;
    private int threshold;

    public APrior(List<Item> itemList, String fname, int threshold) throws IOException {
        this.itemList = itemList;;
        this.threshold = threshold;

        buckets = new HashMap<>();
        Files.lines(Paths.get(fname)).map(line -> stringToBucket(line)).forEach(bucket -> buckets.put(bucket.getBucketNum(), bucket.getItemset()));
    }

    public List<Pair<Itemset, Integer>> getKItemsets(int k) throws IOException {
        List<Itemset> candidates = itemList.stream().map(item -> {
            Itemset itemset = new Itemset();
            itemset.addItem(item);
            return itemset;
        }).collect(Collectors.toList());
        List<Pair<Itemset, Integer>> freqItemsets = null;

        for (int i = 0; i < k; i++) {
            // O(200,000 * 100^k * 10)
            freqItemsets = prune(candidates);

            // O(100*100)
            freqItemsets.forEach(each -> each.getLeft().sort());

            // O(100*100*k)
            candidates = kMinus2Merge( freqItemsets.stream().map(Pair::getLeft).collect(Collectors.toList()) );
        }

        return freqItemsets;
    }

    private List<Pair<Itemset, Integer>> prune(List<Itemset> candidates) throws IOException {
        List<Pair<Itemset, Integer>> counters = candidates.stream().map(set -> new Pair<>(set, 0)).collect(Collectors.toList());

        for(Map.Entry<Integer, Itemset> entry : buckets.entrySet()){
            for(Pair<Itemset, Integer> counter : counters){
                if(entry.getValue().containsAll(counter.getLeft()))
                    counter.setRight(counter.getRight()+1);
            }
        }

//        List<Itemset> notFreqItemset = counters.stream().filter(counter -> counter.getRight() <= threshold)
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

    // O(100*100*k)
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
