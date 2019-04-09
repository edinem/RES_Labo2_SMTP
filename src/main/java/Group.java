import java.util.ArrayList;

public class Group {
    private Sender sender;
    private ArrayList<String> victims;

    Group(Sender sender){
        this.sender = sender;
        this.victims = new ArrayList();
    }

    public void addVictim(String victim){
        victims.add(victim);
    }

    public Sender getSender() {
        return sender;
    }

    public ArrayList<String> getVictims() {
        return victims;
    }
}
