package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


// Server class 
public class Server
{

    public static int wartosc = 5;
    public static int seconds = 15;
    private static int i = 0;
    public static ArrayList<String> nicks;
    public static ArrayList<Consumer> clients;

    public static void main(String[] args) throws IOException
    {

        clients = new ArrayList<Consumer>();
        nicks = new ArrayList<>();


        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Stopwatch timer = new Stopwatch();


                try (ServerSocket ss = new ServerSocket(5056)) {
                    while (true) {

                        //akceptowanie socketu i umieszczenie go w kolejce
                        Socket s = ss.accept();
                        if(i==0) timer.startTimer();
                        clients.add(new Consumer(s,wartosc,clients));
                        clients.get(i).ConsumerStart();
                        clients.get(i).getOutputStream().writeUTF("Aktualna cena: "+ wartosc);
                        i++;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(0);
                }

            }

        });

        thread.start();  //startujemy wątek serwera

    }
}