package model;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility class to provide unique, sequential IDs for Library Items and Users.
 * Uses AtomicInteger to ensure thread safety if the system expands.
 */
public class IDGenerator {
    // Starting counters for different categories
    private static final AtomicInteger itemCounter = new AtomicInteger(1000);
    private static final AtomicInteger userCounter = new AtomicInteger(500);

    /**
     * Generates a unique ID for books/media (e.g., ITEM-1001)
     */
    public static String generateItemID() {
        return "ITEM-" + itemCounter.incrementAndGet();
    }

    /**
     * Generates a unique ID for library members (e.g., USER-501)
     */
    public static String generateUserID() {
        return "USER-" + userCounter.incrementAndGet();
    }

    /**
     * Optional: Reset counters (Useful for unit testing)
     */
    public static void reset() {
        itemCounter.set(1000);
        userCounter.set(500);
    }
}