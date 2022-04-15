package info4.gl.dm.coopcycle.repository.rowmapper;

import info4.gl.dm.coopcycle.domain.Product;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Product}, with proper type conversions.
 */
@Service
public class ProductRowMapper implements BiFunction<Row, String, Product> {

    private final ColumnConverter converter;

    public ProductRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Product} stored in the database.
     */
    @Override
    public Product apply(Row row, String prefix) {
        Product entity = new Product();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setiDProduct(converter.fromRow(row, prefix + "_i_d_product", Long.class));
        entity.setiDMenu(converter.fromRow(row, prefix + "_i_d_menu", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setPrice(converter.fromRow(row, prefix + "_price", Float.class));
        entity.setStock(converter.fromRow(row, prefix + "_stock", Integer.class));
        entity.setCooperativeId(converter.fromRow(row, prefix + "_cooperative_id", Long.class));
        entity.setOrderId(converter.fromRow(row, prefix + "_order_id", Long.class));
        return entity;
    }
}
