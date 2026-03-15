package controller;

import model.*;
import java.util.*;
import java.io.*;

public class LibraryManager {
    // Requirements 3.1 - 3.4
    private List<LibraryItem> inventory = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private Map<String, Queue<String>> reservations = new HashMap<>(); 
    private Stack<String> undoStack = new Stack<>(); 
    private LibraryItem[] mostFrequentCache = new LibraryItem[5]; 

    public LibraryManager() {
        // Sample Data for testing
        inventory.add(new Book("B001", "Java Programming", "Deitel", 2024, "Education"));
        inventory.add(new Book("B002", "Data Structures", "Weiss", 2023, "Tech"));
        inventory.add(new Book("B003", "Algorithms", "Sedgewick", 2022, "Tech"));
        inventory.add(new Magazine("M001", "Tech Monthly", "Tech Publisher", 2024, "Technology"));
        inventory.add(new Magazine("M002", "Science Weekly", "Science Publisher", 2023, "Science"));
        inventory.add(new Journal("J001", "AI Research", "AI Institute", 2024, "Science"));
        inventory.add(new Journal("J002", "Quantum Computing", "Quantum Labs", 2023, "Science"));
    }

    // --- Search Algorithms (Requirement 4 & 6) ---

    public LibraryItem performSearch(String query, String method) {
        if (query == null || query.isEmpty()) return null;
        
        LibraryItem result = null;
        switch (method) {
            case "Recursive Search":
                result = recursiveSearchByTitle(inventory, query, 0);
                break;
            case "Binary Search":
                // Must sort before binary search
                sortInventory("Title", "QuickSort");
                result = binarySearchByTitle(inventory, query);
                break;
            default: // Linear
                result = inventory.stream()
                        .filter(i -> i.getTitle().equalsIgnoreCase(query))
                        .findFirst().orElse(null);
        }
        if (result != null) updateCache(result);
        return result;
    }

    private LibraryItem recursiveSearchByTitle(List<LibraryItem> items, String query, int index) {
        if (index >= items.size()) return null;
        if (items.get(index).getTitle().equalsIgnoreCase(query)) return items.get(index);
        return recursiveSearchByTitle(items, query, index + 1);
    }

    private LibraryItem binarySearchByTitle(List<LibraryItem> items, String query) {
        int low = 0, high = items.size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            int res = query.compareToIgnoreCase(items.get(mid).getTitle());
            if (res == 0) return items.get(mid);
            if (res > 0) low = mid + 1;
            else high = mid - 1;
        }
        return null;
    }

    // --- Sorting Algorithms (Requirement 5) ---

    public void sortInventory(String criteria, String algorithm) {
        if (algorithm.equals("QuickSort")) {
            quickSort(inventory, 0, inventory.size() - 1, criteria);
        } else {
            // Fallback to standard sort for simplicity in SelectionSort example
            inventory.sort((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));
        }
    }

    private void quickSort(List<LibraryItem> items, int low, int high, String criteria) {
        if (low < high) {
            int pi = partition(items, low, high, criteria);
            quickSort(items, low, pi - 1, criteria);
            quickSort(items, pi + 1, high, criteria);
        }
    }

    private int partition(List<LibraryItem> items, int low, int high, String criteria) {
        LibraryItem pivot = items.get(high);
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (items.get(j).getTitle().compareToIgnoreCase(pivot.getTitle()) < 0) {
                i++;
                Collections.swap(items, i, j);
            }
        }
        Collections.swap(items, i + 1, high);
        return i + 1;
    }

    // --- Transactions & Undo (Requirement 3.2, 3.3) ---

    public boolean borrowItem(String itemId, String userId) {
        LibraryItem item = findItemById(itemId);
        if (item != null && item.getStatus().equals("Available")) {
            item.setStatus("Borrowed");
            item.setBorrowedBy(userId);
            undoStack.push("BORROW:" + itemId);
            return true;
        } else if (item != null) {
            reservations.computeIfAbsent(itemId, k -> new LinkedList<>()).add(userId);
        }
        return false;
    }

    public boolean returnItem(String itemId) {
        LibraryItem item = findItemById(itemId);
        if (item != null) {
            item.setStatus("Available");
            item.setBorrowedBy("None");
            if (reservations.containsKey(itemId) && !reservations.get(itemId).isEmpty()) {
                System.out.println("Reserved user notified: " + reservations.get(itemId).poll());
            }
        }
        return false;
    }

    public void undoLastAction() {
        if (undoStack.isEmpty()) return;
        String[] parts = undoStack.pop().split(":");
        if (parts[0].equals("BORROW")) returnItem(parts[1]);
        else if (parts[0].equals("ADD_USER")) users.remove(users.size() - 1);
    }

    // --- Helpers ---
    public void addUser(String name, String email) {
        String id = "U" + (users.size() + 101);
        users.add(new User(id, name, email));
        undoStack.push("ADD_USER:" + id);
    }

    private void updateCache(LibraryItem item) {
        for (int i = 4; i > 0; i--) mostFrequentCache[i] = mostFrequentCache[i-1];
        mostFrequentCache[0] = item;
    }

    public String[] getFixedCache() {
        return Arrays.stream(mostFrequentCache).filter(Objects::nonNull)
                .map(LibraryItem::getTitle).toArray(String[]::new);
    }

    public LibraryItem findItemById(String id) {
        return inventory.stream().filter(i -> i.getId().equals(id)).findFirst().orElse(null);
    }

    public List<LibraryItem> getInventory() { return inventory; }
    public List<User> getUsers() { return users; }
    public String getUndoStackSize() { return String.valueOf(undoStack.size()); }
    public String[] getReservationList() { return new String[]{"No active reservations"}; }
    public void saveData() { /* Implementation for File IO */ }
    public void loadFromFile(String path) { /* Implementation for File IO */ }
    public String generateReport() { return "Total Items: " + inventory.size() + "\nTotal Users: " + users.size(); }

    public int recursiveSearch(String query, int i) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'recursiveSearch'");
    }
}