package model;

public class Bucket {
    private int bucketNum;
    private Itemset itemset;

    public Bucket(int bucketNum, Itemset itemset) {
        this.bucketNum = bucketNum;
        this.itemset = itemset;
    }

    public int getBucketNum() {
        return bucketNum;
    }

    public void setBucketNum(int bucketNum) {
        this.bucketNum = bucketNum;
    }

    public Itemset getItemset() {
        return itemset;
    }

    public void setItemset(Itemset itemset) {
        this.itemset = itemset;
    }
}
