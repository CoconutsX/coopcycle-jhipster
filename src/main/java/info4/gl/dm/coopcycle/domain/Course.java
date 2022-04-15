package info4.gl.dm.coopcycle.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Course.
 */
@Table("course")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("i_d")
    private Long iD;

    @NotNull(message = "must not be null")
    @Column("i_d_delivery_person")
    private Long iDDeliveryPerson;

    @Transient
    private Order order;

    @Transient
    @JsonIgnoreProperties(value = { "courses" }, allowSetters = true)
    private DeliveryPerson deliveryPerson;

    @Column("order_id")
    private Long orderId;

    @Column("delivery_person_id")
    private Long deliveryPersonId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Course id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getiD() {
        return this.iD;
    }

    public Course iD(Long iD) {
        this.setiD(iD);
        return this;
    }

    public void setiD(Long iD) {
        this.iD = iD;
    }

    public Long getiDDeliveryPerson() {
        return this.iDDeliveryPerson;
    }

    public Course iDDeliveryPerson(Long iDDeliveryPerson) {
        this.setiDDeliveryPerson(iDDeliveryPerson);
        return this;
    }

    public void setiDDeliveryPerson(Long iDDeliveryPerson) {
        this.iDDeliveryPerson = iDDeliveryPerson;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
        this.orderId = order != null ? order.getId() : null;
    }

    public Course order(Order order) {
        this.setOrder(order);
        return this;
    }

    public DeliveryPerson getDeliveryPerson() {
        return this.deliveryPerson;
    }

    public void setDeliveryPerson(DeliveryPerson deliveryPerson) {
        this.deliveryPerson = deliveryPerson;
        this.deliveryPersonId = deliveryPerson != null ? deliveryPerson.getId() : null;
    }

    public Course deliveryPerson(DeliveryPerson deliveryPerson) {
        this.setDeliveryPerson(deliveryPerson);
        return this;
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Long order) {
        this.orderId = order;
    }

    public Long getDeliveryPersonId() {
        return this.deliveryPersonId;
    }

    public void setDeliveryPersonId(Long deliveryPerson) {
        this.deliveryPersonId = deliveryPerson;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Course)) {
            return false;
        }
        return id != null && id.equals(((Course) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Course{" +
            "id=" + getId() +
            ", iD=" + getiD() +
            ", iDDeliveryPerson=" + getiDDeliveryPerson() +
            "}";
    }
}
