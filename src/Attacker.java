public class Attacker {
    private String firstname;
    private String lastname;
    private String mail;

    Attacker(String firstname, String lastname, String mail){
        this.firstname = firstname;
        this.lastname = lastname;
        this.mail = mail;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getMail() {
        return mail;
    }
}
