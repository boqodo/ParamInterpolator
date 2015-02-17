package z.cube.param.spring;


import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import static z.cube.param.spring.AspectJExpressionUtils.*;

public abstract class AbstractAdvisorProvider implements AdvisorProvider {
    private static final ParameterNameDiscoverer PND = new LocalVariableTableParameterNameDiscoverer();

    protected abstract Class<? extends Annotation> getAnnotation();

    protected abstract Object parameterHandler(ParameterInvocation parameterInvocation);

    protected MethodInterceptor getInterceptor() {
        return new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                Method invokedMethod = invocation.getMethod();
                Annotation[][] paramAnns = invokedMethod.getParameterAnnotations();

                //传入参数的对象数组
                Object[] parameters = invocation.getArguments();

                //传入参数的名称数组
                String[] parameterNames = PND.getParameterNames(invokedMethod);

                //传入参数的对象类型
                Type[] paramTypes = invokedMethod.getGenericParameterTypes();

                //逐个基本类型参数处理
                for (int i = 0; i < paramAnns.length; i++) {
                    Annotation[] paramAnn = paramAnns[i];
                    for (int j = 0; j < paramAnn.length; j++) {
                        Annotation curAnn = paramAnn[j];
                        if (curAnn.annotationType().equals(getAnnotation())) {

                            String paramName = parameterNames[i];
                            Type paramType = paramTypes[i];

                            ParameterInvocation parameterInvocation = new ParameterInvocation();
                            parameterInvocation.setHostMethod(invokedMethod);
                            parameterInvocation.setParamType(paramType);
                            parameterInvocation.setParamName(paramName);
                            parameterInvocation.setParamAnnotation(curAnn);
                            parameterInvocation.setParamValue(parameters[i]);

                            parameters[i] = parameterHandler(parameterInvocation);

                            break;
                        }
                    }
                }
                return invocation.proceed();
            }
        };
    }

    @Override
    public Advisor getAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        Class clazz = getAnnotation();
        String expression = or(onParam(clazz), onClazz(clazz));
        Validate.isTrue(StringUtils.isNotBlank(expression), "AOP切入表达式不能为空!");
        pointcut.setExpression(expression);
        Advice advice = getInterceptor();
        return new DefaultPointcutAdvisor(pointcut, advice);
    }

}
