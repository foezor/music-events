package adeo.leroymerlin.cdp.repositories;

import adeo.leroymerlin.cdp.datas.Event;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;


public interface EventRepository extends CrudRepository<Event, Long> {

  Optional<Event> findById(Long eventId);

  /**
   * we want to persist modification in database so the readOnly flag is only for this method since there is only read.
   *
   * @return list of all events.
   */
  @Transactional(readOnly = true)
    List<Event> findAllBy();
}
