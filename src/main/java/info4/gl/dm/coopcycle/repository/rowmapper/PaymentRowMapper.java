package info4.gl.dm.coopcycle.repository.rowmapper;

import info4.gl.dm.coopcycle.domain.Payment;
import info4.gl.dm.coopcycle.domain.enumeration.PaymentMethod;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Payment}, with proper type conversions.
 */
@Service
public class PaymentRowMapper implements BiFunction<Row, String, Payment> {

    private final ColumnConverter converter;

    public PaymentRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Payment} stored in the database.
     */
    @Override
    public Payment apply(Row row, String prefix) {
        Payment entity = new Payment();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setMethod(converter.fromRow(row, prefix + "_method", PaymentMethod.class));
        entity.setAmmount(converter.fromRow(row, prefix + "_ammount", Integer.class));
        entity.setOrderId(converter.fromRow(row, prefix + "_order_id", Long.class));
        return entity;
    }
}
