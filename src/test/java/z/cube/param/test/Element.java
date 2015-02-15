package z.cube.param.test;

public class Element {
    private String uuid;
    private Long number;
    private String name;

    public Element() {
    }

    public Element(String uuid, Long number, String name) {
        this.uuid = uuid;
        this.number = number;
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
