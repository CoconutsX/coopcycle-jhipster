package info4.gl.dm.coopcycle.web.rest;

import info4.gl.dm.coopcycle.repository.DeliveryPersonRepository;
import info4.gl.dm.coopcycle.service.DeliveryPersonService;
import info4.gl.dm.coopcycle.service.dto.DeliveryPersonDTO;
import info4.gl.dm.coopcycle.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link info4.gl.dm.coopcycle.domain.DeliveryPerson}.
 */
@RestController
@RequestMapping("/api")
public class DeliveryPersonResource {

    private final Logger log = LoggerFactory.getLogger(DeliveryPersonResource.class);

    private static final String ENTITY_NAME = "deliveryPerson";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeliveryPersonService deliveryPersonService;

    private final DeliveryPersonRepository deliveryPersonRepository;

    public DeliveryPersonResource(DeliveryPersonService deliveryPersonService, DeliveryPersonRepository deliveryPersonRepository) {
        this.deliveryPersonService = deliveryPersonService;
        this.deliveryPersonRepository = deliveryPersonRepository;
    }

    /**
     * {@code POST  /delivery-people} : Create a new deliveryPerson.
     *
     * @param deliveryPersonDTO the deliveryPersonDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deliveryPersonDTO, or with status {@code 400 (Bad Request)} if the deliveryPerson has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/delivery-people")
    public Mono<ResponseEntity<DeliveryPersonDTO>> createDeliveryPerson(@Valid @RequestBody DeliveryPersonDTO deliveryPersonDTO)
        throws URISyntaxException {
        log.debug("REST request to save DeliveryPerson : {}", deliveryPersonDTO);
        if (deliveryPersonDTO.getId() != null) {
            throw new BadRequestAlertException("A new deliveryPerson cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return deliveryPersonService
            .save(deliveryPersonDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/delivery-people/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /delivery-people/:id} : Updates an existing deliveryPerson.
     *
     * @param id the id of the deliveryPersonDTO to save.
     * @param deliveryPersonDTO the deliveryPersonDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deliveryPersonDTO,
     * or with status {@code 400 (Bad Request)} if the deliveryPersonDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deliveryPersonDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/delivery-people/{id}")
    public Mono<ResponseEntity<DeliveryPersonDTO>> updateDeliveryPerson(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DeliveryPersonDTO deliveryPersonDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DeliveryPerson : {}, {}", id, deliveryPersonDTO);
        if (deliveryPersonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deliveryPersonDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return deliveryPersonRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return deliveryPersonService
                    .update(deliveryPersonDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /delivery-people/:id} : Partial updates given fields of an existing deliveryPerson, field will ignore if it is null
     *
     * @param id the id of the deliveryPersonDTO to save.
     * @param deliveryPersonDTO the deliveryPersonDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deliveryPersonDTO,
     * or with status {@code 400 (Bad Request)} if the deliveryPersonDTO is not valid,
     * or with status {@code 404 (Not Found)} if the deliveryPersonDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the deliveryPersonDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/delivery-people/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<DeliveryPersonDTO>> partialUpdateDeliveryPerson(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DeliveryPersonDTO deliveryPersonDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DeliveryPerson partially : {}, {}", id, deliveryPersonDTO);
        if (deliveryPersonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deliveryPersonDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return deliveryPersonRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<DeliveryPersonDTO> result = deliveryPersonService.partialUpdate(deliveryPersonDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /delivery-people} : get all the deliveryPeople.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deliveryPeople in body.
     */
    @GetMapping("/delivery-people")
    public Mono<List<DeliveryPersonDTO>> getAllDeliveryPeople() {
        log.debug("REST request to get all DeliveryPeople");
        return deliveryPersonService.findAll().collectList();
    }

    /**
     * {@code GET  /delivery-people} : get all the deliveryPeople as a stream.
     * @return the {@link Flux} of deliveryPeople.
     */
    @GetMapping(value = "/delivery-people", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<DeliveryPersonDTO> getAllDeliveryPeopleAsStream() {
        log.debug("REST request to get all DeliveryPeople as a stream");
        return deliveryPersonService.findAll();
    }

    /**
     * {@code GET  /delivery-people/:id} : get the "id" deliveryPerson.
     *
     * @param id the id of the deliveryPersonDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deliveryPersonDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/delivery-people/{id}")
    public Mono<ResponseEntity<DeliveryPersonDTO>> getDeliveryPerson(@PathVariable Long id) {
        log.debug("REST request to get DeliveryPerson : {}", id);
        Mono<DeliveryPersonDTO> deliveryPersonDTO = deliveryPersonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deliveryPersonDTO);
    }

    /**
     * {@code DELETE  /delivery-people/:id} : delete the "id" deliveryPerson.
     *
     * @param id the id of the deliveryPersonDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/delivery-people/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteDeliveryPerson(@PathVariable Long id) {
        log.debug("REST request to delete DeliveryPerson : {}", id);
        return deliveryPersonService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
