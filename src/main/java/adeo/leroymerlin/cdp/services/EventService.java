package adeo.leroymerlin.cdp.services;

import adeo.leroymerlin.cdp.datas.Event;
import adeo.leroymerlin.cdp.repositories.EventRepository;
import java.util.List;
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
}
