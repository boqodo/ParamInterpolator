package z.cube.param.spring;

import org.junit.Test;
import z.cube.param.config.Config;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;
import static z.cube.param.spring.AspectJExpressionUtils.*;

public class AspectJExpressionUtilsTest {

    @Test
    public final void test(){
        String ex=onClazz(Config.class);
        String realEx="execution(* *(..,@z.cube.param.config.Config (*),..))";
        assertThat(ex).isNotEmpty().isEqualTo(realEx);

        String ex2=onParam(Config.class);
        String realEx2="execution(* *(..,(@ z.cube.param.config.Config *),..))";
        assertThat(ex2).isNotEmpty().isEqualTo(realEx2);

        String ex3=or(onClazz(Config.class),onParam(Config.class));

        String realEx3=realEx+" || "+realEx2;
        assertThat(ex3).isNotEmpty().isEqualTo(realEx3);

        String ex4=onMethod(Config.class);
        String realEx4="execution(@z.cube.param.config.Config * *(..))";
        assertThat(ex4).isNotEmpty().isEqualTo(realEx4);
    }


}