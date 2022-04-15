package info4.gl.dm.coopcycle.repository;

import info4.gl.dm.coopcycle.domain.DeliveryPerson;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the DeliveryPerson entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeliveryPersonRepository extends ReactiveCrudRepository<DeliveryPerson, Long>, DeliveryPersonRepositoryInternal {
    @Override
    <S extends DeliveryPerson> Mono<S> save(S entity);

    @Override
    Flux<DeliveryPerson> findAll();

    @Override
    Mono<DeliveryPerson> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface DeliveryPersonRepositoryInternal {
    <S extends DeliveryPerson> Mono<S> save(S entity);

    Flux<DeliveryPerson> findAllBy(Pageable pageable);

    Flux<DeliveryPerson> findAll();

    Mono<DeliveryPerson> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<DeliveryPerson> findAllBy(Pageable pageable, Criteria criteria);

}
