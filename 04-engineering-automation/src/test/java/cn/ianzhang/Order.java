package cn.ianzhang;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * @Author: Ian
 * @Description:
 */
public class Order {
    private String orderNumber;
    private LocalDate orderDate;
    private List<Book> bookDetails;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(orderNumber, order.orderNumber) && Objects.equals(orderDate, order.orderDate) && Objects.equals(bookDetails, order.bookDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNumber, orderDate, bookDetails);
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public List<Book> getBookDetails() {
        return bookDetails;
    }

    public void setBookDetails(List<Book> bookDetails) {
        this.bookDetails = bookDetails;
    }
}
