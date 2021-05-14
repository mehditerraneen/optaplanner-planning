package com.opefitoo.optaplannerplanning.sur.repo;

import com.opefitoo.optaplannerplanning.sur.model.Employee;
import com.opefitoo.optaplannerplanning.sur.model.Passage;
import com.opefitoo.optaplannerplanning.sur.model.Tournee;
import org.assertj.core.util.Lists;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.Optional;

public interface TourneeRepository extends PagingAndSortingRepository<Tournee, Long> {

    @Query("SELECT max(t.id) FROM Tournee as t")
    public Long findMaxId();

    public default Tournee anotherFindById(Long id) {
        // Occurs in a single transaction, so each initialized lesson references the same timeslot/room instance
        // that is contained by the timeTable's timeslotList/roomList.
        Optional<Tournee> t = this.findById(id);
        if(t.isPresent())
            return t.get();
        return new Tournee(id, new ArrayList<>(), new ArrayList<>());
    }

}
