package adeo.leroymerlin.cdp.services;

import adeo.leroymerlin.cdp.datas.Band;
import adeo.leroymerlin.cdp.datas.Event;
import adeo.leroymerlin.cdp.datas.EventNotFoundException;
import adeo.leroymerlin.cdp.repositories.EventRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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
    final List<Event> filtered = events.stream().filter(
        event ->
            event.getBands().stream().anyMatch(
                brand ->
                    brand.getMembers().stream().anyMatch(
                        member -> member.getName().toLowerCase().contains(query.toLowerCase())
                    )
            )
    ).collect(Collectors.toList());
    filtered.forEach(event -> {
      final Set<Band> bands = event.getBands();
      NameDecorator.addNumbersOfChildToName(bands, event.getTitle(), event::setTitle);
      bands.forEach(band ->
          NameDecorator.addNumbersOfChildToName(band.getMembers(), band.getName(), band::setName)
      );
    });
    return filtered;

  }

  public Event updateEvent(Event event) {
    Optional<Event> optionalExistingEvent = eventRepository.findById(event.getId());
    optionalExistingEvent.orElseThrow(() -> new EventNotFoundException("Event not found in database"));
    return eventRepository.save(event);
  }
}
