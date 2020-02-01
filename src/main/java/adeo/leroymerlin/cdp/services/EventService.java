package adeo.leroymerlin.cdp.services;

import adeo.leroymerlin.cdp.datas.Event;
import adeo.leroymerlin.cdp.datas.EventNotFoundException;
import adeo.leroymerlin.cdp.repositories.EventRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class EventService {

  private final EventRepository eventRepository;

  public EventService(EventRepository eventRepository) {
    this.eventRepository = eventRepository;
  }

  public List<Event> getEvents() {
    return eventRepository.findAllBy();
  }

  public void delete(Long id) {
    eventRepository.delete(id);
  }

  public List<Event> getFilteredEvents(String query) {
    List<Event> events = eventRepository.findAllBy();
    // Filter the events list in pure JAVA here

    return events;
  }

  public Event updateEvent(Event event) {
    Optional<Event> optionalExistingEvent = eventRepository.findById(event.getId());
    optionalExistingEvent.orElseThrow(() -> new EventNotFoundException("Event not found in database"));
    return eventRepository.save(event);
  }
}
