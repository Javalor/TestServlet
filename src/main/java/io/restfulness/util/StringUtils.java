package io.restfulness.util;

import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class StringUtils {

    public static Predicate<String> IS_NOT_EMPTY = s -> s.length()>0;

    public static List<String> tokenize(String str, String delim) {
        return tokenize(str, delim, null, null);
    }

    public static List<String> tokenize(String str, String delim,
                                        List<Function<String, String>> mappers,
                                        List<Predicate<String>> filters) {

        if (str == null) {
            return Collections.emptyList();
        }

        StringTokenizer stringTokenizer = new StringTokenizer(str, delim);

        return Collections.list(stringTokenizer).stream()
                .map(Object::toString)
                .map(mapAll(mappers))
                .filter(filterAll(filters))
                .collect(Collectors.toList());
    }

    public static Function<String, String> mapAll(List<Function<String, String>> mappers) {
        return s -> {

            if (mappers == null) {
                return s;
            }

            String result = s;

            for (Function<String, String> mapper : mappers) {

                result = mapper.apply(result);

            }

            return result;
        };
    }

    public static Predicate<String> filterAll(List<Predicate<String>> filters) {
        return s -> {

            if (filters == null) {
                return true;
            }

            for (Predicate<String> filter : filters) {

                if (!filter.test(s)) {
                    return false;
                }

            }

            return true;
        };
    }

    public static int locateAnyChar(String str, List<Character> chars) {

        for (char c : chars) {
            int idx = str.indexOf(c);
            if (idx > -1) return idx;
        }

        return -1;
    }

}
