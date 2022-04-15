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
 * A DeliveryPerson.
 */
@Table("delivery_person")
public class DeliveryPerson implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("i_d")
    private Long iD;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @NotNull(message = "must not be null")
    @Column("surname")
    private String surname;

    @Size(min = 10, max = 10)
    @Column("phone")
    private String phone;

    @Column("vehicule_type")
    private String vehiculeType;

    @NotNull(message = "must not be null")
    @Column("latitude")
    private Float latitude;

    @NotNull(message = "must not be null")
    @Column("longitude")
    private Float longitude;

    @Transient
    @JsonIgnoreProperties(value = { "order", "deliveryPerson" }, allowSetters = true)
    private Set<Course> courses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DeliveryPerson id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getiD() {
        return this.iD;
    }

    public DeliveryPerson iD(Long iD) {
        this.setiD(iD);
        return this;
    }

    public void setiD(Long iD) {
        this.iD = iD;
    }

    public String getName() {
        return this.name;
    }

    public DeliveryPerson name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public DeliveryPerson surname(String surname) {
        this.setSurname(surname);
        return this;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return this.phone;
    }

    public DeliveryPerson phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVehiculeType() {
        return this.vehiculeType;
    }

    public DeliveryPerson vehiculeType(String vehiculeType) {
        this.setVehiculeType(vehiculeType);
        return this;
    }

    public void setVehiculeType(String vehiculeType) {
        this.vehiculeType = vehiculeType;
    }

    public Float getLatitude() {
        return this.latitude;
    }

    public DeliveryPerson latitude(Float latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return this.longitude;
    }

    public DeliveryPerson longitude(Float longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Set<Course> getCourses() {
        return this.courses;
    }

    public void setCourses(Set<Course> courses) {
        if (this.courses != null) {
            this.courses.forEach(i -> i.setDeliveryPerson(null));
        }
        if (courses != null) {
            courses.forEach(i -> i.setDeliveryPerson(this));
        }
        this.courses = courses;
    }

    public DeliveryPerson courses(Set<Course> courses) {
        this.setCourses(courses);
        return this;
    }

    public DeliveryPerson addCourse(Course course) {
        this.courses.add(course);
        course.setDeliveryPerson(this);
        return this;
    }

    public DeliveryPerson removeCourse(Course course) {
        this.courses.remove(course);
        course.setDeliveryPerson(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeliveryPerson)) {
            return false;
        }
        return id != null && id.equals(((DeliveryPerson) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeliveryPerson{" +
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
