package info4.gl.dm.coopcycle.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class OrderSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("i_d_order", table, columnPrefix + "_i_d_order"));
        columns.add(Column.aliased("i_d_coop", table, columnPrefix + "_i_d_coop"));
        columns.add(Column.aliased("i_d_customer", table, columnPrefix + "_i_d_customer"));
        columns.add(Column.aliased("i_d_course", table, columnPrefix + "_i_d_course"));
        columns.add(Column.aliased("price", table, columnPrefix + "_price"));
        columns.add(Column.aliased("date", table, columnPrefix + "_date"));
        columns.add(Column.aliased("order_state", table, columnPrefix + "_order_state"));

        columns.add(Column.aliased("customer_id", table, columnPrefix + "_customer_id"));
        columns.add(Column.aliased("cooperative_id", table, columnPrefix + "_cooperative_id"));
        return columns;
    }
}
