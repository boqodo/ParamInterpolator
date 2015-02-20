package z.cube.param.handler;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ConfigValidator {
    public static final Set<Annotation> INVALID = new CopyOnWriteArraySet<Annotation>();

    public static boolean success() {
        return INVALID.isEmpty();
    }
}
