package z.cube.param.test;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class DoubInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method invokedMethod = invocation.getMethod();
        Annotation[][] paramAnns = invokedMethod.getParameterAnnotations();

        //传入参数的对象数组
        Object[] parameters = invocation.getArguments();
        //逐个基本类型参数处理
        for (int i = 0; i < paramAnns.length; i++) {
            Annotation[] paramAnn = paramAnns[i];
            for (int j = 0; j < paramAnn.length; j++) {
                if (paramAnn[j] instanceof Doub) {
                    Object parameter = parameters[i];
                    if (parameter instanceof Integer) {
                        parameters[i] = (Integer) parameter * 2;
                    }
                    break;
                }
            }
        }
        return invocation.proceed();
    }
}
