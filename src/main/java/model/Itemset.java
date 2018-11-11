package model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Itemset {
    private int[] itemList;
    private int size;

    public Itemset(int n) {
        itemList = new int[n];
        size = 0;
    }

    public Itemset(int[] itemList) {
        this.itemList = itemList;
        size = itemList.length;
    }

    public void addItem(int item) {
        itemList[size++] = item;
    }

    // two sorted list
    public boolean containsAll(Itemset right) {
        int[] llist = this.itemList;
        int[] rlist = right.itemList;
        int i = 0;
        int j = 0;

        while(i < this.size && j < right.size){
            while(i < this.size && llist[i] < rlist[j])
                i++;

            if(i >= this.size || llist[i] != rlist[j])
                return false;

            i++;
            j++;
        }

        if(j != right.size)
            return false;
        else
            return true;
    }

    public int size() {
        return size;
    }

    public int[] getFirstKItems(int k) {
        if (k > size)
            return null;

        return Arrays.copyOfRange(itemList, 0, k);
    }

    public void sort() {
        Arrays.sort(itemList);
    }

    public static Itemset merge(Itemset itemset1, Itemset itemset2) {
        int[] merged = Stream.of(itemset1, itemset2)
                .flatMapToInt(each -> IntStream.of(each.itemList))
                .sorted()
                .distinct()
                .toArray();

        Itemset itemset = new Itemset(merged);
        return itemset;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> itemset = IntStream.of(itemList).mapToObj(each -> String.valueOf(each)).collect(Collectors.toList());

        stringBuilder.append("{");
        stringBuilder.append(String.join(",", itemset));
        stringBuilder.append("}");

        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Itemset itemset = (Itemset) o;
        return size == itemset.size &&
                Arrays.equals(itemList, itemset.itemList);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(size);
        result = 31 * result + Arrays.hashCode(itemList);
        return result;
    }
}
