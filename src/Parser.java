import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Parser {

    private ArrayList<String> options;
    private SMTPServer server;
    private ArrayList<Group> groupList;
    private ArrayList<Message> messageList;

    Parser(){
        this.groupList = new ArrayList<>();
        this.messageList = new ArrayList<>();
    }

    public void readConfig(String fileName){
        try{
            File inputFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            Element server = (Element) doc.getElementsByTagName("server").item(0);
            NodeList groups = doc.getElementsByTagName("group");
            NodeList messages = doc.getElementsByTagName("message");

            System.out.println("Creating server...");
            String username = server.getElementsByTagName("username").item(0).getTextContent();
            String password = server.getElementsByTagName("password").item(0).getTextContent();
            String ip = server.getElementsByTagName("ip").item(0).getTextContent();
            int port = Integer.valueOf(server.getElementsByTagName("port").item(0).getTextContent());
            this.server = new SMTPServer(username,password,ip,port);
            System.out.println("Server created...");

            System.out.println("Creating groups...");
            for(int i = 0; i < groups.getLength(); ++i){
                    Node nGroup = groups.item(i);
                    if(nGroup.getNodeType() == Node.ELEMENT_NODE){
                        Element eGroup = (Element)nGroup;
                        Element fakemail = (Element)eGroup.getElementsByTagName("fakemail").item(0);
                        String firstName = fakemail.getElementsByTagName("firstname").item(0).getTextContent();
                        String lastName = fakemail.getElementsByTagName("lastname").item(0).getTextContent();
                        String mail = fakemail.getElementsByTagName("mail").item(0).getTextContent();

                        Attacker attacker = new Attacker(firstName,lastName,mail);

                        Group group = new Group(attacker);

                        NodeList victims = eGroup.getElementsByTagName("victim");
                        for(int j = 0; j < victims.getLength(); ++j){
                            Element eVictim = (Element)victims.item(j);
                            group.addVictim(eVictim.getTextContent());
                        }
                        this.groupList.add(group);
                    }
            }
            System.out.println("Groups created....");
            System.out.println("Creating messages...");
            for(int i = 0; i < messages.getLength(); ++i){
                Node nMessage = messages.item(i);
                if(nMessage.getNodeType() == Node.ELEMENT_NODE){
                    Element eMessage = (Element)nMessage;
                    String subject = eMessage.getElementsByTagName("subject").item(0).getTextContent();
                    String text = eMessage.getElementsByTagName("text").item(0).getTextContent();


                    Message m = new Message(subject,text);
                    this.messageList.add(m);
                }
            }
            System.out.println("Messages created...");
            System.out.println("Attack ready");

        }catch (Exception e){
            System.err.format("Exception occurred trying to read '%s'.", fileName);
            e.printStackTrace();
        }
    }


    public void attack() throws IOException {
        System.out.println("Starting attack...");
        server.makeConnection();
        Random rand = new Random();

        for (Group g: groupList) {
            Attacker a = g.getAttacker();
            ArrayList<String> victims = g.getVictims();

            for(String v : victims){
                Message m = messageList.get(rand.nextInt(messageList.size()));
                server.sendMail(a.getFirstname(),a.getLastname(),a.getMail(),v,m.getSubject(),m.getMessage());
            }
        }
        System.out.println("Attack finished...");
    }

    public static void main(String[] args) throws IOException {
            Parser p = new Parser();
            p.readConfig("config.xml");
            p.attack();
    }
}
