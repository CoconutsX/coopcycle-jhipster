package info4.gl.dm.coopcycle.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class PaymentSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("method", table, columnPrefix + "_method"));
        columns.add(Column.aliased("ammount", table, columnPrefix + "_ammount"));

        columns.add(Column.aliased("order_id", table, columnPrefix + "_order_id"));
        return columns;
    }
}
