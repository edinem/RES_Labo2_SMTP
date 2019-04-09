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
        sendCommand("RCPT To:<"+toEmail+">");
        sendCommand("data");

        writeMessage(fromEmail,toEmail,subject,text);
        System.out.println(in.readLine());
        }

        public void sendCommand(String command) throws IOException {
            System.out.println("Writing: " + command);
            out.print(command);
            out.flush();
            out.print("\r\n");
            out.flush();
            String line = in.readLine();
            System.out.println(line);
            if(line.contains("220") || line.contains("250") || line.contains("334") || line.contains("354")){
                return;
            }
            while (!line.contains("220") || !line.contains("250")) {
                if(line.equals("250 HELP")){
                    return;
                }
                line = in.readLine();
                System.out.println(line);
            }
        }

        public void writeMessage(String from, String to, String subject, String text) {
            out.write("From: " + from);
            out.write("\r\n");
            out.flush();

            out.write("To: " + to);
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
    }


