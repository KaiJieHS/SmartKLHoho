package my.edu.tarc.smartkltab;

public class user {
    private int id;
    private String name;
    private String phoneno;
    private String email;

    public user() {
    }

    public user(int id, String name, String phoneno, String email) {
        this.id=id;
        this.name = name;
        this.phoneno = phoneno;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
