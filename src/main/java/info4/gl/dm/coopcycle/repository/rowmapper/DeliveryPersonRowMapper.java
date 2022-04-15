package info4.gl.dm.coopcycle.repository.rowmapper;

import info4.gl.dm.coopcycle.domain.DeliveryPerson;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link DeliveryPerson}, with proper type conversions.
 */
@Service
public class DeliveryPersonRowMapper implements BiFunction<Row, String, DeliveryPerson> {

    private final ColumnConverter converter;

    public DeliveryPersonRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link DeliveryPerson} stored in the database.
     */
    @Override
    public DeliveryPerson apply(Row row, String prefix) {
        DeliveryPerson entity = new DeliveryPerson();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setiD(converter.fromRow(row, prefix + "_i_d", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setSurname(converter.fromRow(row, prefix + "_surname", String.class));
        entity.setPhone(converter.fromRow(row, prefix + "_phone", String.class));
        entity.setVehiculeType(converter.fromRow(row, prefix + "_vehicule_type", String.class));
        entity.setLatitude(converter.fromRow(row, prefix + "_latitude", Float.class));
        entity.setLongitude(converter.fromRow(row, prefix + "_longitude", Float.class));
        return entity;
    }
}
