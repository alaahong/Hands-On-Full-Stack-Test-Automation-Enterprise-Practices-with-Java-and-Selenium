package cn.ianzhang;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.junit.platform.commons.util.Preconditions;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @Author: Ian
 * @Description:
 */
public class JsonFileArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<JsonFileSource> {
    private JsonFileSource annotation;
    private String[] file;
    private ObjectMapper objectMapper;

    @Override
    public void accept(JsonFileSource annotation) {
        this.annotation = annotation;
        this.file = this.annotation.file();
        this.objectMapper = new ObjectMapper(new JsonFactory());
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        Preconditions.notEmpty(this.file, "Files must not be empty");
        List<Order> argumentsList = new LinkedList<>();
        //    注册jsr310模块
        objectMapper.findAndRegisterModules();
        //    读取数据文件
        Arrays.stream(this.file)
                .map(f -> {
                            try {
                                return objectMapper.readValue(new File(f), Order.class);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .forEach(argumentsList::add);
        return Stream.of(Arguments.of(argumentsList));
    }
}
