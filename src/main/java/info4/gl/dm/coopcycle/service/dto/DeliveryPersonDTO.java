package info4.gl.dm.coopcycle.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link info4.gl.dm.coopcycle.domain.DeliveryPerson} entity.
 */
public class DeliveryPersonDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Long iD;

    @NotNull(message = "must not be null")
    private String name;

    @NotNull(message = "must not be null")
    private String surname;

    @Size(min = 10, max = 10)
    private String phone;

    private String vehiculeType;

    @NotNull(message = "must not be null")
    private Float latitude;

    @NotNull(message = "must not be null")
    private Float longitude;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVehiculeType() {
        return vehiculeType;
    }

    public void setVehiculeType(String vehiculeType) {
        this.vehiculeType = vehiculeType;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeliveryPersonDTO)) {
            return false;
        }

        DeliveryPersonDTO deliveryPersonDTO = (DeliveryPersonDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deliveryPersonDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeliveryPersonDTO{" +
            "id=" + getId() +
            ", iD=" + getiD() +
            ", name='" + getName() + "'" +
            ", surname='" + getSurname() + "'" +
            ", phone='" + getPhone() + "'" +
            ", vehiculeType='" + getVehiculeType() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            "}";
    }
}
