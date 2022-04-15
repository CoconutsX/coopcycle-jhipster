package info4.gl.dm.coopcycle.service;

import info4.gl.dm.coopcycle.domain.Course;
import info4.gl.dm.coopcycle.repository.CourseRepository;
import info4.gl.dm.coopcycle.service.dto.CourseDTO;
import info4.gl.dm.coopcycle.service.mapper.CourseMapper;
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
 * Service Implementation for managing {@link Course}.
 */
@Service
@Transactional
public class CourseService {

    private final Logger log = LoggerFactory.getLogger(CourseService.class);

    private final CourseRepository courseRepository;

    private final CourseMapper courseMapper;

    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    /**
     * Save a course.
     *
     * @param courseDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CourseDTO> save(CourseDTO courseDTO) {
        log.debug("Request to save Course : {}", courseDTO);
        return courseRepository.save(courseMapper.toEntity(courseDTO)).map(courseMapper::toDto);
    }

    /**
     * Update a course.
     *
     * @param courseDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CourseDTO> update(CourseDTO courseDTO) {
        log.debug("Request to save Course : {}", courseDTO);
        return courseRepository.save(courseMapper.toEntity(courseDTO)).map(courseMapper::toDto);
    }

    /**
     * Partially update a course.
     *
     * @param courseDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CourseDTO> partialUpdate(CourseDTO courseDTO) {
        log.debug("Request to partially update Course : {}", courseDTO);

        return courseRepository
            .findById(courseDTO.getId())
            .map(existingCourse -> {
                courseMapper.partialUpdate(existingCourse, courseDTO);

                return existingCourse;
            })
            .flatMap(courseRepository::save)
            .map(courseMapper::toDto);
    }

    /**
     * Get all the courses.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CourseDTO> findAll() {
        log.debug("Request to get all Courses");
        return courseRepository.findAll().map(courseMapper::toDto);
    }

    /**
     * Returns the number of courses available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return courseRepository.count();
    }

    /**
     * Get one course by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CourseDTO> findOne(Long id) {
        log.debug("Request to get Course : {}", id);
        return courseRepository.findById(id).map(courseMapper::toDto);
    }

    /**
     * Delete the course by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Course : {}", id);
        return courseRepository.deleteById(id);
    }
}
