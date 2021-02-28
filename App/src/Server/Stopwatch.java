package Server;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Stopwatch {

    private static Timer timer;
    private int delay;
    private int period;

    public Stopwatch(){
        delay = 1000;
        period = 1000;
        timer = new Timer();
    }

    public void startTimer(){
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                Server.seconds = setInterval();

                Server.clients.stream().forEach(x->
                        {
                            try {
                                x.getOutputStream().writeUTF(String.valueOf(Server.seconds));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                );
            }
        }, delay, period);
    }

    private static final int setInterval() {
        if (Server.seconds == 1)
            timer.cancel();
        return --Server.seconds;
    }
}