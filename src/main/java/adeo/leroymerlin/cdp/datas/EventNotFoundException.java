package adeo.leroymerlin.cdp.datas;

/**
 * Class use to notify user whe he try to update a none existing Event.
 * Since we will use it to deal with correct Http return code only, we extend unchecked exception.
 * Pros:  non repetitive code, and highers method don't have to know about the lower implementation.
 * Cons: not really elegant, since some logic are stuck in Exception.
 * A better way can be to use Vavr or other kind of lib which can allows us to return FunctionalIssue or expected results.
 * But due to the restriction in readme...
 */

public class EventNotFoundException extends RuntimeException {

  public EventNotFoundException(String message) {
    super(message);
  }
}
