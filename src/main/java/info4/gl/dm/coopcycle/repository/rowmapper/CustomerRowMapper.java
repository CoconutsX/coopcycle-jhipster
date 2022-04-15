package info4.gl.dm.coopcycle.repository.rowmapper;

import info4.gl.dm.coopcycle.domain.Customer;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Customer}, with proper type conversions.
 */
@Service
public class CustomerRowMapper implements BiFunction<Row, String, Customer> {

    private final ColumnConverter converter;

    public CustomerRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Customer} stored in the database.
     */
    @Override
    public Customer apply(Row row, String prefix) {
        Customer entity = new Customer();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setiD(converter.fromRow(row, prefix + "_i_d", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setSurname(converter.fromRow(row, prefix + "_surname", String.class));
        entity.setPhone(converter.fromRow(row, prefix + "_phone", String.class));
        entity.setAddress(converter.fromRow(row, prefix + "_address", String.class));
        return entity;
    }
}
