package me.zhengjie.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HL
 * @create 2021/5/17 11:38
 */
public class DataChangeUtil {

    public static <T> List<T> castList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

}
