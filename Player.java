import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    private List<Card> list = new ArrayList<Card>();
    private String name;
    private boolean computer = false;
    public Player(String name) {
        this.name = name;
        if (name.contains("computer")) {
            this.computer = true;
        }
    }
    public boolean isComputer() {
        return this.computer;
    }
    public String getName() {
        return this.name;
    }
    public void add(Card card) {
        this.list.add(card);
    }
    public Card get(int index) {
        return this.list.get(index);
    }
    public Card remove(int index) {
        return this.list.remove(index);
    }
    public void removeAll(List<Card> list) {
        this.list.removeAll(list);
    }
    public int size() {
        return this.list.size();
    }
    public void sort() {
        Collections.sort(this.list, new CardComparator());
    }
}
