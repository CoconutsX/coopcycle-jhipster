package info4.gl.dm.coopcycle.repository;

import info4.gl.dm.coopcycle.domain.Course;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Course entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseRepository extends ReactiveCrudRepository<Course, Long>, CourseRepositoryInternal {
    @Query("SELECT * FROM course entity WHERE entity.order_id = :id")
    Flux<Course> findByOrder(Long id);

    @Query("SELECT * FROM course entity WHERE entity.order_id IS NULL")
    Flux<Course> findAllWhereOrderIsNull();

    @Query("SELECT * FROM course entity WHERE entity.delivery_person_id = :id")
    Flux<Course> findByDeliveryPerson(Long id);

    @Query("SELECT * FROM course entity WHERE entity.delivery_person_id IS NULL")
    Flux<Course> findAllWhereDeliveryPersonIsNull();

    @Override
    <S extends Course> Mono<S> save(S entity);

    @Override
    Flux<Course> findAll();

    @Override
    Mono<Course> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CourseRepositoryInternal {
    <S extends Course> Mono<S> save(S entity);

    Flux<Course> findAllBy(Pageable pageable);

    Flux<Course> findAll();

    Mono<Course> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Course> findAllBy(Pageable pageable, Criteria criteria);

}
