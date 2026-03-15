package controller;

import model.*;
import javax.swing.JOptionPane;

public class BorrowController {
    private LibraryManager manager;

    public BorrowController(LibraryManager manager) {
        this.manager = manager;
    }

    /**
     * Requirement 7: Event-Driven Logic
     * Processes borrowing and saves state to file.
     */
    public boolean processBorrow(String itemId, String userId) {
        if (itemId.isEmpty() || userId.isEmpty()) return false;

        boolean success = manager.borrowItem(itemId, userId);
        if (success) {
            // Requirement 10: Persistence (Save after every major change)
            FileHandler.saveData(manager.getInventory());
        }
        return success;
    }

    public boolean processReturn(String itemId) {
        if (itemId.isEmpty()) return false;

        boolean success = manager.returnItem(itemId);
        if (success) {
            FileHandler.saveData(manager.getInventory());
        }
        return success;
    }
}