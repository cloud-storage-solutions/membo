package io.membo.web.client.convert;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface Converter<T, R> {
    R convert(T from);

    List<R> convert(List<T> from);
}
