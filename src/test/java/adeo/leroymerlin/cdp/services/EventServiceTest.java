package adeo.leroymerlin.cdp.services;


import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;

import adeo.leroymerlin.cdp.datas.Band;
import adeo.leroymerlin.cdp.datas.Event;
import adeo.leroymerlin.cdp.datas.EventNotFoundException;
import adeo.leroymerlin.cdp.datas.Member;
import adeo.leroymerlin.cdp.repositories.EventRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

  @Test
  public void shouldReturnOnlyMatchingEvent_whenCallingFilteredEvents() {
    String matchingCriteriaName = "nameWithCrItERiaInside";
    String unMatchingCriteriaName = "nameWithCrItERInside";
    Event matchingEvent = createCompleteEventToFilter(matchingCriteriaName, unMatchingCriteriaName);
    Event unMatchingEvent = createCompleteEventToFilter(unMatchingCriteriaName, "cocoon");
    given(eventRepository.findAllBy()).willReturn(asList(matchingEvent, unMatchingEvent));
    final List<Event> filteredEvents = eventService.getFilteredEvents("criteria");
    assertEquals("Only matching element should be returned when filtering", 1, filteredEvents.size());
    assertTrue("only matching name should be returned", filteredEvents.get(0).getBands().stream().anyMatch(band ->
        band.getMembers().stream().anyMatch(member -> matchingCriteriaName.equalsIgnoreCase(member.getName()))
    ));
  }

  private Event createCompleteEventToFilter(String... names) {
    Event matchingEvent = new Event();
    matchingEvent.setId(1L);
    matchingEvent.setTitle("title");
    Set<Band> bands = new HashSet<>();
    Band band = new Band();
    band.setName("marvelous name");
    Set<Member> members = new HashSet<>();
    for (String name : names) {
      Member member = new Member();
      member.setName(name);
      members.add(member);
    }
    band.setMembers(members);
    bands.add(band);
    matchingEvent.setBands(bands);
    return matchingEvent;
  }
}
