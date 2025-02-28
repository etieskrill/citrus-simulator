package org.citrusframework.simulator.service;

import jakarta.persistence.criteria.JoinType;
import org.citrusframework.simulator.model.TestParameter_;
import org.citrusframework.simulator.model.TestResult;
import org.citrusframework.simulator.model.TestResult_;
import org.citrusframework.simulator.repository.TestResultRepository;
import org.citrusframework.simulator.service.criteria.TestResultCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for executing complex queries for {@link TestResult} entities in the database.
 * The main input is a {@link TestResultCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TestResult} or a {@link Page} of {@link TestResult} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TestResultQueryService extends QueryService<TestResult> {

    private final Logger logger = LoggerFactory.getLogger(TestResultQueryService.class);

    private final TestResultRepository testResultRepository;

    public TestResultQueryService(TestResultRepository testResultRepository) {
        this.testResultRepository = testResultRepository;
    }

    /**
     * Return a {@link List} of {@link TestResult} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TestResult> findByCriteria(TestResultCriteria criteria) {
        logger.debug("find by criteria : {}", criteria);
        final Specification<TestResult> specification = createSpecification(criteria);
        return testResultRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TestResult} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TestResult> findByCriteria(TestResultCriteria criteria, Pageable page) {
        logger.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TestResult> specification = createSpecification(criteria);
        return testResultRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TestResultCriteria criteria) {
        logger.debug("count by criteria : {}", criteria);
        final Specification<TestResult> specification = createSpecification(criteria);
        return testResultRepository.count(specification);
    }

    /**
     * Function to convert {@link TestResultCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TestResult> createSpecification(TestResultCriteria criteria) {
        Specification<TestResult> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TestResult_.id));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStatus(), TestResult_.status));
            }
            if (criteria.getTestName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTestName(), TestResult_.testName));
            }
            if (criteria.getClassName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getClassName(), TestResult_.className));
            }
            if (criteria.getErrorMessage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getErrorMessage(), TestResult_.errorMessage));
            }
            if (criteria.getFailureStack() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFailureStack(), TestResult_.failureStack));
            }
            if (criteria.getFailureType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFailureType(), TestResult_.failureType));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), TestResult_.createdDate));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), TestResult_.lastModifiedDate));
            }
            if (criteria.getTestParameterKey() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestParameterKey(),
                            root -> root.join(TestResult_.testParameters, JoinType.LEFT).get(TestParameter_.testParameterId).get("key")
                        )
                    );
            }
        }
        return specification;
    }
}
