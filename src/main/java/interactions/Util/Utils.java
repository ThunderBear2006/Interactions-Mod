package interactions.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {
    @SafeVarargs
    public static <T> List<T> CreateList(T... objects) {
        return new ArrayList<>(Arrays.asList(objects));
    }
}
