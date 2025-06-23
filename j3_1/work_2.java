class Student {
  private String id;   // 学籍番号
  private String name; // 名前
  private int grade;   // 成績

  private void print() {
    System.out.println("ID   : " + id);  
    System.out.println("Name : " + name);
    System.out.println("Grade: " + grade);
  }
}

class Main2 {
  public static void main(String[] args) {
    Student st  = new Student();
    Student st2 = new Student();
    st.id    =  "01110"; st2.id    =  "01111";
    st.name  = "Suzuki"; st2.name  = "Yamada";
    st.grade =      100; st2.grade =       80;

    st.print();       
    st2.print();
  }
}
/** 
id,name,gradeにprivateアクセス修飾子をつけた場合のコンパイル結果
Student.java:17: エラー: idはStudentでprivateアクセスされます
    st.id    =  "01110"; st2.id    =  "01111";
      ^
Student.java:17: エラー: idはStudentでprivateアクセスされます
    st.id    =  "01110"; st2.id    =  "01111";
                            ^
Student.java:18: エラー: nameはStudentでprivateアクセスされます
    st.name  = "Suzuki"; st2.name  = "Yamada";
      ^
Student.java:18: エラー: nameはStudentでprivateアクセスされます
    st.name  = "Suzuki"; st2.name  = "Yamada";
                            ^
Student.java:19: エラー: gradeはStudentでprivateアクセスされます
    st.grade =      100; st2.grade =       80;
      ^
Student.java:19: エラー: gradeはStudentでprivateアクセスされます
    st.grade =      100; st2.grade =       80;
                            ^
エラー6個
エラー: コンパイルが失敗しました 
*/
/**
Studentのprintメソッドにprivateアクセス修飾子をつけた場合のコンパイル結果
Student.java:17: エラー: idはStudentでprivateアクセスされます
    st.id    =  "01110"; st2.id    =  "01111";
      ^
Student.java:17: エラー: idはStudentでprivateアクセスされます
    st.id    =  "01110"; st2.id    =  "01111";
                            ^
Student.java:18: エラー: nameはStudentでprivateアクセスされます
    st.name  = "Suzuki"; st2.name  = "Yamada";
      ^
Student.java:18: エラー: nameはStudentでprivateアクセスされます
    st.name  = "Suzuki"; st2.name  = "Yamada";
                            ^
Student.java:19: エラー: gradeはStudentでprivateアクセスされます
    st.grade =      100; st2.grade =       80;
      ^
Student.java:19: エラー: gradeはStudentでprivateアクセスされます
    st.grade =      100; st2.grade =       80;
                            ^
Student.java:21: エラー: print()はStudentでprivateアクセスされます
    st.print();       
      ^
Student.java:22: エラー: print()はStudentでprivateアクセスされます
    st2.print();
       ^
エラー8個
エラー: コンパイルが失敗しました
*/