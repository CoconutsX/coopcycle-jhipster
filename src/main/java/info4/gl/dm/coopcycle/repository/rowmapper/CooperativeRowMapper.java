package info4.gl.dm.coopcycle.repository.rowmapper;

import info4.gl.dm.coopcycle.domain.Cooperative;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Cooperative}, with proper type conversions.
 */
@Service
public class CooperativeRowMapper implements BiFunction<Row, String, Cooperative> {

    private final ColumnConverter converter;

    public CooperativeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Cooperative} stored in the database.
     */
    @Override
    public Cooperative apply(Row row, String prefix) {
        Cooperative entity = new Cooperative();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setiD(converter.fromRow(row, prefix + "_i_d", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setPhone(converter.fromRow(row, prefix + "_phone", String.class));
        entity.setAddress(converter.fromRow(row, prefix + "_address", String.class));
        entity.setWebURL(converter.fromRow(row, prefix + "_web_url", String.class));
        return entity;
    }
}
