# PrimeDaifugo

## 各ファイルについて
- Card: トランプカードのクラス。Listで集約すると手札とか山札になる。
- CardStock: 山札。切ったり（shufle）、引いたり（remove）、加えたり（add）出来る。
- CardComparator: カードの大小を比較するコンパレータ。手札の整理（→Playerクラスのsortメソッド）に使う。
- CheckerUtils: 場に出たカードが有効か判定する系の関数の集まり。
- DaifugoUtils: 手札とか盤面を表示したり、カードを数字に変換したり、根幹の関数の集合。
- Daifugo: 実際に大富豪を行うオブZジェクト。ごちゃごちゃしてるので上手く関数やらオブジェクトで隠蔽できねぇかなと試行錯誤したりしなかったり。
- Main: メインとか言ってるが、実際はUI担当みたいな仲介人。大富豪をプレイするまでの初期設定とかヘルプとかを担当する。
- Player: プレイヤーに関するインタフェース。
- AbstractPlayer: プレイヤーの抽象クラス。手札（List<Card> list変数）を管理したり。行動選択をするselectMenu()と、場に出すカードを決めるselectHand()の実装が、PeopleとComputerで異なるので実クラスに任せている。
- People: プレイヤー(Player)が既に使われてしまったので、人間という名称。
- Computer: CPUのプレイヤー。機械学習かゲーム理論やら学んで対戦CPUを作れたらなという展望。

## クラス図
```mermaid
classDiagram
    Player <|.. AbstractPlayer
    AbstractPlayer <|-- People
    AbstractPlayer <|-- Computer

    CardComparator ..> Card
    Card <--o AbstractPlayer
    Card <--o CardStock

    Daifugo ..> DaifugoUtils
    Daifugo ..> CheckerUtils
    Daifugo --> CardStock


    class Card {
        - value : int
        - jokerValue = 99 : int
        - name : String
        - type : String
        + Card(value : int, name : String, type : String)
        + setJokerValue(jokerValue : int) void
        + getValue() int
        + getName() String
    }

    class CardComparator {
        + compare(c1 : Card, c2 : Card) int
    }

    class CardStock {
        - list : List < Card >
        + CardStock()
        + shufle() void
        + add(card * Card) void
        + addAll(list : List < Card >) void
        + get(index : int) Card
        + remove(index : int) Card
        + size() int
    }


    class Daifugo {
        - playersNum : int
        - initCardnum : int
        - passCount : int
        - valuePrev : int
        - cardsNumPrev : int
        - revolution : boolean
        - cardStock : CardStock
        - cardBoard : List < Card >
        + Daifugo(playersNum : int, initCardnum : int) void
        + initialize(player : Player) void
        + turn(player : Player) boolean
        + putDownCards(player : Player, cards : List < Card >, value : int, cardsNum : int) void
        + flow() void
    }

    class DaifugoUtils {
        + print(player : Player, valuePrev : int, cardsNum : int, revolution : boolean) void$
        + selectMenu(flag : boolean) int$
        + printHand(player : Player) void$
        + stringToCard(player : Player, s : String) Card$
        + makeValue(cards : List < Card >) int$
        + JokerToNumber(card : Card) void$
        + getNumber(card : Card) String$
        + printHelp() void$
    }

    class CheckerUtils {
        + isSameCardsNum(cardsNum : int, cardsNumPrev : int) boolean$
        + isGreater(num1 : int, num2 : int, flag : boolean) boolean$
        + isPrime(number : int) boolean$
        + isGrothendieck(number : int) boolean$
        + isRamanujan(number : int) boolean$
    }


    class Player {
        <<interface>>
        + getName() String
        + add(card : Card) void
        + get(index : int) Card
        + remove(index : int) Card
        + removeAll(list : List < Card >) void
        + size() int
        + sort() void
        + selectHand() List < String >
        + selectMenu(flag : boolean) int
    }

    class AbstractPlayer {
        - list : List < Card >
        - name : String
        + AbstractPlayer(name : String)
        + getName() String
        + add(card : Card) void
        + get(index : int) Card
        + remove(index : int) Card
        + removeAll(list : List < Card >) void
        + size() int
        + sort() void
        + selectHand() List < String >*
        + selectMenu(flag : boolean) int*
    }

    class People {
        + People(name : String)
        + selectHand() List < String >
        + selectMenu(flag : bpolean) int
    }

    class Computer {
        + Computer(name : String)
        + selectHand() List < String >
        + selectMenu(flag : bpolean) int
    }
```

## 実行
適当にコンパイルして
java Main

## 展望
全部コンパイルしていちいちCUIでjava Mainして……面倒すぎるのでいつかGUI周りも漁ってJarファイル形式で手軽に配布まで行けたらいいなと思ったり。
