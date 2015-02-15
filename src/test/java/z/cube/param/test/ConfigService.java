package z.cube.param.test;

import org.springframework.stereotype.Service;
import z.cube.param.config.Config;
import z.cube.param.config.SourceType;

@Service
public class ConfigService {

    public String getBackgroundColor(
            @Config(key = "colors.background", source = SourceType.PROPERTIES) String out) {
        return out;
    }

    public Integer getWindowWidth(@Config(key = "window.width", source = SourceType.PROPERTIES) Integer width) {
        return width;
    }

    public Integer getDBFirstValue(@Config(key = "First", source = SourceType.DATABASE) Integer in) {
        return in;
    }

    public String getDBSecondValue(@Config(key = "Second", source = SourceType.DATABASE) String f) {
        return f;
    }

    public Integer getXmlMsgNumber(@Config(key = "msgnumber", source = SourceType.XML) Integer f) {
        return f;
    }

    public String getXmlReqDateTime(@Config(key = "req.datetime", source = SourceType.XML) String f) {
        return f;
    }

    public String getFlexElementName(
            @Config(key="element.name",source = SourceType.FLEX)String name){
        return name;
    }
    public Long getFlexNumber(
            @Config(key="number",source = SourceType.FLEX)Long number){
        return number;
    }
    public Pub getPub(Pub pub) {
        return pub;
    }

    public MultVO getMultVO(MultVO vo) {
        return vo;
    }
}
