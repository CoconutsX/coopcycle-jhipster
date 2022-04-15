package info4.gl.dm.coopcycle.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Product.
 */
@Table("product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("i_d_product")
    private Long iDProduct;

    @NotNull(message = "must not be null")
    @Column("i_d_menu")
    private Long iDMenu;

    @Column("name")
    private String name;

    @Column("price")
    private Float price;

    @Min(value = 0)
    @Column("stock")
    private Integer stock;

    @Transient
    @JsonIgnoreProperties(value = { "products", "orders" }, allowSetters = true)
    private Cooperative cooperative;

    @Transient
    @JsonIgnoreProperties(value = { "products", "course", "payment", "customer", "cooperative" }, allowSetters = true)
    private Order order;

    @Column("cooperative_id")
    private Long cooperativeId;

    @Column("order_id")
    private Long orderId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getiDProduct() {
        return this.iDProduct;
    }

    public Product iDProduct(Long iDProduct) {
        this.setiDProduct(iDProduct);
        return this;
    }

    public void setiDProduct(Long iDProduct) {
        this.iDProduct = iDProduct;
    }

    public Long getiDMenu() {
        return this.iDMenu;
    }

    public Product iDMenu(Long iDMenu) {
        this.setiDMenu(iDMenu);
        return this;
    }

    public void setiDMenu(Long iDMenu) {
        this.iDMenu = iDMenu;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return this.price;
    }

    public Product price(Float price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getStock() {
        return this.stock;
    }

    public Product stock(Integer stock) {
        this.setStock(stock);
        return this;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Cooperative getCooperative() {
        return this.cooperative;
    }

    public void setCooperative(Cooperative cooperative) {
        this.cooperative = cooperative;
        this.cooperativeId = cooperative != null ? cooperative.getId() : null;
    }

    public Product cooperative(Cooperative cooperative) {
        this.setCooperative(cooperative);
        return this;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
        this.orderId = order != null ? order.getId() : null;
    }

    public Product order(Order order) {
        this.setOrder(order);
        return this;
    }

    public Long getCooperativeId() {
        return this.cooperativeId;
    }

    public void setCooperativeId(Long cooperative) {
        this.cooperativeId = cooperative;
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Long order) {
        this.orderId = order;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", iDProduct=" + getiDProduct() +
            ", iDMenu=" + getiDMenu() +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", stock=" + getStock() +
            "}";
    }
}
