package bbrains.su.firebaseandroidchat;

public class Message {

    private String text;
    private String text2;

    public Message() {
    }

    public Message(String text) {
        this.text = text;
        this.text2=text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText2() {
        return text2;
    }
}
