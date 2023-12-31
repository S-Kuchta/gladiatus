package kuchtastefan.hint;

public class Hint {
    private final String text;
    private boolean showed;

    public Hint( String text) {
        this.text = text;
        this.showed = false;
    }

    public String getText() {
        return text;
    }

    public boolean isShowed() {
        return showed;
    }

    public void setShowed(boolean showed) {
        this.showed = showed;
    }
}
