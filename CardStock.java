import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardStock {
    private List<Card> list = new ArrayList<Card>();
    public CardStock() {
        for (int i = 0; i < 52; i++) {
            int value = (i % 13) + 1;

            String name = "";
            if (value == 1) name = "A";
            else if (value == 11) name = "J";
            else if (value == 12) name = "Q";
            else if (value == 13) name = "K";
            else name = String.valueOf(value);

            String type = "";
            if (i / 13 == 0) type = "Spade";
            else if (i / 13 == 1) type = "Club";
            else if (i / 13 == 2) type = "Diamond";
            else type = "Heart";

            this.list.add(new Card(value, name, type));
        }
        this.list.add(new Card(98, "Joker", "Joker"));
        this.list.add(new Card(99, "Joker", "Joker"));

        this.shufle();
    }
    public void shufle() {
        Collections.shuffle(this.list);
    }
    public void add(Card card) {
        this.list.add(card);
    }
    public void addAll(List<Card> list) {
        this.list.addAll(list);
    }
    public Card get(int index) {
        return this.list.get(index);
    }
    public Card remove(int index) {
        return this.list.remove(index);
    }
    public int size() {
        return this.list.size();
    }
}
