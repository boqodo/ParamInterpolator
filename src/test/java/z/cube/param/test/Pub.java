package z.cube.param.test;


import z.cube.param.config.Config;
import z.cube.param.config.SourceType;

@Config(source = SourceType.XML)
public class Pub {
    private String datetime;
    private Integer msgnumber;
    private String sender;
    private String receiver;
    private Integer code;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Integer getMsgnumber() {
        return msgnumber;
    }

    public void setMsgnumber(Integer msgnumber) {
        this.msgnumber = msgnumber;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
