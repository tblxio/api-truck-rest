package dtb.lego.truck.rest.component.events.entity.repositories;

import dtb.lego.truck.rest.component.events.entity.events.Event;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Collection;

@NoRepositoryBean
public interface EventRepository<T extends Event> extends CrudRepository<T, Long> {

    @Query(value = "SELECT * FROM #{#entityName} a where timestamp between :begin and :end ;",
            nativeQuery = true)
    Collection<T> findEventsInInterval(@Param("begin") long begin,
                                       @Param("end") long end);

    @Query(value = "SELECT * FROM #{#entityName} ORDER BY timestamp DESC LIMIT 1;",
            nativeQuery = true)
    T findLastEvent();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM #{#entityName} where timestamp between :begin and :end ;",
            nativeQuery = true)
    void deleteEventsInInterval(@Param("begin") long begin,
                                @Param("end") long end);
}
