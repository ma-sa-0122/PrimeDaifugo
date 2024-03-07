public class Checker {
    public static boolean isSameCardsNum(int cardsNum, int cardsNumPrev) {
        return cardsNum == cardsNumPrev;
    }
    public static boolean isGreater(int num1, int num2, boolean flag) {
        if (flag) return num1 < num2;
        else return num1 > num2;
    }
    public static boolean isPrime(int number) {
        if (number == 1) return false;
        if (number == 2) return true;
        if (number % 2 != 0) {
            double root = Math.sqrt(number);
            for (int i = 3; i <= root; i += 2) {
                if (number % i == 0) {
                    return false;
                }
            }
            return true;
        }
        return false; 
    }
    public static boolean isGrothendieck(int number) {
        return number == 57;
    }
    public static boolean isRamanujan(int number) {
        return number == 1729;
    }
}
