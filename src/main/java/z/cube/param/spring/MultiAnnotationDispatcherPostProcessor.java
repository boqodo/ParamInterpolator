package z.cube.param.spring;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.aop.framework.ProxyConfig;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.util.ClassUtils;

import java.util.List;


public class MultiAnnotationDispatcherPostProcessor extends ProxyConfig implements BeanPostProcessor, BeanClassLoaderAware, Ordered {
    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    protected List<AdvisorProvider> advisorProviders;


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        //初始化前不处理
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof AopInfrastructureBean) {
            return bean;
        }
        if (advisorProviders == null || advisorProviders.isEmpty()) {
            return bean;
        } else {

            Object temp = bean;
            for (AdvisorProvider provider : advisorProviders) {
                Class<?> targetClass = AopUtils.getTargetClass(temp);
                boolean isEligible = AopUtils.canApply(provider.getAdvisor(), targetClass);
                if (isEligible) {
                    if (temp instanceof Advised) {
                        Advised advised = (Advised) temp;
                        advised.addAdvisor(provider.getAdvisor());
                    } else {
                        ProxyFactory proxyFactory = new ProxyFactory(bean);
                        // Copy our properties (proxyTargetClass etc) inherited from ProxyConfig.
                        proxyFactory.copyFrom(this);
                        proxyFactory.addAdvisor(provider.getAdvisor());
                        temp = proxyFactory.getProxy(this.beanClassLoader);
                    }
                }
            }
            return temp;
        }
    }


    public List<AdvisorProvider> getAdvisorProviders() {
        return advisorProviders;
    }

    public void setAdvisorProviders(List<AdvisorProvider> advisorProviders) {
        this.advisorProviders = advisorProviders;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = beanClassLoader;
    }

}
