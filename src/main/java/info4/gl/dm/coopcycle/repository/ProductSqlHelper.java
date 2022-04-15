package info4.gl.dm.coopcycle.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ProductSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("i_d_product", table, columnPrefix + "_i_d_product"));
        columns.add(Column.aliased("i_d_menu", table, columnPrefix + "_i_d_menu"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("price", table, columnPrefix + "_price"));
        columns.add(Column.aliased("stock", table, columnPrefix + "_stock"));

        columns.add(Column.aliased("cooperative_id", table, columnPrefix + "_cooperative_id"));
        columns.add(Column.aliased("order_id", table, columnPrefix + "_order_id"));
        return columns;
    }
}
