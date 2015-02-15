package z.cube.param.spring;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Test;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import z.cube.param.config.Config;
import z.cube.param.config.Null;
import z.cube.param.config.InitConfig;
import z.cube.param.test.Complex;
import z.cube.param.test.ConfigService;
import z.cube.param.test.Pub;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static z.cube.param.spring.AspectJExpressionUtils.*;

public class MethodParameterInjectInterceptorTest {

    @Test
    public void testInvokeByMock() throws Throwable {
        MethodInvocation methodInvocation= mock(MethodInvocation.class);
        Method method=MethodUtils.getAccessibleMethod(ConfigService.class,
        		"getWindowWidth", Integer.class);
        when(methodInvocation.getMethod()).thenReturn(method);
        Object[] arguments=new Object[]{Null.INTEGER};
        when(methodInvocation.getArguments()).thenReturn(arguments);

        MethodParameterInjectInterceptor mpii=new MethodParameterInjectInterceptor(buildInitConfig());
        mpii.invoke(methodInvocation);

        Object paramObj=arguments[0];

        //sizes.properties  window.width
        assertThat(paramObj).isNotNull().isNotEqualTo(Null.INTEGER).isEqualTo(500);

        verify(methodInvocation).getMethod();
        verify(methodInvocation).getArguments();
    }
    private InitConfig buildInitConfig(){
    	InitConfig ic=new InitConfig();
    	ic.setPropertiesFileNames("config/sizes.properties");
    	ic.setXmlFileNames("config/test.xml");
    	ic.setTableName("CONFIGURATION");
        ic.setKeyColumn("KEY");
    	ic.setValueColumn("value");
    	//ic.setDataSource(dataSource);
    	return ic;
    }

    @Test
    public void testInvokeByProxy(){
    	AspectJExpressionPointcut pointcut=new AspectJExpressionPointcut();
    	String expression = or(onParam(Config.class),onClazz(Config.class));
		pointcut.setExpression(expression);
        Advice advice = new MethodParameterInjectInterceptor(buildInitConfig());
        Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);

		// create proxy
        ConfigService target=new ConfigService();
		ProxyFactory pf = new ProxyFactory();
		pf.setTarget(target);
		pf.addAdvisor(advisor);
		ConfigService proxy = (ConfigService) pf.getProxy();
		proxy.getWindowWidth(Null.INTEGER);
		proxy.getPub(Null.$(Pub.class));
    }
}