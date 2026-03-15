package controller;

import model.*;
import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class LibraryManager {
   
    private List<LibraryItem> inventory = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<String> transactionHistory = new ArrayList<>(); 
    private Map<String, Queue<String>> reservations = new HashMap<>(); 
    private Stack<String> undoStack = new Stack<>(); 
    private LibraryItem[] mostFrequentCache = new LibraryItem[5]; 

    public LibraryManager() {
       
        inventory.add(new Book("B001", "Java Programming", "Deitel", 2024, "Education"));
        inventory.add(new Book("B002", "Data Structures", "Weiss", 2023, "Tech"));
        inventory.add(new Book("B003", "Algorithms", "Sedgewick", 2022, "Tech"));
        inventory.add(new Magazine("M001", "Tech Monthly", "Tech Publisher", 2024, "Technology"));
        inventory.add(new Magazine("M002", "Science Weekly", "Science Publisher", 2023, "Science"));
        inventory.add(new Journal("J001", "AI Research", "AI Institute", 2024, "Science"));
        inventory.add(new Journal("J002", "Quantum Computing", "Quantum Labs", 2023, "Science"));
    }


    public LibraryItem performSearch(String query, String method) {
        if (query == null || query.isEmpty()) return null;
        
        LibraryItem result = null;
        switch (method) {
            case "Recursive Search":
                result = recursiveSearchByTitle(inventory, query, 0);
                break;
            case "Binary Search":
                sortInventory("Title", "QuickSort");
                result = binarySearchByTitle(inventory, query);
                break;
            default: 
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


    public void quickSort(List<LibraryItem> items, int low, int high, String criteria) {
        if (low < high) {
            int pi = partition(items, low, high, criteria);
            quickSort(items, low, pi - 1, criteria);
            quickSort(items, pi + 1, high, criteria);
        }
    }
public void addUser(String name, String email, String role) {
    String id = "U" + (users.size() + 101);
    users.add(new User(id, name, email, role)); // Passing role here
    undoStack.push("ADD_USER:" + id);
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

    public void sortInventory(String criteria, String algorithm) {
        if (algorithm.equals("QuickSort")) {
            quickSort(inventory, 0, inventory.size() - 1, criteria);
        } else {
            inventory.sort((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));
        }
    }


    public void logTransaction(String action, String itemId, String userId) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String message = String.format("[%s] %s - Item: %s | User: %s", timestamp, action, itemId, userId);
        transactionHistory.add(message);
    }

    public boolean borrowItem(String itemId, String userId) {
        LibraryItem item = findItemById(itemId);
        if (item != null && item.getStatus().equals("Available")) {
            item.setStatus("Borrowed");
            item.setBorrowedBy(userId);
            undoStack.push("BORROW:" + itemId);
            logTransaction("BORROW", itemId, userId);
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
            logTransaction("RETURN", itemId, "System");
            
            if (reservations.containsKey(itemId) && !reservations.get(itemId).isEmpty()) {
                System.out.println("Reserved user notified: " + reservations.get(itemId).poll());
            }
            return true;
        }
        return false;
    }

    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- MIVA LIBRARY STATUS REPORT ---\n");
        sb.append("Total Items: ").append(inventory.size()).append("\n");
        sb.append("Total Users: ").append(users.size()).append("\n\n");
        sb.append("--- RECENT ACTIVITY ---\n");
        sb.append(getFullHistory());
        return sb.toString();
    }

    public String getFullHistory() {
        if (transactionHistory.isEmpty()) return "No transactions recorded.";
        return String.join("\n", transactionHistory);
    }


    public void undoLastAction() {
        if (undoStack.isEmpty()) return;
        String[] parts = undoStack.pop().split(":");
        if (parts[0].equals("BORROW")) returnItem(parts[1]);
        else if (parts[0].equals("ADD_USER")) users.remove(users.size() - 1);
    }

    private void updateCache(LibraryItem item) {
        for (int i = 4; i > 0; i--) mostFrequentCache[i] = mostFrequentCache[i-1];
        mostFrequentCache[0] = item;
    }


    public LibraryItem findItemById(String id) {
        return inventory.stream().filter(i -> i.getId().equals(id)).findFirst().orElse(null);
    }

    public List<LibraryItem> getInventory() { return inventory; }
    public List<User> getUsers() { return users; }
    public String getUndoStackSize() { return String.valueOf(undoStack.size()); }

    public int recursiveSearch(String query, int i) {
   
        throw new UnsupportedOperationException("Unimplemented method 'recursiveSearch'");
    }
}