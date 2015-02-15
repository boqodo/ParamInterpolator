package z.cube.param.handler;

import flex.messaging.FlexContext;
import flex.messaging.FlexSession;
import org.junit.Test;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import z.cube.param.test.Complex;
import z.cube.param.test.Element;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class FlexSessionConfigHandlerTest {

    @Test
    public void testGetValue() throws Exception {
        FlexSession session= mock(FlexSession.class);

        String uuid= UUID.randomUUID().toString();
        Long number=Long.MAX_VALUE;
        String name="Test";
        Element element=new Element(uuid,number,name);

        when(session.getAttribute("element")).thenReturn(element);
        when(session.getAttribute("number")).thenReturn(element.getNumber());

        PodamFactory factory = new PodamFactoryImpl();
        Complex complex=factory.manufacturePojo(Complex.class);
        when(session.getAttribute("complex")).thenReturn(complex);

        FlexContext.setThreadLocalSession(session);
        FlexSessionConfigHandler configHandler=new FlexSessionConfigHandler();
        configHandler.init(null);
        String nameVal= (String) configHandler.getValue("element.name");
        assertEquals(name,nameVal);
        verify(session).getAttribute("element");


        Long numberValue= (Long) configHandler.getValue("number");
        assertEquals(number, numberValue);
        verify(session).getAttribute("number");

        String exVal= (String) configHandler.getValue("complex.elements[0].uuid");
        assertEquals(complex.getElements().get(0).getUuid(), exVal);
        verify(session).getAttribute("complex");
    }
}