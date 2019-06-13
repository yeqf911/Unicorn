package com.edu.unicorn.ui.main.dummy;

import com.edu.unicorn.db.SqliteHelper;
import com.edu.unicorn.entity.Bill;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Bill> ITEMS = new ArrayList<Bill>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Bill> ITEM_MAP = new HashMap<String, Bill>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(Bill item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getType(), item);
    }

    private static Bill createDummyItem(int position) {
        return new Bill(Bill.TYPE_FOOD, 16, 0, new Date(), Bill.WAY_CASH, "烧烤" + position);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }
}
