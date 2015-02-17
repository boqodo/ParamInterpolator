package z.cube.param.spring;

import org.springframework.aop.Advisor;


public interface AdvisorProvider {
    Advisor getAdvisor();
}
