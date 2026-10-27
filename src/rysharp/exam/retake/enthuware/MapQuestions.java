package rysharp.exam.retake.enthuware;

import java.util.NavigableMap;
import java.util.TreeMap;

public class MapQuestions {
    static void main(String[] args) {
        NavigableMap<String, String> mymap = new TreeMap<String, String>();// a, aa, b, bb, c, cc
        mymap.put("a", "apple");
        mymap.put("b", "boy");
        mymap.put("c", "cat");
        mymap.put("aa", "apple1");
        mymap.put("bb", "boy1");
        mymap.put("cc", "cat1");

        mymap.pollLastEntry(); // pollLastEntry() removes cc	a, aa, b, bb, c
        mymap.pollFirstEntry(); // pollFirstEntry() removes a	aa, b, bb, c

        // tailMap("bb", false) creates a view of aa, b, bb, c - tailMap("bb", false) excludes bb.
        NavigableMap<String, String> tailmap = mymap.tailMap("bb", false);
        // mymap.tailMap("bb", true);  // bb, c

        System.out.println(tailmap.pollFirstEntry()); // c=cat - The view is backed by the original map.
        System.out.println(mymap.size()); // 3

        tailmap.put("d", "derp");
//        tailmap.put("aaa", "anus"); // IllegalArgumentException: key out of range
        System.out.println(tailmap);
        System.out.println(mymap);

//        var submap = tailmap.subMap("bb", "z");// IllegalArgumentException: fromKey out of range
        var submap = tailmap.subMap("bbb", "z");// bbb is after bb, so valid
        System.out.println(submap); // {d=derp}
        submap.clear(); // clear derp only
        System.out.println(submap); // {}
        System.out.println(mymap);// {aa=apple1, b=boy, bb=boy1} no derp
        /*
        A small challenge for later: Once you're comfortable with Q44,
        try predicting what happens when you call clear() on a subMap() view. That's the same underlying rule tested from another angle.
         */
    }
}
