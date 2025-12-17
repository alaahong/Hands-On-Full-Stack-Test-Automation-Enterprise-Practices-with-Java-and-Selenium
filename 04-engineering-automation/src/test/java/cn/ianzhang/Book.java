package cn.ianzhang;

import java.util.Objects;

/**
 * @Author: Ian
 * @Description:
 */
public class Book {
    private String title;
    private Double price;
    private String author;

    public Book() {
    }

    public Book(String title, Double price, String author) {
        this.title = title;
        this.price = price;
        this.author = author;
    }

    public Book(String title, String price, String author) {
        this.title = title;
        this.price = Double.valueOf(price);
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(title, book.title) && Objects.equals(price, book.price) && Objects.equals(author, book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, price, author);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
