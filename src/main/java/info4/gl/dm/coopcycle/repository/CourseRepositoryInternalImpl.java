package info4.gl.dm.coopcycle.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import info4.gl.dm.coopcycle.domain.Course;
import info4.gl.dm.coopcycle.repository.rowmapper.CourseRowMapper;
import info4.gl.dm.coopcycle.repository.rowmapper.DeliveryPersonRowMapper;
import info4.gl.dm.coopcycle.repository.rowmapper.OrderRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
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
 * Spring Data SQL reactive custom repository implementation for the Course entity.
 */
@SuppressWarnings("unused")
class CourseRepositoryInternalImpl extends SimpleR2dbcRepository<Course, Long> implements CourseRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final OrderRowMapper orderMapper;
    private final DeliveryPersonRowMapper deliverypersonMapper;
    private final CourseRowMapper courseMapper;

    private static final Table entityTable = Table.aliased("course", EntityManager.ENTITY_ALIAS);
    private static final Table orderTable = Table.aliased("jhi_order", "e_order");
    private static final Table deliveryPersonTable = Table.aliased("delivery_person", "deliveryPerson");

    public CourseRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        OrderRowMapper orderMapper,
        DeliveryPersonRowMapper deliverypersonMapper,
        CourseRowMapper courseMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Course.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.orderMapper = orderMapper;
        this.deliverypersonMapper = deliverypersonMapper;
        this.courseMapper = courseMapper;
    }

    @Override
    public Flux<Course> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Course> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = CourseSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(OrderSqlHelper.getColumns(orderTable, "order"));
        columns.addAll(DeliveryPersonSqlHelper.getColumns(deliveryPersonTable, "deliveryPerson"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(orderTable)
            .on(Column.create("order_id", entityTable))
            .equals(Column.create("id", orderTable))
            .leftOuterJoin(deliveryPersonTable)
            .on(Column.create("delivery_person_id", entityTable))
            .equals(Column.create("id", deliveryPersonTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Course.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Course> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Course> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Course process(Row row, RowMetadata metadata) {
        Course entity = courseMapper.apply(row, "e");
        entity.setOrder(orderMapper.apply(row, "order"));
        entity.setDeliveryPerson(deliverypersonMapper.apply(row, "deliveryPerson"));
        return entity;
    }

    @Override
    public <S extends Course> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
