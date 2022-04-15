package info4.gl.dm.coopcycle.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link info4.gl.dm.coopcycle.domain.Product} entity.
 */
public class ProductDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Long iDProduct;

    @NotNull(message = "must not be null")
    private Long iDMenu;

    private String name;

    private Float price;

    @Min(value = 0)
    private Integer stock;

    private CooperativeDTO cooperative;

    private OrderDTO order;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getiDProduct() {
        return iDProduct;
    }

    public void setiDProduct(Long iDProduct) {
        this.iDProduct = iDProduct;
    }

    public Long getiDMenu() {
        return iDMenu;
    }

    public void setiDMenu(Long iDMenu) {
        this.iDMenu = iDMenu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public CooperativeDTO getCooperative() {
        return cooperative;
    }

    public void setCooperative(CooperativeDTO cooperative) {
        this.cooperative = cooperative;
    }

    public OrderDTO getOrder() {
        return order;
    }

    public void setOrder(OrderDTO order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDTO)) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + getId() +
            ", iDProduct=" + getiDProduct() +
            ", iDMenu=" + getiDMenu() +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", stock=" + getStock() +
            ", cooperative=" + getCooperative() +
            ", order=" + getOrder() +
            "}";
    }
}
