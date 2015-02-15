package z.cube.param.spring;


import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;

/**
 * 将AspectJ表达式 用方法的形式表示，以加深印象和使用
 * http://jinnianshilongnian.iteye.com/blog/1420691
 */
public class AspectJExpressionUtils {


    public static String onClazz(Class<? extends Annotation> annotationClazz) {
        String template = "execution(* *(..,@%s (*),..))";
        return format(annotationClazz, template);
    }

    public static String onParam(Class<? extends Annotation> annotationClazz) {
        String template = "execution(* *(..,(@ %s *),..))";
        return format(annotationClazz, template);
    }

    public static String onMethod(Class<? extends Annotation> annotationClazz) {
        String template = "execution(@%s * *(..))";
        return format(annotationClazz, template);
    }

    public static String or(String expression1, String expression2) {
        return mor(expression1, expression2);
    }
    public static String mor(String ...expressions) {
        return StringUtils.join(expressions," || ");
    }

    private static String format(Class<? extends Annotation> annotationClazz, String template) {
        return String.format(template, annotationClazz.getName());
    }

}
