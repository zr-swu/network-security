import java.util.ArrayList;
import java.util.List;


/**
 * Playfair密码加密
 * @author zr
 * @create 2021-03-25-19:27
 */
public class Playfair {

    /**
     * 处理明文，重复字母中间插X，长度非偶数添加X
     * @author zr
     * @param ming 明文
     * @return 处理后的明文
     */
    public static String dealMing(String ming){
        ming=ming.toUpperCase();// 将明文转换成大写
        ming=ming.replaceAll("[^A-Z]", "");//去除所有非字母的字符
        StringBuilder sb=new StringBuilder(ming);
        for(int i=1;i<sb.length();i=i+2){
            //一对明文字母如果是重复的则在这对明文字母之间插入一个填充字符
            if(sb.charAt(i)==sb.charAt(i-1)){
                sb.insert(i,'X');
            }
        }
        //如果经过处理后的明文长度非偶数，则在后面加上字母x
        if(sb.length()%2!=0){
            sb.append('X');
        }
        String p=sb.toString();
        System.out.println("去重后的密钥(I,J都用I表示)："+p);
        return p;
    }

    /**
     * 处理密钥，将J转换成I（I,J同一个字母表示），除去重复字母
     * @author zr
     * @param Key 密钥
     * @return 修改后的密钥List<Character>
     */
    public static List<Character> dealKey(String Key){
        Key=Key.toUpperCase();// 将密钥转换成大写
        Key=Key.replaceAll("[^A-Z]", "");//去除所有非字母的字符
        Key=Key.replaceAll("J","I");//将J转换成I
        List<Character> list=new ArrayList<Character>();
        char[] ch=Key.toCharArray();
        int len=ch.length;
        for(int i=0;i<len;i++){
            //除去重复出现的字母
            if(!list.contains(ch[i])){
                list.add(ch[i]);
            }
        }
        System.out.println("处理后的密钥："+list);
        return list;
    }

