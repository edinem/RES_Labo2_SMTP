import java.util.ArrayList;

public class Group {
    private Attacker attacker;
    private ArrayList<String> victims;

    Group(Attacker attacker){
        this.attacker = attacker;
        this.victims = new ArrayList<>();
    }

    public void addVictim(String victim){
        victims.add(victim);
    }
}
