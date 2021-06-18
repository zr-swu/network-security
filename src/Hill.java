import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author zr
 * @create 2021-06-18-19:36
 */
public class Hill {

    /**
     * 输入密钥矩阵
     *
     * @return 2x2 array of int with the key matrix
     * @author zr
     */
    private static int[][] getKeyMatrix() {
        int[][] keyMatrix = new int[2][2];
        Scanner keyboard = new Scanner(System.in);

        System.out.println("Enter matrix values (a,b,c,d)");
        System.out.println("| a  b |");
        System.out.println("| c  d |");
        System.out.println();
        System.out.print("a: ");
        keyMatrix[0][0] = keyboard.nextInt();
        System.out.print("b: ");
        keyMatrix[0][1] = keyboard.nextInt();
        System.out.print("c: ");
        keyMatrix[1][0] = keyboard.nextInt();
        System.out.print("d: ");
        keyMatrix[1][1] = keyboard.nextInt();

        return keyMatrix;
    }

    /**
     * @param keyMatrix Original key matrix 2x2
     * @author zr
     */
    private static void isValidMatrix(int[][] keyMatrix) {
        int det = keyMatrix[0][0] * keyMatrix[1][1] - keyMatrix[0][1] * keyMatrix[1][0];

        if (det == 0) {
            throw new java.lang.Error("Det equals to zero, invalid key matrix!");
        }
    }

    /**
     * 检查逆矩阵是否合法
     *
     * @param keyMatrix     Original key matrix 2x2
     * @param reverseMatrix Reverse key matrix found in previous calls
     * @author zr
     */
    private static void isValidReverseMatrix(int[][] keyMatrix, int[][] reverseMatrix) {
        int[][] product = new int[2][2];

        product[0][0] = (keyMatrix[0][0] * reverseMatrix[0][0] + keyMatrix[0][1] * reverseMatrix[1][0]) % 26;
        product[0][1] = (keyMatrix[0][0] * reverseMatrix[0][1] + keyMatrix[0][1] * reverseMatrix[1][1]) % 26;
        product[1][0] = (keyMatrix[1][0] * reverseMatrix[0][0] + keyMatrix[1][1] * reverseMatrix[1][0]) % 26;
        product[1][1] = (keyMatrix[1][0] * reverseMatrix[0][1] + keyMatrix[1][1] * reverseMatrix[1][1]) % 26;


        if (product[0][0] != 1 || product[0][1] != 0 || product[1][0] != 0 || product[1][1] != 1) {
            throw new java.lang.Error("Invalid reverse matrix found!");
        }
    }

    /**
     * 求逆矩阵
     *
     * @param keyMatrix Original key matrix 2x2
     * @return 2x2 Reverse key matrix
     * @author zr
     */
    private static int[][] reverseMatrix(int[][] keyMatrix) {
        int detmod26 = (keyMatrix[0][0] * keyMatrix[1][1] - keyMatrix[0][1] * keyMatrix[1][0]) % 26; // Calc det
        int factor;
        int[][] reverseMatrix = new int[2][2];

        // factor*det = 1 mod 26
        for (factor = 1; factor < 26; factor++) {
            if ((detmod26 * factor) % 26 == 1) {
                break;
            }
        }

        reverseMatrix[0][0] = keyMatrix[1][1] * factor % 26;
        reverseMatrix[0][1] = (26 - keyMatrix[0][1]) * factor % 26;
        reverseMatrix[1][0] = (26 - keyMatrix[1][0]) * factor % 26;
        reverseMatrix[1][1] = keyMatrix[0][0] * factor % 26;

        return reverseMatrix;
    }

    /**
     * 输出结果
     * @author zr
     * @param label  Label (encrypt/decrypt)
     * @param phrase Phrase to convert to characters and split in pairs
     */
    private static void echoResult(String label, ArrayList<Integer> phrase) {
        int i;
        System.out.print(label);

        // Loop for each pair
        for (i = 0; i < phrase.size(); i += 2) {
            System.out.print(Character.toChars(phrase.get(i) + 64 + 1));
            System.out.print(Character.toChars(phrase.get(i + 1) + 64 + 1));
            if (i + 2 < phrase.size()) {
                System.out.print("-");
            }
        }
        System.out.println();
    }

