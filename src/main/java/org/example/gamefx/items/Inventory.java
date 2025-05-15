package org.example.gamefx.items;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.example.gamefx.World;
import org.example.gamefx.entities.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores and manages player's items with visual representation
 */
public class Inventory {
    private List<Item> items = new ArrayList<>();
    private int capacity;
    private int selectedSlot = 0;
    private int width;
    private int height;
    private World world;

    /**
     * Creates inventory with fixed capacity
     *
     * @param capacity Maximum number of distinct item slots
     * @param world Reference to game world for positioning
     */
    public Inventory(int capacity, World world) {
        this.capacity = capacity;
        this.world = world;
    }

    /**
     * Attempts to add item to inventory with stacking logic
     *
     * @param newItem Item to add
     * @return True if item was successfully added/stacked
     */
    public boolean addItem(Item newItem) {
        if (newItem instanceof StackableItem) {
            for (Item existingItem : items) {
                if (existingItem.getClass() == newItem.getClass()) {
                    ((StackableItem) existingItem).addCount(((StackableItem) newItem).getCount());
                    return true;
                }
            }
        }

        if (items.size() < capacity) {
            items.add(newItem);
            return true;
        }
        return false;
    }

    public void useItem(int slot, Player player) {
        if (slot < 0 || slot >= items.size()) return;

        Item item = items.get(slot);
        item.use(player);
    }

    /**
     * Renders inventory UI with slots and items
     *
     * @param gc Graphics context for drawing
     */
    public void render(GraphicsContext gc) {
        int slotSize = 64;
        int startX =  (world.getWidth() /2 - capacity* slotSize/2);  // Pravý dolní roh
        int startY = world.getHeight() - 64;

        int padding = 2;
        for (int i = 0; i < capacity; i++) {

            gc.setFill(Color.WHITE);
            gc.rect(startX + i * (slotSize + padding), startY, slotSize, slotSize);

            gc.setStroke(Color.BLACK);
            gc.setLineWidth(3);
            gc.strokeRect(startX + i * (slotSize + padding), startY, slotSize, slotSize);

            if (i == selectedSlot) {
                gc.setStroke(Color.YELLOW);
                gc.setLineWidth(3);
                gc.strokeRect(startX + i * (slotSize + padding), startY, slotSize, slotSize);
            }
        }

        for(int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            gc.drawImage(
                    item.getImg(),
                    startX + (i * slotSize + padding),
                    startY,
                    slotSize,
                    slotSize
            );
            gc.setFill(Color.WHITE);

            if (item instanceof StackableItem) {
                gc.fillText(
                        String.valueOf(((StackableItem) item).getCount()),
                        startX + i * (slotSize + padding) + slotSize - 15,
                        startY + slotSize - 5
                );
            }
        }
    }

    /**
     * @return Maximum number of item slots
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Changes currently selected inventory slot
     * @param slot New slot index (0-based)
     */
    public void setSelectedSlot(int slot) {
        if (slot >= 0 && slot < capacity) {
            selectedSlot = slot;
        } else {
            selectedSlot = 0;
        }
    }

    /**
     * @param slot Slot index to check
     * @return Item in specified slot or null
     */
    public Item getItem(int slot) {
        if (slot >= 0 && slot < items.size()) {
            return items.get(slot);
        }
        return null;
    }

    public int getSelectedSlot() {
        return this.selectedSlot;
    }

    public List<Item> getItems() {
        return items;
    }

    public boolean hasItem(ItemType type) {
        return items.stream().anyMatch(item -> item.getType() == type);
    }

    /**
     * Removes/decrements first found item of specified type
     * @param type Type of item to remove
     */
    public void removeFirstItemOfType(ItemType type) {
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item.getType() == type) {

                if (item instanceof StackableItem) {
                    StackableItem stackable = (StackableItem) item;
                    stackable.decrementCount();
                    if (stackable.getCount() <= 0) {
                        items.remove(i);
                    }
                } else {
                    items.remove(i);
                }
                return;
            }
        }
    }
}

