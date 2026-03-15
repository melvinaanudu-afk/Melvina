package model;

public class Book extends LibraryItem {
    public Book(String id, String title, String author, int year, String category) {
        super(id, title, author, year, category);
    }

    @Override
    public String getType() { return "Book"; } 
}