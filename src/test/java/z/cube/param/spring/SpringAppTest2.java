package z.cube.param.spring;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import z.cube.param.test.CustomService;

public class SpringAppTest2 extends AbstractSpringTestCase {
    @Autowired
    private CustomService customService;

    @Test
    public final void testCustomAnnotationInterceptor() {
        //定义一个Doub 双倍注解，用于参数为number类型的，对应值翻倍
        Integer i = 8;
        Integer r = this.customService.getDoubInt(i);
        Assertions.assertThat(r).isNotNull().isEqualTo(i * 2);

        Integer r2 = this.customService.getTrebleInt(i);
        Assertions.assertThat(r2).isNotNull().isEqualTo(i * 3);

        Integer r3 = this.customService.getDoubAndTrebleInt(i);
        Assertions.assertThat(r3).isNotNull().isEqualTo(i * 2 * 3);
    }

}
