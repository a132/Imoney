package guolei.imoney.util;

/**
 * Created by guolei on 2016/6/11.
 */
public class StringHelper {
    public static String getBlankString(int num){
        int length = num;
        String str = "";
        while(length-- > 0){
            str += " ";
        }
        return str;
    }
}
