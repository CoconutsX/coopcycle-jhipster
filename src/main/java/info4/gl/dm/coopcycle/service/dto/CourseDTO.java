package info4.gl.dm.coopcycle.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link info4.gl.dm.coopcycle.domain.Course} entity.
 */
public class CourseDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Long iD;

    @NotNull(message = "must not be null")
    private Long iDDeliveryPerson;

    private OrderDTO order;

    private DeliveryPersonDTO deliveryPerson;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getiD() {
        return iD;
    }

    public void setiD(Long iD) {
        this.iD = iD;
    }

    public Long getiDDeliveryPerson() {
        return iDDeliveryPerson;
    }

    public void setiDDeliveryPerson(Long iDDeliveryPerson) {
        this.iDDeliveryPerson = iDDeliveryPerson;
    }

    public OrderDTO getOrder() {
        return order;
    }

    public void setOrder(OrderDTO order) {
        this.order = order;
    }

    public DeliveryPersonDTO getDeliveryPerson() {
        return deliveryPerson;
    }

    public void setDeliveryPerson(DeliveryPersonDTO deliveryPerson) {
        this.deliveryPerson = deliveryPerson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseDTO)) {
            return false;
        }

        CourseDTO courseDTO = (CourseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, courseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourseDTO{" +
            "id=" + getId() +
            ", iD=" + getiD() +
            ", iDDeliveryPerson=" + getiDDeliveryPerson() +
            ", order=" + getOrder() +
            ", deliveryPerson=" + getDeliveryPerson() +
            "}";
    }
}
