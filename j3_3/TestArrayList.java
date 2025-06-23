import java.util.*;

public class TestArrayList {
    public static void main(String[] args) {
        ArrayList<String> a;
        a = new ArrayList<String>();
        a.add("abc");
        a.add("bcd");
        a.add("cde");
        for(int i = 0 ; i < a.size(); i++) {
            String s = a.get(i);
            System.out.println(s);
        }
    }
}
