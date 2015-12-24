package com.android.together.activity.reg;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.android.together.BaseFragment;
import com.android.together.MyApplication;
import com.android.together.R;
import com.android.together.constant.Constant;
import com.android.together.util.BimpManage;
import com.android.together.util.CommonUtils;
import com.android.together.util.InvalidateUtil;
import com.android.together.view.CircleImageView;
import com.android.together.view.ProgressDialog;

@SuppressLint("ValidFragment")
public class InfoRegFragment extends BaseFragment {
	
	
	
	
	private EditText et_nick;
	private EditText et_mail;
	private EditText et_pwd;
	private EditText et_pwd_confirm;
	
	private CircleImageView iv_head;
	
	private ImageView im_nick;
	private ImageView im_mail;
	private ImageView im_sex;
	private ImageView im_pwd;
	private ImageView im_pwd_confirm;
	
	private RadioGroup radioGroup;
	private RadioButton rb_boy;
	private RadioButton rb_girl;
	
	private boolean isValidate = true;
	
	private String path = "";
	private String fname;
	
	private String pwd;
	
	private Handler mHandler;
	
	private ProgressDialog progressDialog;
	
	private String phone;
	
	public InfoRegFragment(Context context , int layout_id , Handler mHandler){
		super(context , layout_id);
		this.mHandler = mHandler;
	}
	
	
	@Override
	protected void setUpData() {
		
	}

