import java.util.ArrayList;

public class Group {
    private String sender;
    private ArrayList<String> victims;

    Group(String sender){
        this.sender = sender;
        this.victims = new ArrayList();
    }

    public void addVictim(String victim){
        victims.add(victim);
    }

    public String getSender() {
        return sender;
    }

    public ArrayList<String> getVictims() {
        return victims;
    }
}
