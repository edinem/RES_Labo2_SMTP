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

public class SpamAttack {

    private SMTPServer server;
    private ArrayList<Group> groupList;
    private ArrayList<Message> messageList;

    SpamAttack(){
        this.groupList = new ArrayList();
        this.messageList = new ArrayList();
    }

    /**
     * Method used to read config file to prepare the attack.
     * @param fileName Config file
     */
    public void readConfig(String fileName){
        try{
            File inputFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            Element server = (Element) doc.getElementsByTagName("server").item(0);
            NodeList victims = doc.getElementsByTagName("victim");
            NodeList messages = doc.getElementsByTagName("message");
            String tmp2 = doc.getElementsByTagName("nbGroups").item(0).getTextContent();
            int nbGroups = Integer.valueOf(doc.getElementsByTagName("nbGroups").item(0).getTextContent());
            int victimsPerGroup = victims.getLength() / nbGroups;
            if( victimsPerGroup < 3){
                throw new RuntimeException("Error... you don't have enough victims. Change your config and retry !");
            }

            System.out.println("Creating server...");
            String username = server.getElementsByTagName("username").item(0).getTextContent();
            String password = server.getElementsByTagName("password").item(0).getTextContent();
            String ip = server.getElementsByTagName("ip").item(0).getTextContent();
            int port = Integer.valueOf(server.getElementsByTagName("port").item(0).getTextContent());
            this.server = new SMTPServer(username,password,ip,port);
            System.out.println("Server created...");

            //Store victims figuring in the config file
            System.out.println("Creating groups...");
            ArrayList<String> victimList = new ArrayList<>();
            for(int i = 0; i < victims.getLength(); ++i){
                Node nVictim = victims.item(i);
                if(nVictim.getNodeType() == Node.ELEMENT_NODE){
                    Element eVictim = (Element)nVictim;
                    String mailVictim = eVictim.getTextContent();
                    victimList.add(mailVictim);
                }
            }

            //Creates groups
            while(victimList.size() > 0){
                for(int i = 0; i < nbGroups;++i){
                    int rand = (int) ((Math.random() * victimList.size()));
                    String sender = victimList.get(rand);
                    victimList.remove(sender);
                    Group a = new Group(sender);
                    for(int j = 0; j < victimsPerGroup-1;++j){
                        rand = (int) ((Math.random() * victimList.size()));
                        String tmp = victimList.get(rand);
                        victimList.remove(rand);
                        a.addVictim(tmp);
                    }

                    if((i == nbGroups - 1) && victimList.size() !=0){
                        for(int k = 0; k < victimList.size(); ++k){
                            a.addVictim(victimList.get(k));
                            victimList.remove(k);
                        }
                    }
                    this.groupList.add(a);
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
            System.out.println("Config file loaded...\nAttack ready");

        }catch (Exception e){
            System.err.format("Exception occurred trying to read '%s'.", fileName);
            e.printStackTrace();
        }
    }

    /**
     *  Launch the attack
     * @throws IOException
     */
    public void attack() throws IOException {
        System.out.println("Starting attack...");
        try{
            server.makeConnection();
            Random rand = new Random();

            //on parcourt tous les groupes
            for (Group g: groupList) {
                String a = g.getSender();
                //on parcourt toutes les victimes
                //on selectionne un message
                Message m = messageList.get(rand.nextInt(messageList.size()));
                for(String v : g.getVictims()){
                    //on envoit le message a chaque victime
                    server.sendMail(a,v,m.getSubject(),m.getMessage());
                    System.out.println("Message \"" + m.getMessage() + "\" to " + v + " from " + a + " sent");
                }
            }
            server.closeConnection();
            System.out.println("Attack finished...");
        }catch(IOException e){
            System.out.println("Attack failed");
        }
    }

    public static void main(String[] args) throws IOException {
        SpamAttack p = new SpamAttack();
        p.readConfig("../configMockMock.xml");
        p.attack();
    }
}
