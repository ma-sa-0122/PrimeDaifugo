import java.util.List;

public class Computer extends AbstractPlayer {
    public Computer(String name) {
        super(name);
    }
    @Override
    public List<String> selectHand() {
        return null;
    }
    @Override
    public int selectMenu(boolean flag) {
        return 0;
    }
}
