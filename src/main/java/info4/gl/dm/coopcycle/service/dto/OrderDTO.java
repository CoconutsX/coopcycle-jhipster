package info4.gl.dm.coopcycle.service.dto;

import info4.gl.dm.coopcycle.domain.enumeration.State;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link info4.gl.dm.coopcycle.domain.Order} entity.
 */
public class OrderDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Long iDOrder;

    @NotNull(message = "must not be null")
    private Integer iDCoop;

    @NotNull(message = "must not be null")
    private Integer iDCustomer;

    @NotNull(message = "must not be null")
    private Integer iDCourse;

    @Min(value = 3)
    @Max(value = 300)
    private Integer price;

    private ZonedDateTime date;

    private State orderState;

    private CustomerDTO customer;

    private CooperativeDTO cooperative;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getiDOrder() {
        return iDOrder;
    }

    public void setiDOrder(Long iDOrder) {
        this.iDOrder = iDOrder;
    }

    public Integer getiDCoop() {
        return iDCoop;
    }

    public void setiDCoop(Integer iDCoop) {
        this.iDCoop = iDCoop;
    }

    public Integer getiDCustomer() {
        return iDCustomer;
    }

    public void setiDCustomer(Integer iDCustomer) {
        this.iDCustomer = iDCustomer;
    }

    public Integer getiDCourse() {
        return iDCourse;
    }

    public void setiDCourse(Integer iDCourse) {
        this.iDCourse = iDCourse;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public State getOrderState() {
        return orderState;
    }

    public void setOrderState(State orderState) {
        this.orderState = orderState;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public CooperativeDTO getCooperative() {
        return cooperative;
    }

    public void setCooperative(CooperativeDTO cooperative) {
        this.cooperative = cooperative;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderDTO)) {
            return false;
        }

        OrderDTO orderDTO = (OrderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderDTO{" +
            "id=" + getId() +
            ", iDOrder=" + getiDOrder() +
            ", iDCoop=" + getiDCoop() +
            ", iDCustomer=" + getiDCustomer() +
            ", iDCourse=" + getiDCourse() +
            ", price=" + getPrice() +
            ", date='" + getDate() + "'" +
            ", orderState='" + getOrderState() + "'" +
            ", customer=" + getCustomer() +
            ", cooperative=" + getCooperative() +
            "}";
    }
}
