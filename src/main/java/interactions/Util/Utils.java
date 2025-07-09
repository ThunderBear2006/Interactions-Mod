package interactions.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {
    @SafeVarargs
    public static <T> List<T> CreateList(T... objects) {
        return new ArrayList<>(Arrays.asList(objects));
    }

    public static List<String> deserializeStringList(String str, String sub) {
        str = str.substring(1, str.length() - 1);
        if (str.isEmpty()) {
            return new ArrayList<>();
        } else {
            String[] values = str.split(sub);
            return new ArrayList<>(Arrays.asList(values));
        }
    }
}
