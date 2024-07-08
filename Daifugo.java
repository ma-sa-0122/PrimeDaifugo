import java.util.ArrayList;
import java.util.List;

public class Daifugo {
    private int playersNum;                                 // �v���C�l��
    private int initCardnum;                                // ������D����
    private int passCount;                                  // �p�X�A����
    private int valuePrev;                                  // ���O�̐���
    private int cardsNumPrev;                               // ���O�̐����̃J�[�h����
    private boolean revolution;                             // �v��
    private CardStock cardStock = new CardStock();          // �R�D
    private List<Card> cardBoard = new ArrayList<Card>();   // ��
    public Daifugo(int playersNum, int initCardnum) {
        this.playersNum = playersNum;
        this.initCardnum = initCardnum;
    }

    // �ŏ��Ɏ��s�B�R�D����v���C���[�̎�D��initCardnum�������B������D�����B
    public void initialize(Player player) {
        for (int i = 0; i < this.initCardnum; i++) {
            player.add(cardStock.remove(0));
        }
        player.sort();
    }

    // �v���C���[�̃^�[���B����������true���Ԃ�B
    public boolean turn(Player player) {
        // �R�D�����Ȃ��������̐擪���玝���Ă���
        if (cardStock.size() < 5) {
            for (int i = 0; i < cardBoard.size() - 10; i++) {
                cardStock.add(cardBoard.remove(0));
            }
            cardStock.shufle();
        }

        // �O�̒l�Ɗv���̗L����\��
        DaifugoUtils.print(player, this.valuePrev, this.cardsNumPrev, this.revolution);

        // ��D���ꗗ�\��
        DaifugoUtils.printHand(player);

        // �s����I��(a 0:�p�X, 1:1������, 2:��ɏo��,  b 0:�p�X, 1:��ɏo��)
        int a = DaifugoUtils.selectMenu(false);
        int b = 2;
        if (a == 1) {       // �s���I��:�ꖇ����
            player.add(cardStock.remove(0));
            DaifugoUtils.printHand(player);
            b = DaifugoUtils.selectMenu(true);
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
            selects.addAll(player.selectHand());
            split = selects.size();
            if (selects.contains("|")) {        // �������Ȃ�A�l�Ƒf�����̕�����ڂ����Ԗڂ��L�^���AisComposite��true�ɂ���B
                split = selects.indexOf("|");
                selects.remove(split);
                isComposite = true;
            }
            for (String str : selects) {
                selectCards.add(DaifugoUtils.stringToCard(player, str));
            }
        } while (selectCards.contains(null));
        // �J�[�h����value���쐬�B�������o���p�� split(�o�������������Ƒf�����̋�؂�ꏊ), isComposite, composite(�f�����̐�) ��p��
        List<Card> cards = selectCards.subList(0,split);    // �o�����l���\������J�[�h
        int cardsNum = cards.size();                        // �o�����l���\������J�[�h�̖���
        int allSize = selectCards.size();                   // �o���J�[�h�̑���
        int value = DaifugoUtils.makeValue(cards);          // �o�����l
        int composite = 1;                                  // �������o���̑f�����̐�
        for (int i = split; i < allSize; i++) {
            Card card = selectCards.get(i);
            String number = DaifugoUtils.getNumber(card);
            composite = composite * Integer.parseInt(number);
        }

        // �f���E�𔻒�
        // �������� and ���O���傫���l
        if ((cardsNumPrev == 0 || CheckerUtils.isSameCardsNum(cardsNum, cardsNumPrev)) && CheckerUtils.isGreater(value, valuePrev, revolution)) {
            // �f�� -> ��ɏo(cardBoard��add)���Ď�D����remove
            if (CheckerUtils.isPrime(value)) {
                this.putDownCards(player, cards, value, cardsNum);
            }
            // �O���^���f�B�[�N�f�� -> �O���^���J�b�g (��𗬂��ĎR�D�ɉ����؂�)
            else if (CheckerUtils.isGrothendieck(value) && !isComposite) {
                System.out.println("�O���^���J�b�g�I");
                this.putDownCards(player, cards, 0, 0);
                this.flow();
                // �Ăю����̎�ԂɂȂ�̂Ŏ��ȎQ��
                this.turn(player);
            }
            // ���}�k�W������ -> ���}�k�W�����v�� 
            else if (CheckerUtils.isRamanujan(value) && !isComposite) {
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
                if (!CheckerUtils.isPrime(value) && !isComposite) System.out.println(value + "�͑f���ł͂Ȃ��I");
                if (isComposite && value != composite) System.out.println("�������o�����s�I ������" + value + "����" + composite);
                // (�o���������D�ɖ߂�)�o���������Ԃ�R�D�������
                for (int i = 0; i < allSize; i++) {
                    player.add(cardStock.remove(0));
                }
            }
        }
        // �f������Ȃ�or������������or�������Ⴄor�������o�����s -> (�o���������D�ɖ߂�)�o���������Ԃ�R�D�������
        else {
            if (!CheckerUtils.isPrime(value)) System.out.println(value + "�͑f���ł͂Ȃ��I");
            if (!CheckerUtils.isGreater(value, valuePrev, revolution)) {
                if (revolution) System.out.println(value + "��" + valuePrev + "��菬�����Ȃ��I");
                else System.out.println(value + "��" + valuePrev + "���傫���Ȃ��I");
            }
            if (!CheckerUtils.isSameCardsNum(cardsNum, cardsNumPrev) && cardsNumPrev != 0) System.out.println("�J�[�h�̖�����" + cardsNumPrev + "���ł͂Ȃ��I");
            if (isComposite && value != composite) System.out.println("�������o�����s�I ������" + value + "����" + composite);
            for (int i = 0; i < allSize; i++) {
                player.add(cardStock.remove(0));
            }
        }

        // ��D��0���ɂȂ�����I��
        if (player.size() == 0) {
            System.out.println(player.getName() + "�̎�D��0���ɂȂ����̂ŏI��");
            return true;
        }
        return false;
    }

    // ��ɏo�����J�[�h����D����폜����
    public void putDownCards(Player player, List<Card> cards, int value, int cardsNum) {
        for (Card card : cards) {
            this.cardBoard.add(card);
        }
        player.removeAll(cards);
        this.valuePrev = value;
        this.cardsNumPrev = cardsNum;
    }

    // �ꂪ���ꂽ�Ƃ��Ɏ��s����B����R�D�Ɉڂ��āAprev������������
    public void flow() {
        this.cardStock.addAll(this.cardBoard);
        this.cardStock.shufle();
        this.cardBoard.clear();
        this.valuePrev = 0;
        this.cardsNumPrev = 0;
    }
}