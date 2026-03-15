package controller;
import model.LibraryItem;
import java.util.*;

public class SearchEngine {
    
    // Requirement 4.1: Linear Search (Good for unsorted lists or partial matches)
    public static LibraryItem linearSearch(List<LibraryItem> inventory, String query) {
        for (LibraryItem item : inventory) {
            // Check if the title contains the query (case-insensitive)
            if (item.getTitle().toLowerCase().contains(query.toLowerCase())) {
                return item;
            }
        }
        return null; // Not found
    }

    // Requirement 5.2: Selection Sort (O(n^2) - Sorts by Publication Year)
    public static void selectionSortByYear(List<LibraryItem> inventory) {
        int n = inventory.size();
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                // Assuming LibraryItem has a getYear() method
                if (inventory.get(j).getYear() < inventory.get(minIdx).getYear()) {
                    minIdx = j;
                }
            }
            // Swap the found minimum element with the first element
            LibraryItem temp = inventory.get(minIdx);
            inventory.set(minIdx, inventory.get(i));
            inventory.set(i, temp);
        }
    }

    public static void mergeSort(List<LibraryItem> inventory) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mergeSort'");
    }

    public static int binarySearchByTitle(List<LibraryItem> inventory, String query) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'binarySearchByTitle'");
    }

    // ... [Your existing binarySearchByTitle and mergeSort methods go here] ...
}