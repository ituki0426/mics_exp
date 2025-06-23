import java.lang.reflect.Array;
import java.util.ArrayList;

class Student {
    private String id;   // 学籍番号
    private String name; // 名前
    private int grade;   // 成績
    public Student(String id, String name, int grade) {
        this.id = id;
        this.name = name;
        if(grade < 0){
            this.grade = 0;
        }else if(grade > 100){
            this.grade = 100;
        }else{
            this.grade = grade;
        }
    }
    public void printShort() {
        System.out.println(id + ", " + name + ", " + grade);  
    }
    public String getId() {
        return id;
    }
    public void print() {
      System.out.println("ID   : " + id);  
      System.out.println("Name : " + name);
      System.out.println("Grade: " + grade);
    }
  }
  
class Lesson {
    private String name;    // 課題名
    private String teacher; // 担当者 
    private int num;        // 登録履修者数
    private ArrayList<Student> st; // StudentのArrayList
    public Lesson(String name, String teacher) {
      this.name = name;
      this.teacher = teacher; 
      this.st = new ArrayList<Student>(); // ArrayListの生成
    }
    public void add(Student s) {
      Boolean found = false; // 既に登録されているかどうかのフラグ
      for(int i = 0; i < this.st.size(); i++) {
        if(this.st.get(i).getId().compareTo(s.getId()) > 0) {
          found = true; // 既に登録されている
          this.st.add(i, s); // 登録済みのStudentの前に追加
          break; // ループを抜ける
        }
      }
        if(!found) {
            this.st.add(s); // 登録済みのStudentがいなかったので末尾に追加
        }
    }
    public void print() {
   
      // ここに System.out.print や Studentオブジェクトの
      // 中身の表示(printShortメソッドの呼び出し)を追加しましょう．
      System.out.println("Lesson            : " + name);
      System.out.println("Teacher           : " + teacher);
      System.out.println("Number of Students: " + num);
      for(int i = 0; i < this.st.size(); i++) {
        this.st.get(i).printShort();
      }
      System.out.println("----------");  
    }
  }    

class Main7 {
    public static void main(String[] args) {
    Lesson l = new Lesson("Pro Enshu", "Yanai");
    l.add(new Student("000010", "Dentsu Itiro", -100));
    l.add(new Student("000005", "Dentsu Jiro", 200));
    l.add(new Student("000002", "Dentsu Saburo", 60));
    l.add(new Student("000060", "Dentsu Shiro", 40));
    l.add(new Student("000006", "Dentsu Goro", 80));
    l.print();
    }
}
/**
Lesson            : Pro Enshu
Teacher           : Yanai
Number of Students: 0
000002, Dentsu Saburo, 60
000005, Dentsu Jiro, 100
000006, Dentsu Goro, 80
000010, Dentsu Itiro, 0
000060, Dentsu Shiro, 40
---------- 
*/