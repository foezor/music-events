package adeo.leroymerlin.cdp.controller;


import static java.util.Arrays.asList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import adeo.leroymerlin.cdp.datas.Event;
import adeo.leroymerlin.cdp.datas.EventNotFoundException;
import adeo.leroymerlin.cdp.services.EventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(EventController.class)
public class EventControllerTest {

  @Autowired
  private MockMvc mockedContextServer;
  @MockBean
  private EventService eventService;
  private final String BASE_URL = "/api/events/";
  private Event simpleEvent = new Event();
  private Event completeEvent = new Event();
  private String bodyContent;
  @Before
  public void setUp() throws JsonProcessingException {
    simpleEvent.setId(1L);
    completeEvent.setId(2L);
    completeEvent.setComment("fake comment");
    completeEvent.setImgUrl("http://awesomeImageUrl");
    completeEvent.setTitle("super event");
    ObjectMapper objectMapper = new ObjectMapper();
    bodyContent = objectMapper.writeValueAsString(completeEvent);
  }

  @Test
  public void shouldCall_eventServiceGetEvents_whenCallingGetOnEventsController() throws Exception {
    mockedContextServer.perform(get(BASE_URL));
    verify(eventService).getEvents();
  }

  @Test
  public void shouldReturnListOfEvents_whenCallingGetOnEventsController() throws Exception {
    given(eventService.getEvents()).willReturn(asList(simpleEvent, completeEvent));
    mockedContextServer.perform(get(BASE_URL))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].id").value(simpleEvent.getId()))
        .andExpect(jsonPath("$.[1].id").value(completeEvent.getId()))
        .andExpect(jsonPath("$.[1].comment").value(completeEvent.getComment()))
        .andExpect(jsonPath("$.[1].imgUrl").value(completeEvent.getImgUrl()))
        .andExpect(jsonPath("$.[1].title").value(completeEvent.getTitle()));
  }

  @Test
  public void shouldCall_eventServiceGetFilteredEvents_whenCallingGetOnEventsControllerForFilteredEvents() throws Exception {
    final String criteria = "criteria";
    mockedContextServer.perform(get(BASE_URL + "search/" + criteria));
    verify(eventService).getFilteredEvents(eq(criteria));
  }

  @Test
  public void shouldReturnListOfEvents_whenCallingGetOnEventsControllerForFilteredEvents() throws Exception {
    given(eventService.getFilteredEvents(anyString())).willReturn(asList(simpleEvent, completeEvent));
    mockedContextServer.perform(get(BASE_URL + "search/criteria"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].id").value(simpleEvent.getId()))
        .andExpect(jsonPath("$.[1].id").value(completeEvent.getId()))
        .andExpect(jsonPath("$.[1].comment").value(completeEvent.getComment()))
        .andExpect(jsonPath("$.[1].imgUrl").value(completeEvent.getImgUrl()))
        .andExpect(jsonPath("$.[1].title").value(completeEvent.getTitle()));
  }

  @Test
  public void shouldCall_eventServiceDelete_whenCallingDeleteOnEventController() throws Exception {
    mockedContextServer.perform(delete(BASE_URL + "1")).andExpect(status().isOk());
    verify(eventService).delete(1L);
  }

  @Test
  public void shouldCall_eventServiceUpdate_whenCallingUpdateOnEventController() throws Exception {
    mockedContextServer.perform(put(BASE_URL + "2").content(bodyContent).contentType(MediaType.APPLICATION_JSON));
    verify(eventService).updateEvent(any(Event.class));
  }

  @Test
  public void shouldReturn400BadParameter_whenCallingUpdateOnController_withDifferentIdInPathAndBody() throws Exception {
    mockedContextServer.perform(put(BASE_URL + "11")
        .content(bodyContent)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void shouldReturn404NotFound_whenCallingUpdateOnController_withNonExistingEventInDatabase() throws Exception {
    given(eventService.updateEvent(any(Event.class))).willThrow(new EventNotFoundException("message"));
    mockedContextServer.perform(put(BASE_URL + 2)
        .contentType(MediaType.APPLICATION_JSON)
        .content(bodyContent))
        .andExpect(status().isNotFound());

  }
}
