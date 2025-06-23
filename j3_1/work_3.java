class Student {
    private String id;   // 学籍番号
    private String name; // 名前
    private int grade;   // 成績
    void setId(String id) {
        this.id = id;
      }
      void setName(String name) {
        this.name = name;
      }
      void setGrade(int grade) {
        this.grade = grade;
      }
    void print() {
      System.out.println("ID   : " + id);  
      System.out.println("Name : " + name);
      System.out.println("Grade: " + grade);
    }
  }
  
  class Main3 {
    public static void main(String[] args) {
      Student st  = new Student();
      Student st2 = new Student();
      st.setId("01110");  st2.setId("01111");   
      st.setName("Suzuki"); st2.setName("Yamada");
      st.setGrade(100); st2.setGrade(80);
  
      st.print();       
      st2.print();
    }
  }

/**
ID   : 01110
Name : Suzuki
Grade: 100
ID   : 01111
Name : Yamada
Grade: 80 
*/  