    /**
     * 将密玥的字母逐个加入5×5的矩阵内，剩下的空间将未加入的英文字母
     * 依a-z的顺序加入。（将I和J视作同一字。JOY -> IOY）
     * @author zr
     * @param Key 密钥
     * @return 5*5矩阵
     */
    public static char[][] matrix(String Key){
        List<Character> key=dealKey(Key);
        //添加在K中出现的字母
        List<Character> list=new ArrayList<Character>(key);
        //添加按字母表顺序排序的剩余的字母
        for(char ch='A';ch<='Z';ch++){
            if(ch!='J'&&!list.contains(ch)){
                list.add(ch);
            }
        }
        char[][] matrix=new char[5][5];
        int count=0;
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                matrix[i][j]=list.get(count++);
            }
        }
        System.out.println("使用密钥'"+Key+"'构建的矩阵：");
        for(int i=0;i<matrix.length;i++){
            for(int j=0;j<matrix[i].length;j++){
                System.out.print(matrix[i][j]+" ");
            }
            System.out.println();
        }
        return matrix;
    }



    /**
     * 根据playfair算法对明文对进行加密
     * @author zr
     * @param matrix 矩阵
     * @param ch1 字母1
     * @param ch2 字母2
     * @return 密文对
     */
    public static String encrypt(char[][] matrix,char ch1,char ch2){
        //获取明文对在矩阵中的位置
        int r1=0,c1=0,r2=0,c2=0;
        for(int i=0;i<matrix.length;i++){
            for(int j=0;j<matrix[i].length;j++){
                if(ch1==matrix[i][j]){
                    r1=i;
                    c1=j;
                }
                if(ch2==matrix[i][j]){
                    r2=i;
                    c2=j;
                }
            }
        }
        StringBuilder sb=new StringBuilder();
        //进行规制匹配，得到密文对
        if(r1==r2){
            //P1、P2同行：
            //对应的C1和C2分别是紧靠P1、P2右端的字母。其中第一列被看作是最后一列的右方。
            sb.append(matrix[r1][(c1+1)%5]);
            sb.append(matrix[r1][(c2+1)%5]);
        }else if(c1==c2){
            //P1、P2同列：
            //对应的C1和C2分别是紧靠P1、P2下方的字母。其中第一行看作是最后一行的下方
            sb.append(matrix[(r1+1)%5][c1]);
            sb.append(matrix[(r2+1)%5][c1]);
        }else{
            //P1、P2不同行、不同列：
            //C1和C2是由P1和P2确定的矩形的其它两角的字母，并且C1和P1、C2和P2同行。
            sb.append(matrix[r1][c2]);
            sb.append(matrix[r2][c1]);
        }
        sb.append(' ');
        return sb.toString();
    }

    /**
     * 对明文进行加密
     * @author zr
     * @param ming 明文
     * @param key 密钥
     * @return 密文
     */
    public static String encrypt(String ming,String key){
        char[] ch=dealMing(ming).toCharArray();
        char[][] matrix=matrix(key);
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<ch.length;i=i+2){
            sb.append(encrypt(matrix,ch[i],ch[i+1]));
        }
        return sb.toString();
    }

    /**
     * 根据playfair算法对密文对进行解密
     * @author zr
     * @param matrix
     * @param ch1 字母1
     * @param ch2 字母2
     * @return 明文对
     */
    public static String decrypt(char[][] matrix,char ch1,char ch2){
        //获取密文对在矩阵中的位置
        int r1=0,c1=0,r2=0,c2=0;
        for(int i=0;i<matrix.length;i++){
            for(int j=0;j<matrix[i].length;j++){
                if(ch1==matrix[i][j]){
                    r1=i;
                    c1=j;
                }
                if(ch2==matrix[i][j]){
                    r2=i;
                    c2=j;
                }
            }
        }
        StringBuilder sb=new StringBuilder();
        //进行规制匹配，得到明文对
        if(r1==r2){
            //密文字母对的两个字母在同一行，则截取左边的字母
            sb.append(matrix[r1][(c1-1+5)%5]);
            sb.append(matrix[r1][(c2-1+5)%5]);
        }else if(c1==c2){
            //密文字母对的两个字母在同一列，则截取上方的字母
            sb.append(matrix[(r1-1+5)%5][c1]);
            sb.append(matrix[(r2-1+5)%5][c1]);
        }else{
            //密文字母所形成的矩形对角线上的两个字母，任意选择两种方向
            sb.append(matrix[r1][c2]);
            sb.append(matrix[r2][c1]);
            //sb.append(matrix[r2][c1]);
            //sb.append(matrix[r1][c2]);
        }
        sb.append(' ');
        return sb.toString();
    }

    /**
     * 对密文进行解密
     * @author zr
     * @param cipher 密文
     * @param key 密钥
     * @return 明文
     */
    public static String decrypt(String cipher,String key){
        cipher=cipher.toUpperCase();
        cipher=cipher.replaceAll("[^A-Z]", "");//去除所有非字母的字符
        char[] ch=cipher.toCharArray();
        char[][] matrix=matrix(key);
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<ch.length;i=i+2){
            sb.append(decrypt(matrix,ch[i],ch[i+1]));
        }
        return sb.toString();
    }

    /**
     * 测试
     *
     * @author zr
     * @param args
     */
    public static void main(String args[]){
        String ming="playfair cipher";

        String key="PLAY FAIR IS A DIGRAM CIPHER";// 密钥

        key = key.replace(" ", ""); //去除中间空格

        String cipher="";// 密文
        if(key.length()<=25){
            System.out.println("===========加密===========");
            cipher=encrypt(ming,key);
            System.out.println("加密后的密文："+cipher);
            System.out.println();
            System.out.println("===========解密===========");
            ming=decrypt(cipher,key);
            System.out.println("解密后的明文："+ming);
        }else{
            System.out.println("密钥大于25个字符");
        }
    }
}


