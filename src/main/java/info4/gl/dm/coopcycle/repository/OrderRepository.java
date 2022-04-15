package info4.gl.dm.coopcycle.repository;

import info4.gl.dm.coopcycle.domain.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Order entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, Long>, OrderRepositoryInternal {
    @Query("SELECT * FROM jhi_order entity WHERE entity.id not in (select course_id from course)")
    Flux<Order> findAllWhereCourseIsNull();

    @Query("SELECT * FROM jhi_order entity WHERE entity.id not in (select payment_id from payment)")
    Flux<Order> findAllWherePaymentIsNull();

    @Query("SELECT * FROM jhi_order entity WHERE entity.customer_id = :id")
    Flux<Order> findByCustomer(Long id);

    @Query("SELECT * FROM jhi_order entity WHERE entity.customer_id IS NULL")
    Flux<Order> findAllWhereCustomerIsNull();

    @Query("SELECT * FROM jhi_order entity WHERE entity.cooperative_id = :id")
    Flux<Order> findByCooperative(Long id);

    @Query("SELECT * FROM jhi_order entity WHERE entity.cooperative_id IS NULL")
    Flux<Order> findAllWhereCooperativeIsNull();

    @Override
    <S extends Order> Mono<S> save(S entity);

    @Override
    Flux<Order> findAll();

    @Override
    Mono<Order> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface OrderRepositoryInternal {
    <S extends Order> Mono<S> save(S entity);

    Flux<Order> findAllBy(Pageable pageable);

    Flux<Order> findAll();

    Mono<Order> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Order> findAllBy(Pageable pageable, Criteria criteria);

}
