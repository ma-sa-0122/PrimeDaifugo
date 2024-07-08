import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractPlayer implements Player{
    private List<Card> list = new ArrayList<Card>();
    private String name;
    public AbstractPlayer(String name) {
        this.name = name;
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

    public abstract List<String> selectHand();
    public abstract int selectMenu(boolean flag);
}
