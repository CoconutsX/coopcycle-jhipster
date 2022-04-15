package info4.gl.dm.coopcycle.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Cooperative.
 */
@Table("cooperative")
public class Cooperative implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("i_d")
    private Long iD;

    @Size(min = 3)
    @Column("name")
    private String name;

    @Size(min = 10, max = 10)
    @Column("phone")
    private String phone;

    @NotNull(message = "must not be null")
    @Column("address")
    private String address;

    @Column("web_url")
    private String webURL;

    @Transient
    @JsonIgnoreProperties(value = { "cooperative", "order" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "products", "course", "payment", "customer", "cooperative" }, allowSetters = true)
    private Set<Order> orders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cooperative id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getiD() {
        return this.iD;
    }

    public Cooperative iD(Long iD) {
        this.setiD(iD);
        return this;
    }

    public void setiD(Long iD) {
        this.iD = iD;
    }

    public String getName() {
        return this.name;
    }

    public Cooperative name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return this.phone;
    }

    public Cooperative phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return this.address;
    }

    public Cooperative address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebURL() {
        return this.webURL;
    }

    public Cooperative webURL(String webURL) {
        this.setWebURL(webURL);
        return this;
    }

    public void setWebURL(String webURL) {
        this.webURL = webURL;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setCooperative(null));
        }
        if (products != null) {
            products.forEach(i -> i.setCooperative(this));
        }
        this.products = products;
    }

    public Cooperative products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Cooperative addProduct(Product product) {
        this.products.add(product);
        product.setCooperative(this);
        return this;
    }

    public Cooperative removeProduct(Product product) {
        this.products.remove(product);
        product.setCooperative(null);
        return this;
    }

    public Set<Order> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Order> orders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setCooperative(null));
        }
        if (orders != null) {
            orders.forEach(i -> i.setCooperative(this));
        }
        this.orders = orders;
    }

    public Cooperative orders(Set<Order> orders) {
        this.setOrders(orders);
        return this;
    }

    public Cooperative addOrder(Order order) {
        this.orders.add(order);
        order.setCooperative(this);
        return this;
    }

    public Cooperative removeOrder(Order order) {
        this.orders.remove(order);
        order.setCooperative(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cooperative)) {
            return false;
        }
        return id != null && id.equals(((Cooperative) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cooperative{" +
            "id=" + getId() +
            ", iD=" + getiD() +
            ", name='" + getName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", address='" + getAddress() + "'" +
            ", webURL='" + getWebURL() + "'" +
            "}";
    }
}
