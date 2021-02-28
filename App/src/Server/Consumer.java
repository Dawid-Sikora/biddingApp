package Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


public class Consumer {


    private Socket soc;
    private DataInputStream inputStream;
    private ObjectInputStream objectInputStream;
    private DataOutputStream outputStream;
    private int value;
    private ArrayList<Consumer> clients;
    int znaleziono = 0;

    public Consumer(Socket soc, int value,ArrayList<Consumer> clients) throws IOException {
        this.soc = soc;
        this.inputStream = new DataInputStream(soc.getInputStream());
        this.objectInputStream = new ObjectInputStream(inputStream);
        this.outputStream = new DataOutputStream(soc.getOutputStream());
        this.value=value;
        this.clients = clients;
    }

    public DataOutputStream getOutputStream() {
        return outputStream;
    }


    public void ConsumerStart() {

        try {

            Thread thread = new Thread(new Runnable() {


                @Override
                public void run() {
                    try {

                        while (true) {

                            Message mess = (Message) objectInputStream.readObject();

                            if(mess.getValue().equals("nick")){

                                if(Server.nicks.size()>0) {
                                    for (int j = 0; j < Server.nicks.size(); j++) {
                                        if (Server.nicks.get(j).equals(mess.getNick())) {
                                            outputStream.writeUTF("Ten nick jest już zajęty. Podaj inny.");
                                            znaleziono = 1;
                                        }
                                    }
                                    if(znaleziono==1){
                                        znaleziono=0;
                                    }else {
                                        Server.nicks.add(mess.getNick());
                                        outputStream.writeUTF(String.valueOf(Server.seconds));
                                    }

                                }else {
                                    Server.nicks.add(mess.getNick());
                                    outputStream.writeUTF(String.valueOf(Server.seconds));
                                }

                            }else {

                                if (Server.wartosc < Integer.valueOf(mess.getValue())) {
                                    System.out.println("Użytkownik " + mess.getNick() + " przebił cene na: " + mess.getValue());
                                    Server.wartosc = Integer.valueOf(mess.getValue());

                                    if(Server.seconds<10)
                                    Server.seconds=10;

                                    for (int j = 0; j < clients.size(); j++) {

                                        clients.get(j).getOutputStream().writeUTF("Użytkownik " + mess.getNick() + " przebił cene na: " + mess.getValue());

                                    }
                                }
                            }
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                        System.exit(0);
                    }
                }
            });
            thread.start();

        }catch (Exception o){

        }
    }
}




