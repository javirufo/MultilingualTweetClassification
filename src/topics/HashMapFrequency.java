package topics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HashMapFrequency extends HashMap<String, List<Integer>> {
    public void put(String key, Integer number) {
        List<Integer> current = get(key);
        if (current == null) {
            current = new ArrayList<Integer>();
            super.put(key, current);
        }
        current.add(number);
    }
    
    public static HashMapFrequency sortByComparator(Map unsortMap) {
		 
		List list = new LinkedList(unsortMap.entrySet());
 
		// sort list based on comparator
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o2)).getValue())
                                       .compareTo(((Map.Entry) (o1)).getValue());
			}
		});
 
		// put sorted list into map again
                //LinkedHashMap make sure order in which keys were inserted
		HashMapFrequency sortedMap = new HashMapFrequency();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey().toString(), new Integer(entry.getValue().toString()));
		}
		return sortedMap;
	}	    
}

