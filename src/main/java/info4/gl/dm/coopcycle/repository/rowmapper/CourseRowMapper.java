package info4.gl.dm.coopcycle.repository.rowmapper;

import info4.gl.dm.coopcycle.domain.Course;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Course}, with proper type conversions.
 */
@Service
public class CourseRowMapper implements BiFunction<Row, String, Course> {

    private final ColumnConverter converter;

    public CourseRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Course} stored in the database.
     */
    @Override
    public Course apply(Row row, String prefix) {
        Course entity = new Course();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setiD(converter.fromRow(row, prefix + "_i_d", Long.class));
        entity.setiDDeliveryPerson(converter.fromRow(row, prefix + "_i_d_delivery_person", Long.class));
        entity.setOrderId(converter.fromRow(row, prefix + "_order_id", Long.class));
        entity.setDeliveryPersonId(converter.fromRow(row, prefix + "_delivery_person_id", Long.class));
        return entity;
    }
}
