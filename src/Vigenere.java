/**
 * @author zr
 * @create 2021-06-17-16:41
 */
public class Vigenere {
    /**
     * 加密
     * @author zr
     * @param ming 明文
     * @param key 密钥
     * @return 密文
     */
    public static String encrypt(String ming, String key){
        String ciphertext = "";
        char[][] chars = new char[2][ming.length()];
        for (int i = 0; i <ming.length() ; i++) {
            chars[0][i]=ming.charAt(i);
            chars[1][i]=key.toUpperCase().charAt(i%(key.length()));
        }
        char[] charArray = ming.toCharArray();
        for (int i = 0; i <charArray.length ; i++) {
            int j = charArray[i];
            if (j>=97&&j<=97+26){
                int k = chars[1][i];
                char te = (char) (((j - 97) + (k - 65)) % 26+65);
                ciphertext = ciphertext+te;
            }
            if (j>=65&&j<=65+26){
                int k = chars[1][i];
                char te = (char) (((j - 65) + (k - 65)) % 26+97);
                ciphertext = ciphertext+te;
            }
        }
        return  ciphertext;
    }

    /**
     * 解密
     * @author zr
     * @param cipher 密文
     * @param key 密钥
     * @return 明文
     */
    public static String decrypt(String cipher, String key){
        String plaintext="";
        char[][] chars = new char[2][cipher.length()];
        for (int i = 0; i <cipher.length() ; i++) {
            chars[0][i]=cipher.charAt(i);
            chars[1][i]=key.toUpperCase().charAt(i%(key.length()));
        }
        char[] charArray = cipher.toCharArray();
        for (int i = 0; i <charArray.length ; i++) {
            int j = charArray[i];
            if (j>=97&&j<=97+26){
                int k = chars[1][i];
                char te = (char) (((j - 97) + 26-(k - 65)) % 26+65);
                plaintext = plaintext+te;
            }
            if (j>=65&&j<=65+26){
                int k = chars[1][i];
                char te = (char) (((j - 65) + 26-(k - 65)) % 26+97);
                plaintext = plaintext+te;
            }
        }
        return plaintext;
    }

    /**
     * 测试
     *
     * @author zr
     * @param args
     */
    public static void main(String args[]){
        String ming="explanation";

        String key="legleglegle";// 密钥

        String cipher="";// 密文
        System.out.println("===========加密===========");
        cipher=encrypt(ming,key);
        System.out.println("加密后的密文："+cipher);
        System.out.println();
        System.out.println("===========解密===========");
        ming=decrypt(cipher,key);
        System.out.println("解密后的明文："+ming);

    }
}
