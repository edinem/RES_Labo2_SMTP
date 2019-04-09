import java.io.*;
import java.net.Socket;
import java.util.Base64;

public class SMTPServer {
    private String username;
    private String password;
    private String serverAdress;
    private int serverPort;
    private Socket smtpSocket;
    private BufferedReader in;
    private PrintWriter out;
    private boolean supportAuth;


    SMTPServer(String username, String password, String serverAdress, int serverPort){
        this.username = username;
        this.password = password;
        this.serverAdress = serverAdress;
        this.serverPort = serverPort;
        this.supportAuth = false;
    }

    public void makeConnection() throws IOException {
        //creation du socket et des differents flux
        this.smtpSocket = new Socket(this.serverAdress,this.serverPort);
        in = new BufferedReader(new InputStreamReader(smtpSocket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(smtpSocket.getOutputStream()), true);

        sendCommand("EHLO illuseyoursmtpasspamserver");

        if(supportAuth && username != null){
            sendCommand("AUTH LOGIN");
            sendCommand(Base64.getEncoder().encodeToString(username.getBytes()));
            sendCommand(Base64.getEncoder().encodeToString(password.getBytes()));
        }

    }

    public void sendMail(String fromFirstName, String fromLastName, String fromEmail, String toEmail, String subject, String text) throws IOException {

    sendCommand("MAIL From:<"+fromEmail+">");
    sendCommand("RCPT To:<"+toEmail+">");
    sendCommand("data");

    writeMessage(fromFirstName,fromLastName,fromEmail,toEmail,subject,text);
    System.out.println(in.readLine());
    }

    public void sendCommand(String command) throws IOException {
        out.print(command);
        out.flush();
        out.print("\r\n");
        out.flush();
        String line = "";

        while((!line.contains("250 ") && (!line.contains("334 ")) && (!line.contains("354 ")) && (!line.contains("235") && !line.contains("OK"))) || line == null){
            if(line.contains("AUTH")){
                supportAuth = true;
            }
            line= in.readLine();
            System.out.println(line);
        }
    }



    public void writeMessage(String firstName, String lastName, String from, String to, String subject, String text) {
        out.write("From: "+firstName + " " + lastName + "<" + from + ">");
        out.write("\r\n");
        out.flush();

        out.write("To: <" + to +">");
        out.write("\r\n");
        out.flush();

        out.write("Subject: " + subject);
        out.write("\r\n");
        out.flush();
        out.write("\r\n");
        out.flush();

        out.write(text);
        out.write("\r\n");
        out.flush();
        out.write("\r\n.\r\n");
        out.flush();
    }

    public void closeConnection() throws IOException {
        this.smtpSocket.close();
    }
}


