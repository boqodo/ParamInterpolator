package z.cube.param.config;

import java.util.Date;

public final class Null {

    public static final Object  OBJECT  = $(Object.class);
    public static final String  STRING  = $(String.class);
    public static final Integer INTEGER = $(Integer.class);
    public static final Date    DATE    = $(Date.class);
    public static final Float   FLOAT   = $(Float.class);
    public static final Double  DOUBLE  = $(Double.class);
    public static final Long    LONG    = $(Long.class);

    public static final <T> T $(Class<T> t) {
        return null;
    }

    public static final <T> T $() {
        return null;
    }
}
