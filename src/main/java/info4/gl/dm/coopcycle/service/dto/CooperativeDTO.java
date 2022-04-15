package info4.gl.dm.coopcycle.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link info4.gl.dm.coopcycle.domain.Cooperative} entity.
 */
public class CooperativeDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Long iD;

    @Size(min = 3)
    private String name;

    @Size(min = 10, max = 10)
    private String phone;

    @NotNull(message = "must not be null")
    private String address;

    private String webURL;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebURL() {
        return webURL;
    }

    public void setWebURL(String webURL) {
        this.webURL = webURL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CooperativeDTO)) {
            return false;
        }

        CooperativeDTO cooperativeDTO = (CooperativeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cooperativeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CooperativeDTO{" +
            "id=" + getId() +
            ", iD=" + getiD() +
            ", name='" + getName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", address='" + getAddress() + "'" +
            ", webURL='" + getWebURL() + "'" +
            "}";
    }
}
