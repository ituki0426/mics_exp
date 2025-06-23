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
    public void print() {
      System.out.println("ID   : " + id);  
      System.out.println("Name : " + name);
      System.out.println("Grade: " + grade);
    }
  }
  
class Lesson {
    private String name;    // 課題名
    private String teacher; // 担当者 
    private int max;        // 最大履修者数
    private int num;        // 登録履修者数
    private Student[] st;   // Studentの配列
         
    public Lesson(String name, String teacher, int max) {
      this.name = name;
      this.teacher = teacher;
      this.max = max;  
      this.num = 0;          // numは0に初期化
      st = new Student[max]; // 配列の確保(オブジェクトは別に生成する必要あり)
    }
    public void add(Student s) {
      st[this.num++] = s; 
    }
    public void print() {
   
      // ここに System.out.print や Studentオブジェクトの
      // 中身の表示(printShortメソッドの呼び出し)を追加しましょう．
      System.out.println("Lesson            : " + name);
      System.out.println("Teacher           : " + teacher);
      System.out.println("Number of Students: " + num);
      for(int i = 0; i < num; i++) {
        st[i].printShort();
      }
      System.out.println("----------");  
    }
  }    

class Main7 {
    public static void main(String[] args) {
    Lesson l = new Lesson("Pro Enshu", "Yanai", 100);
    l.add(new Student("00001", "Dentsu Itiro", -100));
    l.add(new Student("00002", "Dentsu Jiro", 200));
    l.add(new Student("00003", "Dentsu Saburo", 60));
    l.add(new Student("00004", "Dentsu Shiro", 40));
    l.add(new Student("00005", "Dentsu Goro", 80));
    l.print();
    }
}

/**
Lesson            : Pro Enshu
Teacher           : Yanai
Number of Students: 5
00001, Dentsu Itiro, 0
00002, Dentsu Jiro, 100
00003, Dentsu Saburo, 60
00004, Dentsu Shiro, 40
00005, Dentsu Goro, 80
----------
*/