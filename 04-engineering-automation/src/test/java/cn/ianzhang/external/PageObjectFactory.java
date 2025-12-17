package cn.ianzhang.external;

import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Ian
 * @Description:
 */
public class PageObjectFactory {
    private final ConcurrentHashMap<String, ByElement> map = new ConcurrentHashMap<>();

    private PageObjectFactory() {
    }

    public static PageObjectFactory getInstance() {
        return PageObjectFactoryInstance.INSTANCE;
    }

    public synchronized void putNewValue(String key, ByElement value) {
        if (map.containsKey(key)) {
            throw new RuntimeJsonMappingException("found duplicate key " + key);
        }
        map.put(key, value);
    }

    public ByElement lookupByElement(String key) {
        ByElement byElement = map.get(key);
        if (Objects.isNull(byElement)) {
            throw new NoSuchElementException(key);
        }
        return byElement;
    }

    public By lookupBy(String key) {
        ByElement byElement = map.get(key);
        if (Objects.isNull(byElement)) {
            throw new NoSuchElementException(key);
        }
        return convertByElementToBy(byElement);
    }

    private By convertByElementToBy(ByElement byElement) {
        switch (byElement.getType()) {
            case ID:
                return By.id(byElement.getValue());
            case NAME:
                return By.name(byElement.getValue());
            case CLASS:
                return By.className(byElement.getValue());
            case LINK_TEXT:
                return By.linkText(byElement.getValue());
            case PARTIAL_LINK_TEXT:
                return By.partialLinkText(byElement.getValue());
            case TAG:
                return By.tagName(byElement.getValue());
            case CSS:
                return By.cssSelector(byElement.getValue());
            case XPATH:
                return By.xpath(byElement.getValue());
            case OTHER:
            default:
                throw new NoSuchElementException(byElement.toString());
        }
    }

    private static class PageObjectFactoryInstance {
        private static final PageObjectFactory INSTANCE = new PageObjectFactory();
    }
}
