package info4.gl.dm.coopcycle.service;

import info4.gl.dm.coopcycle.domain.Order;
import info4.gl.dm.coopcycle.repository.OrderRepository;
import info4.gl.dm.coopcycle.service.dto.OrderDTO;
import info4.gl.dm.coopcycle.service.mapper.OrderMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Order}.
 */
@Service
@Transactional
public class OrderService {

    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    /**
     * Save a order.
     *
     * @param orderDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<OrderDTO> save(OrderDTO orderDTO) {
        log.debug("Request to save Order : {}", orderDTO);
        return orderRepository.save(orderMapper.toEntity(orderDTO)).map(orderMapper::toDto);
    }

    /**
     * Update a order.
     *
     * @param orderDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<OrderDTO> update(OrderDTO orderDTO) {
        log.debug("Request to save Order : {}", orderDTO);
        return orderRepository.save(orderMapper.toEntity(orderDTO)).map(orderMapper::toDto);
    }

    /**
     * Partially update a order.
     *
     * @param orderDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<OrderDTO> partialUpdate(OrderDTO orderDTO) {
        log.debug("Request to partially update Order : {}", orderDTO);

        return orderRepository
            .findById(orderDTO.getId())
            .map(existingOrder -> {
                orderMapper.partialUpdate(existingOrder, orderDTO);

                return existingOrder;
            })
            .flatMap(orderRepository::save)
            .map(orderMapper::toDto);
    }

    /**
     * Get all the orders.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<OrderDTO> findAll() {
        log.debug("Request to get all Orders");
        return orderRepository.findAll().map(orderMapper::toDto);
    }

    /**
     *  Get all the orders where Course is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<OrderDTO> findAllWhereCourseIsNull() {
        log.debug("Request to get all orders where Course is null");
        return orderRepository.findAllWhereCourseIsNull().map(orderMapper::toDto);
    }

    /**
     *  Get all the orders where Payment is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<OrderDTO> findAllWherePaymentIsNull() {
        log.debug("Request to get all orders where Payment is null");
        return orderRepository.findAllWherePaymentIsNull().map(orderMapper::toDto);
    }

    /**
     * Returns the number of orders available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return orderRepository.count();
    }

    /**
     * Get one order by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<OrderDTO> findOne(Long id) {
        log.debug("Request to get Order : {}", id);
        return orderRepository.findById(id).map(orderMapper::toDto);
    }

    /**
     * Delete the order by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Order : {}", id);
        return orderRepository.deleteById(id);
    }
}
