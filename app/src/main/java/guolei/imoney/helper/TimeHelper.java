package guolei.imoney.helper;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by guolei on 2016/6/9.
 */
public class TimeHelper {
    public static long oneDay = 24 * 60 * 60 * 1000;  //ms

    public static long getFisrtDay(EnumHelper.conditionEnum condition){
        Calendar cal = GregorianCalendar.getInstance(Locale.FRANCE);
        switch (condition){
            case YEAR:
                cal.set(Calendar.MONTH,0);
                cal.set(Calendar.DAY_OF_MONTH,cal.getActualMinimum(Calendar.DAY_OF_MONTH));
                break;
            case MONTH:
                cal.set(Calendar.DAY_OF_MONTH,cal.getActualMinimum(Calendar.DAY_OF_MONTH));
                break;
            case WEEK:
                cal.set(Calendar.DAY_OF_WEEK,2);
                break;
            case DAY:
                break;
        }
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE,0);
        return cal.getTimeInMillis();
    }
    //获得已过去的月的数目     june -> 5
    public static int getPastMonth(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.get(Calendar.MONTH);
    }
    // 获得本月已过去的天数
    public static int getPastDayOfMonth(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.get(Calendar.DAY_OF_MONTH);
    }
    //获得本周已过去的天数  thursday -> 5
    public static int getPastDaysOfWeek(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int days = cal.get(Calendar.DAY_OF_WEEK);
        return  days == Calendar.SUNDAY ? 8 : days;
    }

    public static int getDaysOfMonth(int month){
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        Calendar cal = new GregorianCalendar(currentYear,month,1);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static int TotalDaysOfMonth(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static int getDayofWeek(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.get(Calendar.DAY_OF_WEEK)-1;
    }
    public static int[] MonthDay = {31};
}
