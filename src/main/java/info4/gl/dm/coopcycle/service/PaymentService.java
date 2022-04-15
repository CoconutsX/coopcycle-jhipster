package info4.gl.dm.coopcycle.service;

import info4.gl.dm.coopcycle.domain.Payment;
import info4.gl.dm.coopcycle.repository.PaymentRepository;
import info4.gl.dm.coopcycle.service.dto.PaymentDTO;
import info4.gl.dm.coopcycle.service.mapper.PaymentMapper;
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
 * Service Implementation for managing {@link Payment}.
 */
@Service
@Transactional
public class PaymentService {

    private final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;

    public PaymentService(PaymentRepository paymentRepository, PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
    }

    /**
     * Save a payment.
     *
     * @param paymentDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PaymentDTO> save(PaymentDTO paymentDTO) {
        log.debug("Request to save Payment : {}", paymentDTO);
        return paymentRepository.save(paymentMapper.toEntity(paymentDTO)).map(paymentMapper::toDto);
    }

    /**
     * Update a payment.
     *
     * @param paymentDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PaymentDTO> update(PaymentDTO paymentDTO) {
        log.debug("Request to save Payment : {}", paymentDTO);
        return paymentRepository.save(paymentMapper.toEntity(paymentDTO)).map(paymentMapper::toDto);
    }

    /**
     * Partially update a payment.
     *
     * @param paymentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<PaymentDTO> partialUpdate(PaymentDTO paymentDTO) {
        log.debug("Request to partially update Payment : {}", paymentDTO);

        return paymentRepository
            .findById(paymentDTO.getId())
            .map(existingPayment -> {
                paymentMapper.partialUpdate(existingPayment, paymentDTO);

                return existingPayment;
            })
            .flatMap(paymentRepository::save)
            .map(paymentMapper::toDto);
    }

    /**
     * Get all the payments.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PaymentDTO> findAll() {
        log.debug("Request to get all Payments");
        return paymentRepository.findAll().map(paymentMapper::toDto);
    }

    /**
     * Returns the number of payments available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return paymentRepository.count();
    }

    /**
     * Get one payment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<PaymentDTO> findOne(Long id) {
        log.debug("Request to get Payment : {}", id);
        return paymentRepository.findById(id).map(paymentMapper::toDto);
    }

    /**
     * Delete the payment by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Payment : {}", id);
        return paymentRepository.deleteById(id);
    }
}
