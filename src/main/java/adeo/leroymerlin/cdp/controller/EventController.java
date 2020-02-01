package adeo.leroymerlin.cdp.controller;

import adeo.leroymerlin.cdp.datas.ErrorMessage;
import adeo.leroymerlin.cdp.datas.Event;
import adeo.leroymerlin.cdp.datas.EventNotFoundException;
import adeo.leroymerlin.cdp.services.EventService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
public class EventController {

  private final EventService eventService;

  public EventController(EventService eventService) {
    this.eventService = eventService;
  }

  @GetMapping(value = "/")
  public List<Event> findEvents() {
    return eventService.getEvents();
  }

  @GetMapping(value = "/search/{query}")
  public List<Event> findEvents(@PathVariable String query) {
    return eventService.getFilteredEvents(query);
  }

  @DeleteMapping(value = "/{id}")
  public void deleteEvent(@PathVariable Long id) {
    eventService.delete(id);
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity updateEvent(@PathVariable Long id, @RequestBody Event event) {
    if (!id.equals(event.getId())) {
      return new ResponseEntity(new ErrorMessage("id in path not matching the one in the body."), HttpStatus.BAD_REQUEST);
    }
    eventService.updateEvent(event);
    return ResponseEntity.ok().build();
  }

  @ExceptionHandler(EventNotFoundException.class)
  public ResponseEntity<ErrorMessage> handleEventNotfound(Exception eventNotFound) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(eventNotFound.getMessage()));
  }
}
