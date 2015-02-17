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
public class TrebleAdvisorProvider implements AdvisorProvider {
    @Override
    public Advisor getAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        String expression = or(onParam(Treble.class), onClazz(Treble.class));
        pointcut.setExpression(expression);
        Advice advice = new TrebleInterceptor();
        Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
        return advisor;
    }
}
