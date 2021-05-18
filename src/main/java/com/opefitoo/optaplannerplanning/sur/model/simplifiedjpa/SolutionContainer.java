package com.opefitoo.optaplannerplanning.sur.model.simplifiedjpa;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class SolutionContainer extends AbstractPersistableJpa {

    String month, year, score;

    @Column(columnDefinition = "jsonb")
    @Type(type = "jsonb")
    String solution;

    protected SolutionContainer() {

    }
}
