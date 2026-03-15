package controller;

import model.*;

public class BorrowController {
    private LibraryManager manager;

    public BorrowController(LibraryManager manager) {
        this.manager = manager;
    }

    public boolean processBorrow(String itemId, String userId) {
        if (itemId.isEmpty() || userId.isEmpty()) return false;

        boolean success = manager.borrowItem(itemId, userId);
        if (success) {
            manager.logTransaction("BORROW", itemId, userId);
            FileHandler.saveData(manager.getInventory());
        }
        return success;
    }

    public boolean processReturn(String itemId) {
        if (itemId.isEmpty()) return false;

        boolean success = manager.returnItem(itemId);
        if (success) {
            manager.logTransaction("RETURN", itemId, "System/Admin");
            FileHandler.saveData(manager.getInventory());
        }
        return success;
    }
}