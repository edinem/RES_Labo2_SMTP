import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        SpamAttack p = new SpamAttack();
        p.readConfig("configMockMock.xml");
        p.attack();

    }
}
