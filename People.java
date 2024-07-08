import java.util.List;

public class People extends AbstractPlayer {
    public People(String name) {
        super(name);
    }
    @Override
    public List<String> selectHand() {
        return DaifugoUtils.selectHand();
    }
    @Override
    public int selectMenu(boolean flag) {
        return DaifugoUtils.selectMenu(flag);
    }
}
