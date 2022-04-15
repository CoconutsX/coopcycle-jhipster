package info4.gl.dm.coopcycle.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import info4.gl.dm.coopcycle.domain.Order;
import info4.gl.dm.coopcycle.domain.enumeration.State;
import info4.gl.dm.coopcycle.repository.rowmapper.CooperativeRowMapper;
import info4.gl.dm.coopcycle.repository.rowmapper.CustomerRowMapper;
import info4.gl.dm.coopcycle.repository.rowmapper.OrderRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the Order entity.
 */
@SuppressWarnings("unused")
class OrderRepositoryInternalImpl extends SimpleR2dbcRepository<Order, Long> implements OrderRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CustomerRowMapper customerMapper;
    private final CooperativeRowMapper cooperativeMapper;
    private final OrderRowMapper orderMapper;

    private static final Table entityTable = Table.aliased("jhi_order", EntityManager.ENTITY_ALIAS);
    private static final Table customerTable = Table.aliased("customer", "customer");
    private static final Table cooperativeTable = Table.aliased("cooperative", "cooperative");

    public OrderRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CustomerRowMapper customerMapper,
        CooperativeRowMapper cooperativeMapper,
        OrderRowMapper orderMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Order.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.customerMapper = customerMapper;
        this.cooperativeMapper = cooperativeMapper;
        this.orderMapper = orderMapper;
    }

    @Override
    public Flux<Order> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Order> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = OrderSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CustomerSqlHelper.getColumns(customerTable, "customer"));
        columns.addAll(CooperativeSqlHelper.getColumns(cooperativeTable, "cooperative"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(customerTable)
            .on(Column.create("customer_id", entityTable))
            .equals(Column.create("id", customerTable))
            .leftOuterJoin(cooperativeTable)
            .on(Column.create("cooperative_id", entityTable))
            .equals(Column.create("id", cooperativeTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Order.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Order> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Order> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Order process(Row row, RowMetadata metadata) {
        Order entity = orderMapper.apply(row, "e");
        entity.setCustomer(customerMapper.apply(row, "customer"));
        entity.setCooperative(cooperativeMapper.apply(row, "cooperative"));
        return entity;
    }

    @Override
    public <S extends Order> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
