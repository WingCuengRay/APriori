package mining;

import model.Bucket;
import model.Itemset;
import model.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class APrior {
    private List<Integer> itemList;
    private Map<Integer, Itemset> buckets;
    private int threshold;

    public APrior(List<Integer> itemList, String fname) throws IOException {
        this.itemList = itemList;

        loadFile(fname);
    }

//    public List<Pair<Itemset, Integer>> getKItemsets(int k) {
//        List<Itemset> candidates = itemList.stream().map(item -> {
//            Itemset itemset = new Itemset(1);
//            itemset.addItem(item);
//            return itemset;
//        }).collect(Collectors.toList());
//        List<Pair<Itemset, Integer>> freqItemsets = null;
//
//        for (int i = 0; i < k; i++) {
//            // O(200,000 * 100^k * 10)
//            freqItemsets = prune(candidates);
//
//            // O(100*100)
//            freqItemsets.forEach(each -> each.getLeft().sort());
//
//            // O(100*100*k)
//            candidates = kMinus2Merge( freqItemsets.stream().map(Pair::getLeft).collect(Collectors.toList()) );
//        }
//
//        return freqItemsets;
//    }

    public List<Pair<Itemset, AtomicInteger>> getAllFreqItemsets() {
        List<Itemset> candidates = itemList.stream().map(item -> {
            Itemset itemset = new Itemset(1);
            itemset.addItem(item);
            return itemset;
        }).collect(Collectors.toList());

        int maxBucketSize = buckets.entrySet().stream().map(Map.Entry::getValue).map(Itemset::size).max(Integer::compareTo).orElse(100);
        List<Pair<Itemset, AtomicInteger>> allFreqItemset = new ArrayList<>();

        List<Pair<Itemset, AtomicInteger>> freqItemsets = null;
        for (int i = 0; i < maxBucketSize; i++) {
            // O(200,000 * 100^k * 10)
            freqItemsets = prune(candidates);

            // O(100*100)
            freqItemsets.parallelStream().forEach(each -> each.getLeft().sort());
            if(i != 0)    {
                // ignore size-1 frequent itemset
                allFreqItemset.addAll(freqItemsets);
//                freqItemsets.forEach(each -> System.out.println(each.getLeft().toString()));
            }


            // O(100*100*k)
            candidates = kMinus2Merge( freqItemsets.stream().map(Pair::getLeft).collect(Collectors.toList()) );
        }

        return allFreqItemset;
    }

    private List<Pair<Itemset, AtomicInteger>> prune(List<Itemset> candidates) {
        List<Pair<Itemset, AtomicInteger>> counters = candidates.stream().map(set -> new Pair<>(set, new AtomicInteger(0))).collect(Collectors.toList());

        buckets.entrySet().parallelStream().forEach(entry -> {
            for(Pair<Itemset, AtomicInteger> counter : counters){
                if(entry.getValue().containsAll(counter.getLeft()))
                    counter.getRight().incrementAndGet();
            }
        });

        return counters.parallelStream()
                .filter(counter -> counter.getRight().get() >= threshold)
                .collect(Collectors.toList());
    }


    private Bucket stringToBucket(String line) {
        String[] parts = line.split(",");
        if (parts.length == 0)
            return null;

        int bucketNum = Integer.parseInt(parts[0]);

        int []nums = new int[parts.length-1];
        for (int i = 1; i < parts.length; i++)
            nums[i-1] = Integer.parseInt(parts[i]);

        Arrays.sort(nums);
        Itemset itemset = new Itemset(nums);
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

                if( !Itemset.kEquals(firstSet, secondSet, k-2))
                    break;

                Itemset mergedSet = Itemset.merge(firstSet, secondSet);
                candidates.add(mergedSet);
            }
        }

        HashSet<Itemset> itemsetHashSet = new HashSet<>(freqSortedSets);
        candidates = candidates.parallelStream().filter(each -> itemsetHashSet.containsAll(each.getKMinus1Subset())).collect(Collectors.toList());

        return candidates;
    }

    private void loadFile(String fname) throws IOException {
        String line;
        String[] parts;

        buckets = new HashMap<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fname));
        line = bufferedReader.readLine();
        parts = line.split("\\s+");
        int numOfline = Integer.parseInt(parts[0]);
        threshold = Integer.parseInt(parts[1]);

        for(int i=0; i<numOfline; i++){
            line = bufferedReader.readLine();
            if(line == null)
                break;

            Bucket bucket = stringToBucket(line);
            buckets.put(bucket.getBucketNum(), bucket.getItemset());
        }

        return;
    }
}
