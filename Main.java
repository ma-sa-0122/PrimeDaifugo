import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<Player> playerList = new ArrayList<Player>();
        int playersNum = 0;
        int initCardnum = 0;

        String a = Menu(false);
        String b = "0";
        if (a.equals("0")) {
            Daifugo.printHelp();
            b = Menu(true);
        }
        if (a.equals("1") || b.equals("0")) {
            playerList.add(new Player("player1"));
            playerList.add(new Player("player2"));
            playersNum = 2;
            initCardnum = 7;
        } else if (a.equals("2") || b.equals("1")) {
            System.out.print("�v���C���[�l�������> ");
            playersNum = scanner.nextInt();

            for (int i = 0; i < playersNum; i++) {
                playerList.add(new Player("player" + (i + 1)));
            }

            System.out.print("�ŏ��ɔz���D�̖��������> ");
            initCardnum = scanner.nextInt();
        } else {
            return ;
        }

        // �f����x���I�u�W�F�N�g
        DaifugoMain daifugoMain = new DaifugoMain(playersNum, initCardnum);

        // �e�v���C���[�Ɏ�D��z��
        for (Player player : playerList) {
            daifugoMain.initialize(player);
        }
        // ���s�����܂Ń^�[����i�߂�
        int playerSelect = 0;
        boolean winlose = false;
        while (!winlose) {
            playerSelect = playerSelect % playersNum;
            Player player = playerList.get(playerSelect);
            winlose = daifugoMain.turn(player);
            playerSelect++;
        }
    }
    public static String Menu(boolean flag) {
        List<String> list;
        if (flag) list = Arrays.asList("player1 vs player2, ������D7��", "�ݒ�", "�I��");
        else list = Arrays.asList("������", "player1 vs player2, ������D7��", "�ݒ�", "�I��");
        int size = list.size();
        
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < size - 1; i++) {
            System.out.println(i + ": " + list.get(i));
        }
        System.out.println("else: " + list.get(size - 1));
        System.out.print("�I��> ");
        String setting = scanner.nextLine();

        return setting;
    }
}