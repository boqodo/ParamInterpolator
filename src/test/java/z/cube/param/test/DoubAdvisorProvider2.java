package z.cube.param.test;

import z.cube.param.spring.AbstractAdvisorProvider;
import z.cube.param.spring.ParameterInvocation;

import java.lang.annotation.Annotation;


public class DoubAdvisorProvider2 extends AbstractAdvisorProvider {
    @Override
    protected Class<? extends Annotation> getAnnotation() {
        return Doub.class;
    }

    @Override
    protected Object parameterHandler(ParameterInvocation parameterInvocation) {
        Object parameter = parameterInvocation.getParamValue();
        if (parameter instanceof Integer) {
            parameter = (Integer) parameter * 2;
        }
        return parameter;
    }
}