    /**
     * 加密
     * @author zr
     * @param phrase Original phrase from keyboard to encrypt
     */
    public static String encrypt(String phrase) {
        int i;
        int[][] keyMatrix;
        ArrayList<Integer> phraseToNum = new ArrayList<>();
        ArrayList<Integer> phraseEncoded = new ArrayList<>();

        phrase = phrase.replaceAll("[^a-zA-Z]", "").toUpperCase();

        if (phrase.length() % 2 == 1) {
            phrase += "Q";
        }

        keyMatrix = getKeyMatrix();

        isValidMatrix(keyMatrix);


        for (i = 0; i < phrase.length(); i++) {
            phraseToNum.add(phrase.charAt(i) - (64 + 1));
        }

        for (i = 0; i < phraseToNum.size(); i += 2) {
            phraseEncoded.add((keyMatrix[0][0] * phraseToNum.get(i) + keyMatrix[1][0] * phraseToNum.get(i + 1)) % 26);
            phraseEncoded.add((keyMatrix[0][1] * phraseToNum.get(i) + keyMatrix[1][1] * phraseToNum.get(i + 1)) % 26);

            System.out.println("(" + (keyMatrix[0][0] * phraseToNum.get(i) + keyMatrix[1][0] * phraseToNum.get(i + 1)) % 26
                    + "," + (keyMatrix[0][1] * phraseToNum.get(i) + keyMatrix[1][1] * phraseToNum.get(i + 1)) % 26 + ")");
        }


        // Print the result
        echoResult("Encoded phrase: ", phraseEncoded);

        StringBuilder builder = new StringBuilder();
        for (i = 0; i < phraseEncoded.size(); i += 2) {
            builder.append(Character.toChars(phraseEncoded.get(i) + 64 + 1));
            builder.append(Character.toChars(phraseEncoded.get(i + 1) + 64 + 1));
            if (i + 2 < phraseEncoded.size()) {
                builder.append("-");
            }
        }
        return builder.toString();
    }

    /**
     * 解密
     * @author zr
     * @param phrase Original phrase from keyboard to decrypt
     */
    public static void decrypt(String phrase) {
        int i;
        int[][] keyMatrix, revKeyMatrix;
        ArrayList<Integer> phraseToNum = new ArrayList<>();
        ArrayList<Integer> phraseDecoded = new ArrayList<>();

        phrase = phrase.replaceAll("[^a-zA-Z]", "").toUpperCase();

        keyMatrix = getKeyMatrix();

        isValidMatrix(keyMatrix);

        for (i = 0; i < phrase.length(); i++) {
            phraseToNum.add(phrase.charAt(i) - (64 + 1));
        }

        revKeyMatrix = reverseMatrix(keyMatrix);

        isValidReverseMatrix(keyMatrix, revKeyMatrix);

        for (i = 0; i < phraseToNum.size(); i += 2) {
            phraseDecoded.add((revKeyMatrix[0][0] * phraseToNum.get(i) + revKeyMatrix[1][0] * phraseToNum.get(i + 1)) % 26);
            phraseDecoded.add((revKeyMatrix[0][1] * phraseToNum.get(i) + revKeyMatrix[1][1] * phraseToNum.get(i + 1)) % 26);
            System.out.println("(" + (revKeyMatrix[0][0] * phraseToNum.get(i) + revKeyMatrix[1][0] * phraseToNum.get(i + 1)) % 26
                    + "," + (revKeyMatrix[0][1] * phraseToNum.get(i) + revKeyMatrix[1][1] * phraseToNum.get(i + 1)) % 26 + ")");
        }

        // Print the result
        echoResult("Decoded phrase: ", phraseDecoded);
    }

    /**
     * 测试
     * @author zr
     * @param args
     */
    public static void main(String[] args) {
        String opt, phrase;
        byte[] p;

        Scanner keyboard = new Scanner(System.in);
        System.out.println("Hill implementation (2x2)");

        System.out.println("===========加密===========");

        System.out.print("请输入明文: ");
        phrase = keyboard.nextLine();
        String encrypt = encrypt(phrase);


        System.out.println();

        System.out.println("===========解密===========");
        decrypt(encrypt);

    }
}