	@Override
	protected void setUpListner() {
		et_nick.addTextChangedListener(new MyTextWatcher(et_nick));
		et_mail.addTextChangedListener(new MyTextWatcher(et_mail));
		et_pwd.addTextChangedListener(new MyTextWatcher(et_pwd));
		et_pwd_confirm.addTextChangedListener(new MyTextWatcher(et_pwd_confirm));
		
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup rb, int checkId) {
				switch (checkId) {
				case R.id.rb_boy:
					break;
				case R.id.rb_girl:
					break;
				}
				
				im_sex.setVisibility(View.GONE);
			}
		});
		
		iv_head.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				new PopupWindows(context, mView);
			}
		});
	}

	@Override
	protected void setUpView() {
		et_nick = (EditText) findViewById(R.id.et_nick);
		et_mail = (EditText) findViewById(R.id.et_mail);
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		et_pwd_confirm = (EditText) findViewById(R.id.et_pwd_confirm);
		
		iv_head = (CircleImageView) findViewById(R.id.iv_head);
		
		im_nick = (ImageView) findViewById(R.id.im_nick);
		im_mail = (ImageView) findViewById(R.id.im_mail);
		im_sex = (ImageView) findViewById(R.id.im_sex);
		im_pwd = (ImageView) findViewById(R.id.im_pwd);
		im_pwd_confirm = (ImageView) findViewById(R.id.im_pwd_confirm);
		
		radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
		rb_boy = (RadioButton) findViewById(R.id.rb_boy);
		rb_girl = (RadioButton) findViewById(R.id.rb_girl);
		
		
	}
	
	/**
	 * 从图库获取图片
	 */
	public void selectPicFromLocal() {
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			System.out.println("sdk_int----:"+"<19");
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");

		} else {
			intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, Constant.REQUEST_LOCAL);
	}
	
	
	private File cameraFile;
	
	/**
	 * 照相获取图片
	 */
	public void selectPicFromCamera() {
		if (!CommonUtils.isExitsSdcard()) {
			Toast.makeText(context, "SD卡不存在，不能拍照", Toast.LENGTH_SHORT).show();
			
			return;
		}

//		File imgDir = new File(IMG_PATH);
//		if(!imgDir.exists()){
//			imgDir.mkdir();
//		}
		
		cameraFile = new File(Constant.IMG_PATH, MyApplication.getInstance().getUserName()+"_"
				+ System.currentTimeMillis() + ".jpg");
		
		cameraFile.getParentFile().mkdirs();
		path = cameraFile.getPath();
		
		startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
				Constant.REQUEST_CAMERA);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
		case Constant.REQUEST_CAMERA:
			System.out.println("path--:"+path);
			fname = MyApplication.getInstance().getUserName();
			setAndUploadUserHead(path);
			break;
		case Constant.REQUEST_LOCAL:
			if (BimpManage.drr.size() < 8 && resultCode == -1) {
				if(data != null){
					Uri uri = data.getData();
					if(uri!=null){              
			            String uriStr=uri.toString();  
			            System.out.println("uriStr-------:"+uriStr);
			            path=uriStr.substring(10,uriStr.length());  
			            System.out.println("pathhh-------:"+path);
			            fname = MyApplication.getInstance().getUserName();
			            path = uri2Path(uri);
			            setAndUploadUserHead(path);
			            
			            if(path.startsWith("com.sec.android.gallery3d")){  
			                Log.e("TEST", "It's auto backup pic path:"+uri.toString());  
			                return;
			            }  
					}
				}
			}
			break;
		}
	}
	
	class MyTextWatcher implements TextWatcher{

		private EditText et;
		
		public MyTextWatcher(EditText et){
			this.et = et;
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			
			switch(et.getId()){
			case R.id.et_nick:
				im_nick.setVisibility(View.GONE);
				break;
			case R.id.et_mail:
				im_mail.setVisibility(View.GONE);
				break;
			case R.id.et_pwd:
				im_pwd.setVisibility(View.GONE);
				break;
			case R.id.et_pwd_confirm:
				im_pwd_confirm.setVisibility(View.GONE);
				break;
			}
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public Map<String , Object> setInfo(){
		Map<String , Object> user_info = new LinkedHashMap<String, Object>();
		
		user_info.put("user_name", et_nick.getText().toString());
		user_info.put("user_mail", et_mail.getText().toString());
		user_info.put("pwd", et_pwd.getText().toString());
		if(rb_boy.isChecked()){
			user_info.put("sex", 0);
		}else{
			user_info.put("sex", 1);
		}
		
		return user_info;
	}

	public boolean isValidate() {
		
		isValidate = true;
		
		if(InvalidateUtil.isNull(et_nick)){
			im_nick.setVisibility(View.VISIBLE);
			isValidate = false;
		}
		
		if(InvalidateUtil.isNull(et_mail) || !InvalidateUtil.matchEmail(et_mail.getText().toString())){
			im_mail.setVisibility(View.VISIBLE);
			isValidate = false;
		}
		
		if(InvalidateUtil.isNull(rb_boy) && InvalidateUtil.isNull(rb_girl)){
			im_sex.setVisibility(View.VISIBLE);
			isValidate = false;
		}
		
		if(InvalidateUtil.isNull(et_pwd)){
			im_pwd.setVisibility(View.VISIBLE);
			isValidate = false;
		}
		
		if(InvalidateUtil.isNull(et_pwd_confirm)){
			im_pwd_confirm.setVisibility(View.VISIBLE);
			isValidate = false;
		}
		
		
		if(!InvalidateUtil.isEqual(et_pwd, et_pwd_confirm)){
			im_pwd_confirm.setVisibility(View.VISIBLE);
			isValidate = false;
		}else{
			pwd = et_pwd_confirm.getText().toString();
		}
		
		return isValidate;
	}


	//自定义Popupwindows
	class PopupWindows extends PopupWindow {

		@SuppressWarnings("deprecation")
		public PopupWindows(Context mContext, View parent) {

			View view = View
					.inflate(mContext, R.layout.item_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_in));
			final LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.roll_up));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					selectPicFromCamera() ;
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					selectPicFromLocal();
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});

			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					if(view != ll_popup){
						dismiss();
					}
				}
			});
		}
	}
	
	
	public String uri2Path(Uri uri)
    {
       int actual_image_column_index;
       String img_path;
       String[] proj = { MediaStore.Images.Media.DATA };
       Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
       actual_image_column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
       cursor.moveToFirst();
       img_path = cursor.getString(actual_image_column_index);
       return img_path;
    }
	
	private void setAndUploadUserHead(final String path){
		Bitmap bm;
		try {
			bm = BimpManage.revitionImageSize(path);
			if(bm!=null)
				iv_head.setImageBitmap(bm);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	public void startUpload(){
		
		
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(!path.equals("")){
					progressDialog = new ProgressDialog(context, "正在上传头像...");
					progressDialog.show();
					uploadFile(Constant.IMAGE_URL+"method=upload_head" , path , MyApplication.getInstance().getUserName()+".jpg");
				}else{
					mHandler.sendEmptyMessage(3);
				}
			}
		}).start();
	}
	
	/* 上传文件至Server的方法 */
    private void uploadFile(String upload_url , String path , String filename)
    {
      String end ="\r\n";
      String twoHyphens ="--";
      String boundary ="*****";
      try
      {
        URL url =new URL(upload_url);
        HttpURLConnection con=(HttpURLConnection)url.openConnection();
        /* 允许Input、Output，不使用Cache */
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);
        /* 设置传送的method=POST */
        con.setRequestMethod("POST");
        /* setRequestProperty */
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
        con.setRequestProperty("Content-Type",
                           "multipart/form-data;boundary="+boundary);
        /* 设置DataOutputStream */
        DataOutputStream ds =
          new DataOutputStream(con.getOutputStream());
       
        
        ds.writeBytes(twoHyphens + boundary + end);
        ds.writeBytes("Content-Disposition: form-data; "+
                      "name=\"file1\";filename=\""+
                      filename +"\""+ end);
        ds.writeBytes(end);  
        /* 取得文件的FileInputStream */
        FileInputStream fStream =new FileInputStream(path);
        /* 设置每次写入1024bytes */
        int bufferSize =1024;
        byte[] buffer =new byte[bufferSize];
        int length =-1;
        /* 从文件读取数据至缓冲区 */
        while((length = fStream.read(buffer)) !=-1)
        {
          /* 将资料写入DataOutputStream中 */
          ds.write(buffer, 0, length);
        }
        ds.writeBytes(end);
        ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
        /* close streams */
        fStream.close();
        ds.flush();
        /* 取得Response内容 */
        InputStream is = con.getInputStream();
        int ch;
        StringBuffer b =new StringBuffer();
        while( ( ch = is.read() ) !=-1 )
        {
        	
          b.append( (char)ch );
        }
        /* 将Response显示于Dialog */
        //showDialog("上传成功"+b.toString().trim());
        System.out.println("成功--:"+b.toString().trim());
        
        if(!b.toString().trim().equals("-1")){
        	progressDialog.dismiss();
        	mHandler.sendEmptyMessage(3);
        }
        
        /* 关闭DataOutputStream */
        ds.close();
        
        
        
      }
      catch(Exception e)
      {
        //showDialog("上传失败"+e);
    	  progressDialog.dismiss();
    	  System.out.println("失败 ");
      }
    }


	public String getPwd() {
		return pwd;
	}


	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

    
    
    
}
