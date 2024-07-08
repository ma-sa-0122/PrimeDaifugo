import java.util.List;

public interface Player {
    public String getName();
    public void add(Card card);
    public Card get(int index);
    public Card remove(int index);
    public void removeAll(List<Card> list);
    public int size();
    public void sort();
    public List<String> selectHand();
    public int selectMenu(boolean flag);
}
