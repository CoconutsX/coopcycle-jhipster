package info4.gl.dm.coopcycle.repository.rowmapper;

import info4.gl.dm.coopcycle.domain.Order;
import info4.gl.dm.coopcycle.domain.enumeration.State;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Order}, with proper type conversions.
 */
@Service
public class OrderRowMapper implements BiFunction<Row, String, Order> {

    private final ColumnConverter converter;

    public OrderRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Order} stored in the database.
     */
    @Override
    public Order apply(Row row, String prefix) {
        Order entity = new Order();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setiDOrder(converter.fromRow(row, prefix + "_i_d_order", Long.class));
        entity.setiDCoop(converter.fromRow(row, prefix + "_i_d_coop", Integer.class));
        entity.setiDCustomer(converter.fromRow(row, prefix + "_i_d_customer", Integer.class));
        entity.setiDCourse(converter.fromRow(row, prefix + "_i_d_course", Integer.class));
        entity.setPrice(converter.fromRow(row, prefix + "_price", Integer.class));
        entity.setDate(converter.fromRow(row, prefix + "_date", ZonedDateTime.class));
        entity.setOrderState(converter.fromRow(row, prefix + "_order_state", State.class));
        entity.setCustomerId(converter.fromRow(row, prefix + "_customer_id", Long.class));
        entity.setCooperativeId(converter.fromRow(row, prefix + "_cooperative_id", Long.class));
        return entity;
    }
}
