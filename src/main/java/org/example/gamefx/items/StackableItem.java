package org.example.gamefx.items;

/**
 * Base class for items that can be stacked in inventory
 */
public abstract class StackableItem extends Item {
    private int count;

    /**
     * Creates stackable item with initial count
     *
     * @param type Item type identifier
     * @param count Starting stack size
     */
    public StackableItem(ItemType type, int count) {
        super(type);
        this.count = count;
    }

    /**
     * Increases stack size by 1
     */
    public void incrementCount() {
        count++;
    }

    /**
     * Decreases stack size by 1 (never goes below 0)
     */
    public void decrementCount() {
        count = Math.max(0, count - 1);
    }

    /**
     * @return Current number of items in stack
     */
    public int getCount() {
        return count;
    }

    /**
     * Adds specified amount to stack
     *
     * @param amount Number of items to add
     */
    public void addCount(int amount) {
        count += amount;
    }
}


