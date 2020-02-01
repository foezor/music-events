package adeo.leroymerlin.cdp.services;


import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;

import adeo.leroymerlin.cdp.datas.Event;
import adeo.leroymerlin.cdp.datas.EventNotFoundException;
import adeo.leroymerlin.cdp.repositories.EventRepository;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
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
  private Event event = new Event();

  @Before
  public void setUp() {
    event.setTitle("title");
    event.setId(1L);
  }

  @Test
  public void shouldCallEventRepositoryFindAllBy_whenCallingGetEvents() {
    eventService.getEvents();
    verify(eventRepository).findAllBy();
  }

  @Test
  public void shouldReturnEvents_whenCallingGetEvents() {
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

  @Test
  public void shouldCallEventRepositoryFind_whenCallingUpdate() {
    given(eventRepository.findById(anyLong())).willReturn(Optional.of(new Event()));
    eventService.updateEvent(event);
    verify(eventRepository).findById(anyLong());
  }

  @Test
  public void shouldCallEventRepositorySave_whenCallingUpdate_withExistingEvent() {
    given(eventRepository.findById(anyLong())).willReturn(Optional.of(new Event()));
    eventService.updateEvent(event);
    verify(eventRepository).save(any(Event.class));
  }

  @Test(expected = EventNotFoundException.class)
  public void shouldThrowNotFoundException_whenCallingUpdate_withNotExistingEvent() {
    given(eventRepository.findById(anyLong())).willReturn(Optional.empty());
    eventService.updateEvent(event);
  }
}
