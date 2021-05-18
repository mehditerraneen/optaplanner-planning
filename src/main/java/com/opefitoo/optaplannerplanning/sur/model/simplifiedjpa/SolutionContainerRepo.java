package com.opefitoo.optaplannerplanning.sur.model.simplifiedjpa;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Set;

@RepositoryRestResource(collectionResourceRel = "solutions", path = "solutions")
public interface SolutionContainerRepo extends PagingAndSortingRepository<SolutionContainer, Long> {

    Set<SolutionContainer> findByYearAndMonth(@Param("year") String year, @Param("month") String month);

}
