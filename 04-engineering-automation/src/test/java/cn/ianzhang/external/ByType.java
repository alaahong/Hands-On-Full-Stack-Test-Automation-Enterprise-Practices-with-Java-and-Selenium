package cn.ianzhang.external;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @Author: Ian
 * @Description:
 */
public enum ByType {
    ID("id"),
    NAME("name"),
    CLASS("class"),
    LINK_TEXT("linktext"),
    PARTIAL_LINK_TEXT("partiallinktext"),
    TAG("tag"),
    CSS("css"),
    XPATH("xpath"),
    OTHER("other");
    @JsonValue
    private final String type;

    ByType(String type) {
        this.type = type;
    }
}
