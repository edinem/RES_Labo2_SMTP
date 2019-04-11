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

    /**
     * Create connection with the SMTP Server
     * @throws IOException
     */
    public void makeConnection() throws IOException {
        try{
            //Create the socket and diffrents buffers
            this.smtpSocket = new Socket(this.serverAdress,this.serverPort);
            in = new BufferedReader(new InputStreamReader(smtpSocket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(smtpSocket.getOutputStream()), true);

            //Sending the EHLO command to the server
            sendCommand("EHLO illuseyoursmtpasspamserver");

            //If the server supports authentification, if yes, send the username and the password
            if(supportAuth && username != null){
                sendCommand("AUTH LOGIN");
                sendCommand(Base64.getEncoder().encodeToString(username.getBytes()));
                sendCommand(Base64.getEncoder().encodeToString(password.getBytes()));
            }
        }catch(IOException e){
            System.out.println("Connection to SMTP server failed");
        }

    }

    /**
     * Send mail via the SMTP Server
     * @param fromEmail email from which we send the mail
     * @param toEmail email to which we send the mail
     * @param subject subject of the mail
     * @param text    body of the mail
     * @throws IOException
     */
    public void sendMail(String fromEmail, String toEmail, String subject, String text) throws IOException {
        try{
            //sending the fromEmail to the server
            sendCommand("MAIL From:<"+fromEmail+">");
            //sending the toMail to the server
            sendCommand("RCPT To:<"+toEmail+">");
            //sending the data command to the server
            sendCommand("data");

            //we write the message
            writeMessage(fromEmail,toEmail,subject,text);
            in.readLine();

        }catch(IOException e){
            System.out.println("Failed to send mail...");
        }
    }

    /**
     * Method used to send commands to the server
     * @param command command to send to the server
     * @throws IOException
     */
    public void sendCommand(String command) throws IOException {
        try{

            //we send the command to the server
            out.print(command);
            out.flush();
            out.print("\r\n");
            out.flush();
            String line = "";

            //Check if the server respond something valid and if it supports authentification
            while((!line.contains("250 ") && (!line.contains("334 ")) && (!line.contains("354 ")) && (!line.contains("235") && !line.contains("OK"))) || line == null){
                if(line.contains("AUTH")){
                    supportAuth = true;
                }
                line= in.readLine();
            }
        }catch(IOException e){
            System.out.println("Failed to send command");
        }
    }


    /**
     * Method to write the message to the server
     * @param from mail from which we want to send the mail
     * @param to mail to which we want to send the mail
     * @param subject subject of the mail
     * @param text text of the mail
     */
    public void writeMessage(String from, String to, String subject, String text) {

        //writing from
        out.write("From: <" + from + ">");
        out.write("\r\n");
        out.flush();

        //writinf to
        out.write("To: <" + to +">");
        out.write("\r\n");
        out.flush();

        //writing subject
        out.write("Subject: " + subject);
        out.write("\r\n");
        out.flush();
        out.write("\r\n");
        out.flush();

        //writing text
        out.write(text);
        out.write("\r\n");
        out.flush();
        out.write("\r\n.\r\n");
        out.flush();
    }

    /**
     * Method used to close connection with the servers
     * @throws IOException
     */
    public void closeConnection() throws IOException {
        try{
            this.smtpSocket.close();
            this.out.close();
            this.in.close();
        }catch(IOException e){
            System.out.println("Failed to close connection...");
        }
    }
}


