package cn.ianzhang.external;

/**
 * @Author: Ian
 * @Description:
 */
public class ByElement {
    private String name;
    private ByType type;
    private String value;
    private String comment;

    @Override
    public String toString() {
        return "ByElement{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", value='" + value + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ByType getType() {
        return type;
    }

    public void setType(ByType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
