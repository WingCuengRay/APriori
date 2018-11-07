package model;

import java.util.Objects;

public class Item implements Comparable<Item> {
    private int itemID;

    public Item(int id) {
        this.itemID = id;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return itemID == item.itemID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemID);
    }

    @Override
    public int compareTo(Item o) {
        return Integer.compare(this.itemID, o.itemID);
    }
}
