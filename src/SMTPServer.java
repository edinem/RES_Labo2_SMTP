import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Base64;
import java.util.Date;

import javax.mail.*;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



public class SMTPServer {
    private String username;
    private String password;
    private String serverAdress;
    private int serverPort;
    private Socket smtpSocket;
    private InputStream inn;
    private OutputStream outt;
    private BufferedReader in;
    private PrintWriter out;


    SMTPServer(String username, String password, String serverAdress, int serverPort){
        this.username = username;
        this.password = password;
        this.serverAdress = serverAdress;
        this.serverPort = serverPort;
    }

    public void makeConnection() throws IOException {
        this.smtpSocket = new Socket(this.serverAdress,this.serverPort);
        inn = smtpSocket.getInputStream();
        outt = smtpSocket.getOutputStream();
        in = new BufferedReader(new InputStreamReader(inn));
        out = new PrintWriter(new OutputStreamWriter(outt), true);

        if(username != null){
            sendCommand("AUTH LOGIN");
            sendCommand(Base64.getEncoder().encodeToString(username.getBytes()));
            sendCommand(Base64.getEncoder().encodeToString(password.getBytes()));
        }

        sendCommand("EHLO spamgenerator");
    }

    public void sendMail(String fromFirstName, String fromLastName, String fromEmail, String toEmail, String subject, String text) throws IOException {

        sendCommand("MAIL From:<"+fromEmail+">");
        System.out.println(in.readLine());
        sendCommand("RCPT To:<"+toEmail+">");
        System.out.println(in.readLine());
        sendCommand("data");
        System.out.println(in.readLine());

        sendCommand("From: " + fromEmail);
        sendCommand("To: " + toEmail);
        sendCommand("Subject: " + subject);
        sendCommand(text);
        out.write("\r\n.");
        }

        public void sendCommand(String command) throws IOException {
            System.out.println("Writing: " + command);
            out.print(command);
            out.flush();
            out.print("\r\n");
            out.flush();
            String line = "";

            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        }
    }


