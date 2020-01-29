package adeo.leroymerlin.cdp.services;


import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import adeo.leroymerlin.cdp.datas.Event;
import adeo.leroymerlin.cdp.repositories.EventRepository;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EventServiceTest {

  @Mock
  EventRepository eventRepository;
  @InjectMocks
  EventService eventService;

  @Test
  public void shouldCallEventRepositoryFindAllBy_whenCallingGetEvents() {
    eventService.getEvents();
    verify(eventRepository).findAllBy();
  }

  @Test
  public void shouldReturnEvents_whenCallingGetEvents() {
    Event event = new Event();
    event.setTitle("title");
    event.setId(1L);
    given(eventRepository.findAllBy()).willReturn(singletonList(event));

    final List<Event> events = eventService.getEvents();

    assertEquals("should return only one event", 1, events.size());
    assertEquals("should return correct  event", events.get(0), event);
  }

  @Test
  public void shouldCallEventRepositoryDelete_whenCallingDeleteEvent() {
    eventService.delete(1L);
    verify(eventRepository).delete(1L);
  }

  @Test
  public void shouldCallEventRepositoryFindAllBy_whenCallingGetFilteredEvents() {
    eventService.getFilteredEvents("criteria");
    verify(eventRepository).findAllBy();
  }
}
