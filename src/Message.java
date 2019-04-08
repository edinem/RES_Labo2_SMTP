import java.util.ArrayList;

public class Message {
    private String subject;
    private String message;

    Message(String subject, String message){
        this.subject = subject;
        this.message = message;
    }

    public String toString(){
        String tmp = subject + "\t" + message;
        return tmp;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }
}
