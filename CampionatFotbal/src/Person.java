public abstract class Person {
    private String lastName;
    private String firstName;
    private String nationality;
    private int age;

    public Person(String lastName, String firstName, int age, String nationality) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.age = age;
        this.nationality = nationality;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getNationality() {
        return nationality;
    }
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        if (age > 0 && age < 200) {
            this.age = age;
        } else {
            System.out.println("invalid age");
        }
    }
}