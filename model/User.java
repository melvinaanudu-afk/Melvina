package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Requirement 10: Part of the User Management System.
 * Stores information about library members and their borrowed items.
 */
public class User implements Serializable {
    private String userId;
    private String name;
    private String email;
    private String role; // e.g., Student, Faculty, Admin
    private List<String> borrowedItemIds; // List to track items currently held

    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = "Student"; // Default role
        this.borrowedItemIds = new ArrayList<>();
    }

    // --- Business Logic ---

    public void addBorrowedItem(String itemId) {
        if (!borrowedItemIds.contains(itemId)) {
            borrowedItemIds.add(itemId);
        }
    }

    public void removeBorrowedItem(String itemId) {
        borrowedItemIds.remove(itemId);
    }

    // --- Getters and Setters ---

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public List<String> getBorrowedItemIds() { return borrowedItemIds; }

    @Override
    public String toString() {
        return name + " (" + userId + ")";
    }
}