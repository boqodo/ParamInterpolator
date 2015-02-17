package z.cube.param.test;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import z.cube.param.config.Config;
import z.cube.param.config.InitConfig;
import z.cube.param.spring.AdvisorProvider;
import z.cube.param.spring.MethodParameterInjectInterceptor;

import static z.cube.param.spring.AspectJExpressionUtils.*;


public class ConfigAdvisorProvider implements AdvisorProvider {
    private InitConfig initConfig;

    @Override
    public Advisor getAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        String expression = or(onParam(Config.class), onClazz(Config.class));
        pointcut.setExpression(expression);
        Advice advice = new MethodParameterInjectInterceptor(initConfig);
        return new DefaultPointcutAdvisor(pointcut, advice);
    }

    public void setInitConfig(InitConfig initConfig) {
        this.initConfig = initConfig;
    }
}
