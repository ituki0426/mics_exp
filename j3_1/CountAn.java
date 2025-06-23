public class CountAn {
    public static void main(String[] args) {
      String[] str = new String[5];
      int i;
      int count;
  
      str[0] = "a";
      str[1] = "ant";
      str[2] = "an";
      str[3] = "grasshopper";
      str[4] = "and";
  
      count = 0;
      for (i = 0; i < 5; i++) {
        if (str[i].compareTo("an") == 0) {
          count++;
      }
      }
      System.out.println("an: " + count);
  
      count = 0;
      for (i = 0; i < 5; i++){
        if(str[i].startsWith("an")) {
          count++;
        }
      }
      System.out.println("an...: " + count);
    }
}
