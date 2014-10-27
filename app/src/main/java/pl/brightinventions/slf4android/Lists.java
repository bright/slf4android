package pl.brightinventions.slf4android;

import java.util.ArrayList;
import java.util.List;

class Lists {
    public static List<String> newArrayList(Iterable<String> itemList) {
        ArrayList<String> result = new ArrayList<String>();
        for (String item : itemList) {
            result.add(item);
        }
        return result;
    }
}
