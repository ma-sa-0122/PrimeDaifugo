import java.util.ArrayList;
import java.util.List;

public class DaifugoMain {
    private int playersNum;                                 // �v���C�l��
    private int initCardnum;                                // ������D����
    private int passCount;                                  // �p�X�A����
    private int valuePrev;                                  // ���O�̐���
    private int cardsNumPrev;                               // ���O�̐����̃J�[�h����
    private boolean revolution;                             // �v��
    private CardStock cardStock = new CardStock();          // �R�D
    private List<Card> cardBoard = new ArrayList<Card>();   // ��
    public DaifugoMain(int playersNum, int initCardnum) {
        this.playersNum = playersNum;
        this.initCardnum = initCardnum;
    }
    public void initialize(Player player) {
        for (int i = 0; i < this.initCardnum; i++) {
            player.add(cardStock.remove(0));
        }
        player.sort();
    }
    public boolean turn(Player player) {
        // �R�D�����Ȃ��������̐擪���玝���Ă���
        if (cardStock.size() < 5) {
            for (int i = 0; i < cardBoard.size() - 10; i++) {
                cardStock.add(cardBoard.remove(0));
            }
            cardStock.shufle();
        }

        // �O�̒l�Ɗv���̗L����\��
        Daifugo.print(player, this.valuePrev, this.cardsNumPrev, this.revolution);

        // ��D���ꗗ�\��
        Daifugo.printHand(player);

        // �s����I��(a 0:�p�X, 1:1������, 2:��ɏo��,  b 0:�p�X, 1:��ɏo��)
        int a = Daifugo.selectMenu(false);
        int b = 2;
        if (a == 1) {       // �s���I��:�ꖇ����
            player.add(cardStock.remove(0));
            Daifugo.printHand(player);
            b = Daifugo.selectMenu(true);
        }
        if (a == 0 || b == 0) {     // �s���I��:�p�X
            this.passCount++;
            // �S���p�X�Ȃ��𗬂�
            if (this.passCount == playersNum - 1) {
                this.flow();
            }
            return false;
        }

        // �s���I��:��ɏo��
        passCount = 0;
        int split = 0;
        boolean isComposite = false;
        List<String> selects = new ArrayList<String>();
        List<Card> selectCards = new ArrayList<Card>();
        do {    // �o���J�[�h��I���B��D�ɂȂ��J�[�h��I�������������I�ђ���
            selectCards.clear();
            selects.addAll(Daifugo.selectHand(player));
            split = selects.size();
            if (selects.contains("|")) {
                split = selects.indexOf("|");
                selects.remove(split);
                isComposite = true;
            }
            for (String str : selects) {
                selectCards.add(Daifugo.stringToCard(player, str));
            }
        } while (selectCards.contains(null));
        // �J�[�h����value���쐬�B�������o���p�� split(�o�������������Ƒf�����̋�؂�ꏊ), isComposite, composite(�f�����̐�) ��p��
        List<Card> cards = selectCards.subList(0,split);
        int cardsNum = cards.size();
        int allSize = selectCards.size();
        int value = Daifugo.makeValue(cards);
        int composite = 1;
        for (int i = split; i < allSize; i++) {
            Card card = selectCards.get(i);
            String number = Daifugo.getNumber(card);
            composite = composite * Integer.parseInt(number);
        }

        // �f���E�𔻒�
        // �������� and ���O���傫���l
        if ((cardsNumPrev == 0 || Checker.isSameCardsNum(cardsNum, cardsNumPrev)) && Checker.isGreater(value, valuePrev, revolution)) {
            // �f�� -> ��ɏo(cardBoard��add)���Ď�D����remove
            if (Checker.isPrime(value)) {
                this.putDownCards(player, cards, value, cardsNum);
            }
            // �O���^���f�B�[�N�f�� -> �O���^���J�b�g (��𗬂��ĎR�D�ɉ����؂�)
            else if (Checker.isGrothendieck(value) && !isComposite) {
                System.out.println("�O���^���J�b�g�I");
                this.putDownCards(player, cards, 0, 0);
                this.flow();
                // �Ăю����̎�ԂɂȂ�̂Ŏ��ȎQ��
                this.turn(player);
            }
            // ���}�k�W������ -> ���}�k�W�����v�� 
            else if (Checker.isRamanujan(value) && !isComposite) {
                System.out.println("���}�k�W�����v���I");
                this.putDownCards(player, cards, value, cardsNum);
            }
            // �������o��
            else if (value == composite && value != 1) {
                System.out.print("�������o���I " + value + "=");
                for (int i = split; i < allSize - 1; i++) {
                    Card card = selectCards.get(i);
                    System.out.print(card.getValue() + "*");
                }
                System.out.println(selectCards.get(allSize - 1).getValue());
                this.putDownCards(player, selectCards, value, cardsNum);
            }
            else {
                if (!Checker.isPrime(value) && !isComposite) System.out.println(value + "�͑f���ł͂Ȃ��I");
                if (isComposite && value != composite) System.out.println("�������o�����s�I ������" + value + "����" + composite);
                // (�o���������D�ɖ߂�)�o���������Ԃ�R�D�������
                for (int i = 0; i < allSize; i++) {
                    player.add(cardStock.remove(0));
                }
            }
        }
        // �f������Ȃ�or������������or�������Ⴄor�������o�����s -> (�o���������D�ɖ߂�)�o���������Ԃ�R�D�������
        else {
            if (!Checker.isPrime(value)) System.out.println(value + "�͑f���ł͂Ȃ��I");
            if (!Checker.isGreater(value, valuePrev, revolution)) {
                if (revolution) System.out.println(value + "��" + valuePrev + "��菬�����Ȃ��I");
                else System.out.println(value + "��" + valuePrev + "���傫���Ȃ��I");
            }
            if (!Checker.isSameCardsNum(cardsNum, cardsNumPrev) && cardsNumPrev != 0) System.out.println("�J�[�h�̖�����" + cardsNumPrev + "���ł͂Ȃ��I");
            if (isComposite && value != composite) System.out.println("�������o�����s�I ������" + value + "����" + composite);
            for (int i = 0; i < allSize; i++) {
                player.add(cardStock.remove(0));
            }
        }

        resetJoker();

        // ��D��0���ɂȂ�����I��
        if (player.size() == 0) {
            System.out.println(player.getName() + "�̎�D��0���ɂȂ����̂ŏI��");
            return true;
        }
        return false;
    }
    public void putDownCards(Player player, List<Card> cards, int value, int cardsNum) {
        for (Card card : cards) {
            this.cardBoard.add(card);
        }
        player.removeAll(cards);
        this.valuePrev = value;
        this.cardsNumPrev = cardsNum;
    }
    public void flow() {
        this.cardStock.addAll(this.cardBoard);
        this.cardStock.shufle();
        this.cardBoard.clear();
        this.valuePrev = 0;
        this.cardsNumPrev = 0;
    }
    public void resetJoker() {
        for (Card card : cardBoard) {
            card.setJokerValue(99);
        }
        for (int i = 0; i < cardStock.size(); i++) {
            cardStock.get(i).setJokerValue(99);
        }
    }
}