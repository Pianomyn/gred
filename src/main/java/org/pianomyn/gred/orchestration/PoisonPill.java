package org.pianomyn.gred.orchestration;

import java.nio.file.Path;

public class PoisonPill {
  // Marker to allow consumers to gracefully exit.
  public static final Path POISON_PILL = Path.of("__POISON_PILL__");
}
