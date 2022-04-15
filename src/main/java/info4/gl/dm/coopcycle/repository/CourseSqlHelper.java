package info4.gl.dm.coopcycle.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class CourseSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("i_d", table, columnPrefix + "_i_d"));
        columns.add(Column.aliased("i_d_delivery_person", table, columnPrefix + "_i_d_delivery_person"));

        columns.add(Column.aliased("order_id", table, columnPrefix + "_order_id"));
        columns.add(Column.aliased("delivery_person_id", table, columnPrefix + "_delivery_person_id"));
        return columns;
    }
}
