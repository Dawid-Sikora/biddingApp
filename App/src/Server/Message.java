package Server;

import java.io.Serializable;

public class Message implements Serializable {

    public String getNick() {
        return nick;
    }


    public String getValue() {
        return value;
    }

    private String nick;

    public Message(String nick, String value) {
        this.nick = nick;
        this.value = value;
    }

    private String value;


}
