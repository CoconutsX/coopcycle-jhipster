package info4.gl.dm.coopcycle.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import info4.gl.dm.coopcycle.domain.enumeration.State;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Order.
 */
@Table("jhi_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("i_d_order")
    private Long iDOrder;

    @NotNull(message = "must not be null")
    @Column("i_d_coop")
    private Integer iDCoop;

    @NotNull(message = "must not be null")
    @Column("i_d_customer")
    private Integer iDCustomer;

    @NotNull(message = "must not be null")
    @Column("i_d_course")
    private Integer iDCourse;

    @Min(value = 3)
    @Max(value = 300)
    @Column("price")
    private Integer price;

    @Column("date")
    private ZonedDateTime date;

    @Column("order_state")
    private State orderState;

    @Transient
    @JsonIgnoreProperties(value = { "cooperative", "order" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    @Transient
    private Course course;

    @Transient
    private Payment payment;

    @Transient
    @JsonIgnoreProperties(value = { "orders" }, allowSetters = true)
    private Customer customer;

    @Transient
    @JsonIgnoreProperties(value = { "products", "orders" }, allowSetters = true)
    private Cooperative cooperative;

    @Column("customer_id")
    private Long customerId;

    @Column("cooperative_id")
    private Long cooperativeId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Order id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getiDOrder() {
        return this.iDOrder;
    }

    public Order iDOrder(Long iDOrder) {
        this.setiDOrder(iDOrder);
        return this;
    }

    public void setiDOrder(Long iDOrder) {
        this.iDOrder = iDOrder;
    }

    public Integer getiDCoop() {
        return this.iDCoop;
    }

    public Order iDCoop(Integer iDCoop) {
        this.setiDCoop(iDCoop);
        return this;
    }

    public void setiDCoop(Integer iDCoop) {
        this.iDCoop = iDCoop;
    }

    public Integer getiDCustomer() {
        return this.iDCustomer;
    }

    public Order iDCustomer(Integer iDCustomer) {
        this.setiDCustomer(iDCustomer);
        return this;
    }

    public void setiDCustomer(Integer iDCustomer) {
        this.iDCustomer = iDCustomer;
    }

    public Integer getiDCourse() {
        return this.iDCourse;
    }

    public Order iDCourse(Integer iDCourse) {
        this.setiDCourse(iDCourse);
        return this;
    }

    public void setiDCourse(Integer iDCourse) {
        this.iDCourse = iDCourse;
    }

    public Integer getPrice() {
        return this.price;
    }

    public Order price(Integer price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public Order date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public State getOrderState() {
        return this.orderState;
    }

    public Order orderState(State orderState) {
        this.setOrderState(orderState);
        return this;
    }

    public void setOrderState(State orderState) {
        this.orderState = orderState;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setOrder(null));
        }
        if (products != null) {
            products.forEach(i -> i.setOrder(this));
        }
        this.products = products;
    }

    public Order products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Order addProduct(Product product) {
        this.products.add(product);
        product.setOrder(this);
        return this;
    }

    public Order removeProduct(Product product) {
        this.products.remove(product);
        product.setOrder(null);
        return this;
    }

    public Course getCourse() {
        return this.course;
    }

    public void setCourse(Course course) {
        if (this.course != null) {
            this.course.setOrder(null);
        }
        if (course != null) {
            course.setOrder(this);
        }
        this.course = course;
    }

    public Order course(Course course) {
        this.setCourse(course);
        return this;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public void setPayment(Payment payment) {
        if (this.payment != null) {
            this.payment.setOrder(null);
        }
        if (payment != null) {
            payment.setOrder(this);
        }
        this.payment = payment;
    }

    public Order payment(Payment payment) {
        this.setPayment(payment);
        return this;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        this.customerId = customer != null ? customer.getId() : null;
    }

    public Order customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public Cooperative getCooperative() {
        return this.cooperative;
    }

    public void setCooperative(Cooperative cooperative) {
        this.cooperative = cooperative;
        this.cooperativeId = cooperative != null ? cooperative.getId() : null;
    }

    public Order cooperative(Cooperative cooperative) {
        this.setCooperative(cooperative);
        return this;
    }

    public Long getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(Long customer) {
        this.customerId = customer;
    }

    public Long getCooperativeId() {
        return this.cooperativeId;
    }

    public void setCooperativeId(Long cooperative) {
        this.cooperativeId = cooperative;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return id != null && id.equals(((Order) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", iDOrder=" + getiDOrder() +
            ", iDCoop=" + getiDCoop() +
            ", iDCustomer=" + getiDCustomer() +
            ", iDCourse=" + getiDCourse() +
            ", price=" + getPrice() +
            ", date='" + getDate() + "'" +
            ", orderState='" + getOrderState() + "'" +
            "}";
    }
}
