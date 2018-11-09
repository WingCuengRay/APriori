package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Itemset {
    private List<Item> itemList;

    public Itemset(){
        itemList = new ArrayList<>();
    }

    public Itemset(List<Item> itemList) {
        this.itemList = itemList;
    }

    public void addItem(Item item){
        itemList.add(item);
    }

    public boolean containsAll(Itemset right){
        return itemList.containsAll(right.itemList);
    }

    public int size(){
        return itemList.size();
    }

    public List<Item> getFirstKItems(int k){
        if(k > itemList.size())
            return null;

        return itemList.subList(0, k);
    }

    public void sort(){
        itemList.sort(Comparator.comparing(Item::getItemID));
    }

    public static Itemset merge(Itemset itemset1, Itemset itemset2) {
        List<Item> mergedList = Stream.of(itemset1, itemset2)
                .flatMap(each -> each.itemList.stream())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        return new Itemset(mergedList);
    }



    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> itemset = itemList.stream().map(Item::getItemID).map(String::valueOf).collect(Collectors.toList());

        stringBuilder.append("{");
        stringBuilder.append(String.join(",", itemset));
        stringBuilder.append("}");

        return stringBuilder.toString();
    }
}
