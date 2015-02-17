package z.cube.param.spring;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class ParameterInvocation {
    /**
     * 参数传入值
     */
    private Object paramValue;
    /**
     * 参数名称
     */
    private String paramName;
    /**
     * 参数类型
     */
    private Type paramType;
    /**
     * 当前参数注解
     */
    private Annotation paramAnnotation;
    /**
     * 宿主方法
     */
    private Method hostMethod;

    public Object getParamValue() {
        return paramValue;
    }

    public void setParamValue(Object paramValue) {
        this.paramValue = paramValue;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public Type getParamType() {
        return paramType;
    }

    public void setParamType(Type paramType) {
        this.paramType = paramType;
    }

    public Annotation getParamAnnotation() {
        return paramAnnotation;
    }

    public void setParamAnnotation(Annotation paramAnnotation) {
        this.paramAnnotation = paramAnnotation;
    }

    public Method getHostMethod() {
        return hostMethod;
    }

    public void setHostMethod(Method hostMethod) {
        this.hostMethod = hostMethod;
    }
}
