import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

public class Parser {

    private ArrayList<String> options;
    private SMTPServer server;
    private ArrayList<Group> groupList;
    private ArrayList<Message> messageList;

    Parser(){

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
            String ip = server.getElementsByTagName("ip").item(0).getTextContent();
            int port = Integer.valueOf(server.getElementsByTagName("port").item(0).getTextContent());
            this.server = new SMTPServer(ip,port);
            System.out.println("Server created...");

            System.out.println("Creating groups...");
            for(int i = 0; i < groups.getLength(); ++i){
                    Node nGroup = groups.item(i);
                    if(nGroup.getNodeType() == Node.ELEMENT_NODE){
                        Element eGroup = (Element)nGroup;
                        Element fakemail = (Element)eGroup.getElementsByTagName("fakemail").item(0);
                        String firstName = fakemail.getElementsByTagName("firstname").item(0).getTextContent();
                        String lastName = fakemail.getElementsByTagName("lastName").item(0).getTextContent();
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
                    Element eMessages = (Element)nMessage;
                    Element eMessage = (Element)eMessages.getElementsByTagName("message").item(0);
                    String subject = eMessage.getElementsByTagName("subject").item(0).getTextContent();
                    String text = eMessage.getElementsByTagName("text").item(0).getTextContent();

                    Message message = new Message(subject,text);

                    this.messageList.add(message);
                }
            }

        }catch (Exception e){
            System.err.format("Exception occurred trying to read '%s'.", fileName);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
            Parser p = new Parser();
            p.readConfig("config.xml");
    }
}
