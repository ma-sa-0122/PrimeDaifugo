import java.util.ArrayList;
import java.util.List;

public class Daifugo {
    private int playersNum;                                 // プレイ人数
    private int initCardnum;                                // 初期手札枚数
    private int passCount;                                  // パス連続回数
    private int valuePrev;                                  // 直前の数字
    private int cardsNumPrev;                               // 直前の数字のカード枚数
    private boolean revolution;                             // 革命
    private CardStock cardStock = new CardStock();          // 山札
    private List<Card> cardBoard = new ArrayList<Card>();   // 場
    public Daifugo(int playersNum, int initCardnum) {
        this.playersNum = playersNum;
        this.initCardnum = initCardnum;
    }

    // 最初に実行。山札からプレイヤーの手札にinitCardnum枚引く。初期手札を作る。
    public void initialize(Player player) {
        for (int i = 0; i < this.initCardnum; i++) {
            player.add(cardStock.remove(0));
        }
        player.sort();
    }

    // プレイヤーのターン。決着がつくとtrueが返る。
    public boolean turn(Player player) {
        // 山札が少なかったら場の先頭から持ってくる
        if (cardStock.size() < 5) {
            for (int i = 0; i < cardBoard.size() - 10; i++) {
                cardStock.add(cardBoard.remove(0));
            }
            cardStock.shufle();
        }

        // 前の値と革命の有無を表示
        DaifugoUtils.print(player, this.valuePrev, this.cardsNumPrev, this.revolution);

        // 手札を一覧表示
        DaifugoUtils.printHand(player);

        // 行動を選択(a 0:パス, 1:1枚引く, 2:場に出す,  b 0:パス, 1:場に出す)
        int a = DaifugoUtils.selectMenu(false);
        int b = 2;
        if (a == 1) {       // 行動選択:一枚引く
            player.add(cardStock.remove(0));
            DaifugoUtils.printHand(player);
            b = DaifugoUtils.selectMenu(true);
        }
        if (a == 0 || b == 0) {     // 行動選択:パス
            this.passCount++;
            // 全員パスなら場を流す
            if (this.passCount == playersNum - 1) {
                this.flow();
            }
            return false;
        }

        // 行動選択:場に出す
        passCount = 0;
        int split = 0;
        boolean isComposite = false;
        List<String> selects = new ArrayList<String>();
        List<Card> selectCards = new ArrayList<Card>();
        do {    // 出すカードを選択。手札にないカードを選択し続ける限り選び直し
            selectCards.clear();
            selects.addAll(player.selectHand());
            split = selects.size();
            if (selects.contains("|")) {        // 合成数なら、値と素因数の分かれ目が何番目か記録し、isCompositeをtrueにする。
                split = selects.indexOf("|");
                selects.remove(split);
                isComposite = true;
            }
            for (String str : selects) {
                selectCards.add(DaifugoUtils.stringToCard(player, str));
            }
        } while (selectCards.contains(null));
        // カードからvalueを作成。合成数出し用に split(出したい合成数と素因数の区切り場所), isComposite, composite(素因数の積) を用意
        List<Card> cards = selectCards.subList(0,split);    // 出す数値を構成するカード
        int cardsNum = cards.size();                        // 出す数値を構成するカードの枚数
        int allSize = selectCards.size();                   // 出すカードの総数
        int value = DaifugoUtils.makeValue(cards);          // 出す数値
        int composite = 1;                                  // 合成数出しの素因数の積
        for (int i = split; i < allSize; i++) {
            Card card = selectCards.get(i);
            String number = DaifugoUtils.getNumber(card);
            composite = composite * Integer.parseInt(number);
        }

        // 素数・役判定
        // 同じ枚数 and 直前より大きい値
        if ((cardsNumPrev == 0 || CheckerUtils.isSameCardsNum(cardsNum, cardsNumPrev)) && CheckerUtils.isGreater(value, valuePrev, revolution)) {
            // 素数 -> 場に出(cardBoardにadd)して手札からremove
            if (CheckerUtils.isPrime(value)) {
                this.putDownCards(player, cards, value, cardsNum);
            }
            // グロタンディーク素数 -> グロタンカット (場を流して山札に加え切る)
            else if (CheckerUtils.isGrothendieck(value) && !isComposite) {
                System.out.println("グロタンカット！");
                this.putDownCards(player, cards, 0, 0);
                this.flow();
                // 再び自分の手番になるので自己参照
                this.turn(player);
            }
            // ラマヌジャン数 -> ラマヌジャン革命 
            else if (CheckerUtils.isRamanujan(value) && !isComposite) {
                System.out.println("ラマヌジャン革命！");
                this.putDownCards(player, cards, value, cardsNum);
            }
            // 合成数出し
            else if (value == composite && value != 1) {
                System.out.print("合成数出し！ " + value + "=");
                for (int i = split; i < allSize - 1; i++) {
                    Card card = selectCards.get(i);
                    System.out.print(card.getValue() + "*");
                }
                System.out.println(selectCards.get(allSize - 1).getValue());
                this.putDownCards(player, selectCards, value, cardsNum);
            }
            else {
                if (!CheckerUtils.isPrime(value) && !isComposite) System.out.println(value + "は素数ではない！");
                if (isComposite && value != composite) System.out.println("合成数出し失敗！ 合成数" + value + "≠積" + composite);
                // (出した手を手札に戻し)出した枚数ぶん山札から引く
                for (int i = 0; i < allSize; i++) {
                    player.add(cardStock.remove(0));
                }
            }
        }
        // 素数じゃないor数字が小さいor枚数が違うor合成数出し失敗 -> (出した手を手札に戻し)出した枚数ぶん山札から引く
        else {
            if (!CheckerUtils.isPrime(value)) System.out.println(value + "は素数ではない！");
            if (!CheckerUtils.isGreater(value, valuePrev, revolution)) {
                if (revolution) System.out.println(value + "は" + valuePrev + "より小さくない！");
                else System.out.println(value + "は" + valuePrev + "より大きくない！");
            }
            if (!CheckerUtils.isSameCardsNum(cardsNum, cardsNumPrev) && cardsNumPrev != 0) System.out.println("カードの枚数が" + cardsNumPrev + "枚ではない！");
            if (isComposite && value != composite) System.out.println("合成数出し失敗！ 合成数" + value + "≠積" + composite);
            for (int i = 0; i < allSize; i++) {
                player.add(cardStock.remove(0));
            }
        }

        // 手札が0枚になったら終了
        if (player.size() == 0) {
            System.out.println(player.getName() + "の手札が0枚になったので終了");
            return true;
        }
        return false;
    }

    // 場に出したカードを手札から削除する
    public void putDownCards(Player player, List<Card> cards, int value, int cardsNum) {
        for (Card card : cards) {
            this.cardBoard.add(card);
        }
        player.removeAll(cards);
        this.valuePrev = value;
        this.cardsNumPrev = cardsNum;
    }

    // 場が流れたときに実行する。場を山札に移して、prevを初期化する
    public void flow() {
        this.cardStock.addAll(this.cardBoard);
        this.cardStock.shufle();
        this.cardBoard.clear();
        this.valuePrev = 0;
        this.cardsNumPrev = 0;
    }
}