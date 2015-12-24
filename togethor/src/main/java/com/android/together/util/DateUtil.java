package com.android.together.util;

import android.annotation.SuppressLint;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static void getTimeByDate(){
        Date date = new Date();
        DateFormat df1 = DateFormat.getDateInstance();//���ڸ�ʽ����ȷ����
        System.out.println(df1.format(date));
        DateFormat df2 = DateFormat.getDateTimeInstance();//���Ծ�ȷ��ʱ����
        System.out.println(df2.format(date));
        DateFormat df3 = DateFormat.getTimeInstance();//ֻ��ʾ��ʱ����
        System.out.println(df3.format(date));
        DateFormat df4 = DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.FULL); //��ʾ���ڣ��ܣ������磬ʱ�䣨��ȷ���룩
        System.out.println(df4.format(date)); 
        DateFormat df5 = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.LONG); //��ʾ����,�����磬ʱ�䣨��ȷ���룩
        System.out.println(df5.format(date));
        DateFormat df6 = DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.SHORT); //��ʾ���ڣ�������,ʱ�䣨��ȷ���֣�
        System.out.println(df6.format(date));
        DateFormat df7 = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM); //��ʾ���ڣ�ʱ�䣨��ȷ���֣�
        System.out.println(df7.format(date));
    }
    public static void getTimeByCalendar(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);//��ȡ���
        int month=cal.get(Calendar.MONTH);//��ȡ�·�
        int day=cal.get(Calendar.DATE);//��ȡ��
        int hour=cal.get(Calendar.HOUR);//Сʱ
        int minute=cal.get(Calendar.MINUTE);//��           
        int second=cal.get(Calendar.SECOND);//��
        int WeekOfYear = cal.get(Calendar.DAY_OF_WEEK);//һ�ܵĵڼ���
        System.out.println("���ڵ�ʱ���ǣ���Ԫ"+year+"��"+month+"��"+day+"��      "+hour+"ʱ"+minute+"��"+second+"��       ����"+WeekOfYear);
    }
    
    @SuppressLint("SimpleDateFormat")
	public static long changeDateFomate(String time){
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    	Date date1 = null;
    	
		try {
			date1 = format.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return date1.getTime();
    }
    
    public static String changeLong(long time){
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    	Date d = new Date(time);
    	
    	return format.format(d);
    }
    
}
