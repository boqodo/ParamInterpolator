package z.cube.param.spring;

import flex.messaging.FlexContext;
import flex.messaging.FlexSession;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import z.cube.param.config.Null;
import z.cube.param.test.ConfigService;
import z.cube.param.test.Element;
import z.cube.param.test.MultVO;
import z.cube.param.test.Pub;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SpringAppTest extends AbstractSpringTestCase {

    @Autowired
    private ConfigService configService;

    private final Long number=Long.MAX_VALUE;
    private final String name="Test";
    private final String uuid= UUID.randomUUID().toString();

    @Before
    public void setUp() throws Exception {

        FlexSession session= mock(FlexSession.class);
        Element element=new Element(uuid,number,name);
        when(session.getAttribute("element")).thenReturn(element);
        when(session.getAttribute("number")).thenReturn(element.getNumber());

        FlexContext.setThreadLocalSession(session);
    }

    @Test
    public final void testConfigProperties(){
        Integer width=this.configService.getWindowWidth(Null.INTEGER);
        assertThat(width).isNotNull().isEqualTo(500);

        String color=this.configService.getBackgroundColor(Null.STRING);
        assertThat(color).isNotEmpty().isEqualTo("#FFFFFF");
    }

    @Test
    public final void testConifgXml(){
        Integer msgNumber=this.configService.getXmlMsgNumber(Null.INTEGER);
        assertThat(msgNumber).isNotNull().isEqualTo(new Integer(1222));

        String dateTime=this.configService.getXmlReqDateTime(Null.STRING);
        assertThat(dateTime).isNotEmpty().isEqualTo("20150125");
    }
    @Test
    public final void testConfigDatabase(){
        Integer msgNumber=this.configService.getDBFirstValue(Null.INTEGER);
        assertThat(msgNumber).isNotNull().isEqualTo(new Integer(1));

        String second=this.configService.getDBSecondValue(Null.STRING);
        assertThat(second).isNotEmpty().isEqualTo("2");
    }

    @Test
    public final void testConfigFlexSession(){

        String rname=this.configService.getFlexElementName(Null.STRING);
        assertThat(rname).isNotEmpty().isEqualTo(name);
        Long rnumber=this.configService.getFlexNumber(Null.LONG);
        assertThat(rnumber).isNotNull().isEqualTo(number);

    }


    @Test
    public final void testCofigClass(){
        Pub pub=this.configService.getPub(Null.$(Pub.class));
        assertThat(pub).isNotNull();
        assertThat(pub.getDatetime()).isNotEmpty().isEqualTo("20140715");
        assertThat(pub.getMsgnumber()).isNotNull().isEqualTo(1222);
        assertThat(pub.getSender()).isNotNull().isEqualTo("1001");
        assertThat(pub.getReceiver()).isNotNull().isEqualTo("0011");
        assertThat(pub.getCode()).isNotNull().isEqualTo(1201);
    }
    @Test
    public final void testConfigMultVO(){
        MultVO mv=this.configService.getMultVO(Null.$(MultVO.class));

        assertThat(mv).isNotNull();
        assertThat(mv.getXml()).isNotNull().isEqualTo(new Integer(1222));
        assertThat(mv.getDatabase()).isNotNull().isEqualTo("3");
        assertThat(mv.getProperties()).isNotNull().isEqualTo("#FFFFFF");
        assertThat(mv.getFlexsession()).isNotNull().isEqualTo(Long.MAX_VALUE);
        assertThat(mv.getTownname()).isNotNull().isEqualTo(Integer.valueOf("330502201"));
    }
}
