package info4.gl.dm.coopcycle.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class DeliveryPersonSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("i_d", table, columnPrefix + "_i_d"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("surname", table, columnPrefix + "_surname"));
        columns.add(Column.aliased("phone", table, columnPrefix + "_phone"));
        columns.add(Column.aliased("vehicule_type", table, columnPrefix + "_vehicule_type"));
        columns.add(Column.aliased("latitude", table, columnPrefix + "_latitude"));
        columns.add(Column.aliased("longitude", table, columnPrefix + "_longitude"));

        return columns;
    }
}
