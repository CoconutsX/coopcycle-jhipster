package info4.gl.dm.coopcycle.service;

import info4.gl.dm.coopcycle.domain.DeliveryPerson;
import info4.gl.dm.coopcycle.repository.DeliveryPersonRepository;
import info4.gl.dm.coopcycle.service.dto.DeliveryPersonDTO;
import info4.gl.dm.coopcycle.service.mapper.DeliveryPersonMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link DeliveryPerson}.
 */
@Service
@Transactional
public class DeliveryPersonService {

    private final Logger log = LoggerFactory.getLogger(DeliveryPersonService.class);

    private final DeliveryPersonRepository deliveryPersonRepository;

    private final DeliveryPersonMapper deliveryPersonMapper;

    public DeliveryPersonService(DeliveryPersonRepository deliveryPersonRepository, DeliveryPersonMapper deliveryPersonMapper) {
        this.deliveryPersonRepository = deliveryPersonRepository;
        this.deliveryPersonMapper = deliveryPersonMapper;
    }

    /**
     * Save a deliveryPerson.
     *
     * @param deliveryPersonDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<DeliveryPersonDTO> save(DeliveryPersonDTO deliveryPersonDTO) {
        log.debug("Request to save DeliveryPerson : {}", deliveryPersonDTO);
        return deliveryPersonRepository.save(deliveryPersonMapper.toEntity(deliveryPersonDTO)).map(deliveryPersonMapper::toDto);
    }

    /**
     * Update a deliveryPerson.
     *
     * @param deliveryPersonDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<DeliveryPersonDTO> update(DeliveryPersonDTO deliveryPersonDTO) {
        log.debug("Request to save DeliveryPerson : {}", deliveryPersonDTO);
        return deliveryPersonRepository.save(deliveryPersonMapper.toEntity(deliveryPersonDTO)).map(deliveryPersonMapper::toDto);
    }

    /**
     * Partially update a deliveryPerson.
     *
     * @param deliveryPersonDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<DeliveryPersonDTO> partialUpdate(DeliveryPersonDTO deliveryPersonDTO) {
        log.debug("Request to partially update DeliveryPerson : {}", deliveryPersonDTO);

        return deliveryPersonRepository
            .findById(deliveryPersonDTO.getId())
            .map(existingDeliveryPerson -> {
                deliveryPersonMapper.partialUpdate(existingDeliveryPerson, deliveryPersonDTO);

                return existingDeliveryPerson;
            })
            .flatMap(deliveryPersonRepository::save)
            .map(deliveryPersonMapper::toDto);
    }

    /**
     * Get all the deliveryPeople.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<DeliveryPersonDTO> findAll() {
        log.debug("Request to get all DeliveryPeople");
        return deliveryPersonRepository.findAll().map(deliveryPersonMapper::toDto);
    }

    /**
     * Returns the number of deliveryPeople available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return deliveryPersonRepository.count();
    }

    /**
     * Get one deliveryPerson by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<DeliveryPersonDTO> findOne(Long id) {
        log.debug("Request to get DeliveryPerson : {}", id);
        return deliveryPersonRepository.findById(id).map(deliveryPersonMapper::toDto);
    }

    /**
     * Delete the deliveryPerson by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete DeliveryPerson : {}", id);
        return deliveryPersonRepository.deleteById(id);
    }
}
