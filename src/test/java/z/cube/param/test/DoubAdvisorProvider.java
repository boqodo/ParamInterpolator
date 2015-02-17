package z.cube.param.test;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import z.cube.param.spring.AdvisorProvider;

import static z.cube.param.spring.AspectJExpressionUtils.*;

/**
 * Created by KingSoft on 2015/2/16.
 */
public class DoubAdvisorProvider implements AdvisorProvider {
    @Override
    public Advisor getAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        String expression = or(onParam(Doub.class), onClazz(Doub.class));
        pointcut.setExpression(expression);
        Advice advice = new DoubInterceptor();
        Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
        return advisor;
    }
}
