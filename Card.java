public class Card {
    private int value;
    private int jokerValue = 99;
    private String name;
    private String type;
    public Card(int value, String name, String type){
        this.value = value;
        this.name = name;
        this.type = type;
    }
    public void setJokerValue(int jokerValue) {
        this.jokerValue = jokerValue;
    }
    public int getValue() {
        if (this.name.equals("Joker")) return this.jokerValue;
        else return this.value;
    }
    public String getName() {
        return this.name;
    }
}
