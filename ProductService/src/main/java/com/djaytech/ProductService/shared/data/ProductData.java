package com.djaytech.ProductService.shared.data;

        import jakarta.persistence.Entity;
        import jakarta.persistence.Id;
        import lombok.Data;

        import java.math.BigDecimal;

@Data
@Entity
public class ProductData {

    @Id
    private String productId;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private boolean deleted;

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}

