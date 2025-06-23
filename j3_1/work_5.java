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
        if(grade < 0){
            this.grade = 0;
        }else if(grade > 100){
            this.grade = 100;
        }else{
            this.grade = grade;
        }
      }
    void print() {
      System.out.println("ID   : " + id);  
      System.out.println("Name : " + name);
      System.out.println("Grade: " + grade);
    }
  }
  
  class Main5 {
    public static void main(String[] args) {
      Student st  = new Student();
      st.setId("01110");  
      st.setName("Suzuki");
      st.setGrade(200);
  
      st.print();       
    }
  }

/**
st.setGrade(100)の時
ID   : 01110
Name : Suzuki
Grade: 100
*/  

/**
st.setGrade(-10)の時
ID   : 01110
Name : Suzuki
Grade: 0
*/  

/**
st.setGrade(200)の時
ID   : 01110
Name : Suzuki
Grade: 100
*/