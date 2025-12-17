package cn.ianzhang.external;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * @Author: Ian
 * @Description:
 */
public class ByUtil {
    private final File directory;
    private final String[] extensions;
    private final boolean recursive;

    private ByUtil(final File directory, final String[] extensions, final boolean recursive) {
        this.directory = directory;
        this.extensions = extensions;
        this.recursive = recursive;
        try {
            loadData(directory, extensions, recursive);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ByUtil getInstance() {
        return ByUtilInstance.INSTANCE;
    }

    private static void loadData(final File directory, final String[] extensions, final boolean recursive) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());
        //    注册jsr310模块
        objectMapper.findAndRegisterModules();
        Collection<File> files = FileUtils.listFiles(directory, extensions, recursive);
        for (File file : files) {
            //    读取数据文件
            List<ByElement> byElementList = objectMapper.readValue(file, new TypeReference<>() {
            });
            byElementList.forEach(byElement -> PageObjectFactory.getInstance().putNewValue(byElement.getName(), byElement));
        }
    }

    public By lookup(String key) {
        return PageObjectFactory.getInstance().lookupBy(key);
    }

    private static class ByUtilInstance {
        private static final ByUtil INSTANCE = buildByUtil();


        private static ByUtil buildByUtil() {
            ByConfiguration configuration = null;
            try {
                configuration = new ObjectMapper(new YAMLFactory())
                        .readValue(new FileInputStream(new File("src/test/resources/by-configuration.yaml")), ByConfiguration.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return new ByUtil(new File(configuration.getDirectory())
                    , configuration.getExtensions()
                    , configuration.getRecursive());
        }
    }
}
