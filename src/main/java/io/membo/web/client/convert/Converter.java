package io.membo.web.client.convert;

import java.util.List;

public interface Converter<T, R> {
    R convert(T from);

    List<R> convert(List<T> from);
}
