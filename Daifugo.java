import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Daifugo {
    public static void print(Player player, int valuePrev, int cardsNum, boolean revolution) {
        System.out.println();
        System.out.println("---------------");
        System.out.println("���O�̐���: " + valuePrev + " (" + cardsNum + "��)");
        System.out.println("�v��: " + revolution);
        System.out.println("Player: " + player.getName());
        System.out.println();
    }
    public static int selectMenu(boolean flag) {
        List<String> list;
        if (flag) {
            list = Arrays.asList("�p�X", "��D����ɏo��");
        } else {
            list = Arrays.asList("�p�X", "�R�D����1������", "��D����ɏo��");
        }

        while (true) {
            for (int i = 0; i < list.size(); i++) {
                System.out.println(i + ":" + list.get(i));
            }
            System.out.print("�s����I��> ");
            Scanner scanner = new Scanner(System.in);
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.err.println("0~" + (list.size() - 1) + "�͈̔͂œ��͂��Ă�������");
            }
        }
    }
    public static void printHand(Player player) {
        player.sort();
        int size = player.size();
        for (int i = 0; i < size; i++) {
            Card card = player.get(i);
            System.out.print("[" + card.getName() + "] ");
        }
        System.out.println();
    }
    public static List<String> selectHand(Player player) {
        System.out.print("�X�y�[�X��؂�ŃJ�[�h�����> ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        List<String> cards = Arrays.asList(input.split(" "));
        return cards;
    }
    public static Card stringToCard(Player player, String s) {
        for (int j = 0; j < player.size(); j++) {
            Card card = player.get(j);
            String name = card.getName();
            if (name.equals(s)) {
                return card;
            }
        }
        System.out.println("[" + s + "] �͊܂܂�܂���B");
        return null;
    }
    public static int makeValue(List<Card> cards) {
        String value = "";
        for (Card card : cards) {
            value += getNumber(card);
        }
        return Integer.parseInt(value);
    }
    public static void JokerToNumber(Card card) {
        assert card.getName().equals("Joker");
        Scanner scanner = new Scanner(System.in);
        int number = 0;
        while (true) {
            System.out.print("Joker�̒l> ");
            number = scanner.nextInt();
            if (1 <= number && number <= 13) break;
            System.out.println("1~13�͈̔͂œ���");
            number = 0;
        }
        card.setJokerValue(number);
    }
    public static String getNumber(Card card) {
        String number = "";
        if (card.getName().equals("Joker")) {
            JokerToNumber(card);
        }
        number += card.getValue();
        return number;
    }
    public static void printHelp() {
        System.out.println("----------------------------------------");
        System.out.println("�V�ѕ�");
        System.out.println("��{�I�Ƀv�����v�g�ɏ]���A���l����͂���");
        System.out.println("��ɃJ�[�h���o���ۂ́A���l�̍ŏ�ʂ��珇�ɃJ�[�h�����X�y�[�X��؂�ŏ�������");
        System.out.println("��) 1213 -> A 2 A 3 �������� A 2 K �������� Q K");
        System.out.println("Joker��1~13�̔C�ӂ̒l�ɉ�����B�擪���珇�Ԃɉ������������l�����");
        System.out.println("��) �X�y�[�X��؂�ŃJ�[�h�����> Joker 1 2 Joker");
        System.out.println("    Joker�̒l�����> 13");
        System.out.println("    Joker�̒l�����> 1");
        System.out.println("    �Ƃ���ƁA3���o����13121�Ƃ��ĔF�߂���");
        System.out.println();
        System.out.println("���[��");
        System.out.println("1. �O�̐l���傫���������o��");
        System.out.println("2. ��{�I�ɑf�������o���Ȃ��i���������o���j");
        System.out.println("3. �J�[�h�̖����͓����łȂ���΂Ȃ�Ȃ��i�ŏ��̐l��2���o���Ȃ玩����2���o���j");
        System.out.println("4. ���[����j������A��ɏo�����������A�R�D�����D�ɒǉ������");
        System.out.println("5. ��D��0���ɂȂ����珟��");
        System.out.println();
        System.out.println("��");
        System.out.println("�������o��");
        System.out.println("\t��{�I�ɑf�������o���Ȃ����A��O�Ƃ��āA���̍������̑f������S�ďo�����ꍇ�ɔF�߂���");
        System.out.println("\t �u�o������������ |(�p�C�v) ���̑f�����v�̌`�œ��͂���");
        System.out.println("\t��) 12 = 2 * 2 * 3���A�uQ | 2 2 3�v�ŏo���΍�����12���F�߂���");
        System.out.println("57\t�O���^���J�b�g");
        System.out.println("\t���w�҃O���^���f�B�[�N��57�͑f���Əq�ׂ����ƂɋN������i�O���^���f�B�[�N�f���j");
        System.out.println("\t��x���Ō����u8�؂�v�B��𗬂��čĂю����̎�Ԃɂ���");
        System.out.println("1729\t���}�k�W�����v��");
        System.out.println("\t���w�҃��}�k�W���������������^�N�V�[��(1729 = 10^3 + 9^3 = 12^3 + 1^3)");
        System.out.println("\t�o���Ɗv�����N����i�O�̐l��菬�����������o���K�v�j");
        System.out.println("----------------------------------------");
    }
}
