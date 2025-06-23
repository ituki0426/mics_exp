class Student {
    String id;   // 学籍番号
    String name; // 名前
    int grade;   // 成績
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
    if(num >= max){
        System.out.println("Error: Student is full");
        return;
    }
      st[this.num++] = s; 
    }
    public void sortName(){
        for(int i = num; i > 1; i--){
            for(int j = 1; j < i; j++){
                if(st[j-1].name.compareTo(st[j].name) > 0){
                    Student tmp = st[j-1];
                    st[j-1] = st[j];
                    st[j] = tmp;
                }
            }
        }
    }
    public void sortGrade(){
        for(int i = num; i > 1; i--){
            for(int j = 1; j < i; j++){
                if(st[j-1].grade > st[j].grade){
                    Student tmp = st[j-1];
                    st[j-1] = st[j];
                    st[j] = tmp;
                }
            }
        }
    }
    public void sortId(){
        for(int i = num; i > 1; i--){
            for(int j = 1; j < i; j++){
                if(st[j-1].id.compareTo(st[j].id) > 0){
                    Student tmp = st[j-1];
                    st[j-1] = st[j];
                    st[j] = tmp;
                }
            }
        }
        
        
    }
    public void print() {
      System.out.println("Lesson            : " + name);
      System.out.println("Teacher           : " + teacher);
      System.out.println("Number of Students: " + num);
      for(int i = 0; i < num; i++) {
        st[i].printShort();
      }
      System.out.println("----------");  
    }
  }    

class Main9 {
    public static void main(String[] args) {
    Lesson l = new Lesson("Pro Enshu", "Yanai", 3);
    l.add(new Student("00006", "Dentsu Rokuro", 90));
    l.add(new Student("00007", "Dentsu Nanako", 70));
    l.add(new Student("00008", "Dentsu Hachiro", 50));
    l.add(new Student("00004", "Dentsu Shiro", 40));
    l.add(new Student("00005", "Dentsu Goro", 80));
    l.add(new Student("00009", "Dentsu Kyuro", 30));
    l.add(new Student("00010", "Dentsu Juro", 20));
    l.add(new Student("00001", "Dentsu Itiro", -100));
    l.add(new Student("00002", "Dentsu Jiro", 200));
    l.add(new Student("00003", "Dentsu Saburo", 60));
    System.out.println("sort by Grade");
    l.sortGrade();
    l.print();
    System.out.println("sort by Name");
    l.sortName();
    l.print();
    System.out.println("sort by ID");
    l.sortId();
    l.print();
    }
}

/** 
maxを超えて生徒を追加した時
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: Index 3 out of bounds for length 3
        at Lesson.add(work_9.java:36)
        at Main9.main(work_9.java:90)
*/ 

/**
修正版
Error: Student is full
Error: Student is full
Error: Student is full
Error: Student is full
Error: Student is full
Error: Student is full
Error: Student is full
sort by Grade
Lesson            : Pro Enshu
Teacher           : Yanai
Number of Students: 3
00008, Dentsu Hachiro, 50
00007, Dentsu Nanako, 70
00006, Dentsu Rokuro, 90
----------
sort by Name
Lesson            : Pro Enshu
Teacher           : Yanai
Number of Students: 3
00008, Dentsu Hachiro, 50
00007, Dentsu Nanako, 70
00006, Dentsu Rokuro, 90
----------
sort by ID
Lesson            : Pro Enshu
Teacher           : Yanai
Number of Students: 3
00006, Dentsu Rokuro, 90
00007, Dentsu Nanako, 70
00008, Dentsu Hachiro, 50
----------
*/
