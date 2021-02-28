package sample;

import Server.Message;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;


public class Controller {

    DataInputStream dis;
    DataOutputStream dos;
    ObjectOutputStream oos;
    Socket s;

    private boolean isConnect = false;

    @FXML
    private TextArea czas;

    @FXML
    private Button button_connect;

    @FXML
    private Button button_msg;

    @FXML
    private TextField sendText;

    @FXML
    private TextField nick;

    @FXML
    private TextArea textArea;


    public void send(javafx.event.ActionEvent actionEvent) throws IOException {

        Message mess = new Message(nick.getText(), sendText.getText());
        oos.writeObject(mess);

    }

    public void connect(javafx.event.ActionEvent actionEvent) throws IOException {

        if (!isConnect) {
            try {
                // getting localhost ip
                InetAddress ip = InetAddress.getByName("localhost");

                // establish the connection with server port 5056
                s = new Socket(ip, 5056);

                // obtaining input and out streams
                dis = new DataInputStream(s.getInputStream());
                dos = new DataOutputStream(s.getOutputStream());
                oos = new ObjectOutputStream(dos);


                // the following loop performs the exchange of
                // information between client and client handler

                Thread thread = new Thread() {
                    public void run() {
                        while (true) {
                            String received = null;
                            try {
                                received = dis.readUTF();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            try {
                                if(Integer.valueOf(received)==0)
                                    button_msg.setDisable(true);
                                //System.out.println(received);
                                czas.setEditable(true);
                                czas.setText(received);
                                czas.setEditable(false);

                            }catch (Exception x){

                                textArea.setText(received);
                                if (received.equals("Ten nick jest już zajęty. Podaj inny.")) {

                                    System.out.println("Zmien nick");
                                    nick.setEditable(true);
                                    button_msg.setDisable(true);
                                    button_connect.setDisable(false);

                                }
                            }
                        }
                    }
                };
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            button_connect.setDisable(true);
            nick.setEditable(false);
            button_msg.setDisable(false);
            Message mess = new Message(nick.getText(), "nick");
            oos.writeObject(mess);

            // closing resources

        }
    }
}


