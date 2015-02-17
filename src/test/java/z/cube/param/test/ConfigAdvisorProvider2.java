package z.cube.param.test;

import org.aopalliance.intercept.MethodInterceptor;
import z.cube.param.config.Config;
import z.cube.param.config.InitConfig;
import z.cube.param.spring.AbstractAdvisorProvider;
import z.cube.param.spring.MethodParameterInjectInterceptor;
import z.cube.param.spring.ParameterInvocation;

import java.lang.annotation.Annotation;


public class ConfigAdvisorProvider2 extends AbstractAdvisorProvider {
    private InitConfig initConfig;


    @Override
    protected Class<? extends Annotation> getAnnotation() {
        return Config.class;
    }

    @Override
    protected Object parameterHandler(ParameterInvocation parameterInvocation) {
        throw new RuntimeException("由MethodParameterInjectInterceptor处理!");
    }

    @Override
    protected MethodInterceptor getInterceptor() {
        return new MethodParameterInjectInterceptor(initConfig);
    }

    public void setInitConfig(InitConfig initConfig) {
        this.initConfig = initConfig;
    }
}
