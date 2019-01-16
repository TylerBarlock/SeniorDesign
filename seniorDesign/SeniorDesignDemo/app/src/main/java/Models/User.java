package Models;

public class User {
    public String fname;
    public String lname;
    public int weight;
    public int heightFeet;
    public int heightInches;
    public int age;
    public boolean smoker;

    public User(String fname, String lname, int weight, int heightFeet, int heightInches, int age, boolean smoker){
        this.fname = fname;
        this.lname = lname;
        this.weight = weight;
        this.heightFeet = heightFeet;
        this.heightInches = heightInches;
        this.age = age;
        this.smoker = smoker;
    }
}
