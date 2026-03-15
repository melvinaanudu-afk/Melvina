package controller;

import model.*;
import java.io.*;
import java.util.*;

public class FileHandler {
    private static final String DATA_FILE = "library_data.txt";

    public static void saveData(List<LibraryItem> items) {
        try (PrintWriter out = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (LibraryItem item : items) {
                // Format: Type|ID|Title|Author|Year|Category|Status|BorrowedBy
                out.println(item.getType() + "|" + item.getId() + "|" + item.getTitle() + "|" +
                            item.getAuthor() + "|" + item.getYear() + "|" + item.getCategory() + "|" +
                            item.getStatus() + "|" + item.getBorrowedBy());
            }
        } catch (IOException e) {
            System.err.println("Persistence Error: " + e.getMessage());
        }
    }

    public static List<LibraryItem> loadData() {
        List<LibraryItem> items = new ArrayList<>();
        File file = new File(DATA_FILE);
        if (!file.exists()) return items;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|");
                if (p.length < 8) continue;

                LibraryItem item;
                if (p[0].equals("Book")) item = new Book(p[1], p[2], p[3], Integer.parseInt(p[4]), p[5]);
                else if (p[0].equals("Magazine")) item = new Magazine(p[1], p[2], p[3], Integer.parseInt(p[4]), p[5]);
                else item = new Journal(p[1], p[2], p[3], Integer.parseInt(p[4]), p[5]);

                item.setStatus(p[6]);
                item.setBorrowedBy(p[7]);
                items.add(item);
            }
        } catch (Exception e) {
            System.err.println("Load Error: " + e.getMessage());
        }
        return items;
    }
}