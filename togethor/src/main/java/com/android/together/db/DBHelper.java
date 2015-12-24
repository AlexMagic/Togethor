package com.android.together.db;



import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
	
	private Context context;
	
	private static final String DB_NAME = "together.db";
	
	//·�߻����
	
	
	
	//�ϵ����ر�
	public static final String MUTITREAD_DOWNLOAD_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS filedownlog (id integer primary key autoincrement, downpath varchar(100), threadid INTEGER, downlength INTEGER)";

	public static final int DATABASE_VERSION= 0;
	private static DBHelper instance;
	
	
	
	public static DBHelper getInstance(Context context){
		if(instance == null){
			instance = new DBHelper(context.getApplicationContext());
		}
		
		return instance;
	}
	
	private DBHelper(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		//db.execSQL(MUTITREAD_DOWNLOAD_CREATE_TABLE);
	}

	//���DATABASE_VERSIONֵ����Ϊ2,ϵͳ�����������ݿ�汾��ͬ,�������onUpgrade  
    @Override  
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
    	context.deleteDatabase(DB_NAME);
        onCreate(db);
    } 
    
    
    
    
   
	
	public void closeDB(){
		if(instance != null){
		 try {
	        SQLiteDatabase db = instance.getWritableDatabase();
	        db.close();
		 }catch (Exception e) {
		        e.printStackTrace();
		    }
		    instance = null;
		}
	}

}
