import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Daifugo {
    public static void print(Player player, int valuePrev, int cardsNum, boolean revolution) {
        System.out.println();
        System.out.println("---------------");
        System.out.println("直前の数字: " + valuePrev + " (" + cardsNum + "枚)");
        System.out.println("革命: " + revolution);
        System.out.println("Player: " + player.getName());
        System.out.println();
    }
    public static int selectMenu(boolean flag) {
        List<String> list;
        if (flag) {
            list = Arrays.asList("パス", "手札を場に出す");
        } else {
            list = Arrays.asList("パス", "山札から1枚引く", "手札を場に出す");
        }

        while (true) {
            for (int i = 0; i < list.size(); i++) {
                System.out.println(i + ":" + list.get(i));
            }
            System.out.print("行動を選択> ");
            Scanner scanner = new Scanner(System.in);
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.err.println("0~" + (list.size() - 1) + "の範囲で入力してください");
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
        System.out.print("スペース区切りでカードを入力> ");
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
        System.out.println("[" + s + "] は含まれません。");
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
            System.out.print("Jokerの値> ");
            number = scanner.nextInt();
            if (1 <= number && number <= 13) break;
            System.out.println("1~13の範囲で入力");
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
        System.out.println("遊び方");
        System.out.println("基本的にプロンプトに従い、数値を入力する");
        System.out.println("場にカードを出す際は、数値の最上位から順にカード名をスペース区切りで書き下す");
        System.out.println("例) 1213 -> A 2 A 3 もしくは A 2 K もしくは Q K");
        System.out.println("Jokerは1~13の任意の値に化ける。先頭から順番に化けさせたい値を入力");
        System.out.println("例) スペース区切りでカードを入力> Joker 1 2 Joker");
        System.out.println("    Jokerの値を入力> 13");
        System.out.println("    Jokerの値を入力> 1");
        System.out.println("    とすると、3枚出しの13121として認められる");
        System.out.println();
        System.out.println("ルール");
        System.out.println("1. 前の人より大きい数字を出す");
        System.out.println("2. 基本的に素数しか出せない（→合成数出し）");
        System.out.println("3. カードの枚数は同じでなければならない（最初の人が2枚出しなら自分も2枚出す）");
        System.out.println("4. ルールを破ったら、場に出した枚数分、山札から手札に追加される");
        System.out.println("5. 手札が0枚になったら勝利");
        System.out.println();
        System.out.println("役");
        System.out.println("合成数出し");
        System.out.println("\t基本的に素数しか出せないが、例外として、その合成数の素因数を全て出した場合に認められる");
        System.out.println("\t 「出したい合成数 |(パイプ) その素因数」の形で入力する");
        System.out.println("\t例) 12 = 2 * 2 * 3より、「Q | 2 2 3」で出せば合成数12が認められる");
        System.out.println("57\tグロタンカット");
        System.out.println("\t数学者グロタンディークが57は素数と述べたことに起因する（グロタンディーク素数）");
        System.out.println("\t大富豪で言う「8切り」。場を流して再び自分の手番にする");
        System.out.println("1729\tラマヌジャン革命");
        System.out.println("\t数学者ラマヌジャンが発見したタクシー数(1729 = 10^3 + 9^3 = 12^3 + 1^3)");
        System.out.println("\t出すと革命が起きる（前の人より小さい数字を出す必要）");
        System.out.println("----------------------------------------");
    }
}
