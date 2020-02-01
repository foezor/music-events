package adeo.leroymerlin.cdp.services;

import java.util.Set;

public class NameDecorator {

  private NameDecorator() {
    throw new IllegalStateException("Utility class");
  }

  public static <T> void addNumbersOfChildToName(Set<T> list, String name, NameImprover fieldToImprove) {
    if (list == null || list.isEmpty()) {
      return;
    }
    fieldToImprove.setResult(name + "[" + list.size() + "]");
  }
}
