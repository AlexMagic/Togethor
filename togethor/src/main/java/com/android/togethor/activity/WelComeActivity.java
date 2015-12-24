package com.android.togethor.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.together.BaseActivity;
import com.android.together.MyApplication;
import com.android.together.R;
import com.android.together.annotation.ContentView;
import com.android.together.annotation.ViewInject;
import com.android.together.constant.Constant;
import com.android.together.util.JsonUtil;
import com.android.volley.Request;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

@ContentView(R.layout.activity_welcome)
public class WelComeActivity extends BaseActivity {

	private static final int ANIMATION_DURATION = 2000;
	private static final int ALPHA_ANIMATION_DURATION = 2000;

    private static float mDistance = 500;
	
    public float factor = 0.5f;
    
	@ViewInject(R.id.tv_content)
	private TextView tv_content;
	
	@ViewInject(R.id.layout_welcome)
	private RelativeLayout layout_welcome;
	
	@ViewInject(R.id.progress)
	private ProgressBar bar;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		super.init();
		
		freeFall();
	}
	
	@Override
	public void respone(String response) {
		// TODO Auto-generated method stub
		super.respone(response);
		
		Map<String , Object> map = JsonUtil.Json2Map(response);
		if(map!=null){
			int status = (Integer) map.get("status");
			String code = (String) map.get("code");
			String msg = (String) map.get("message");
			//MyApplication.getInstance().setLineStatus(-1);
			if(code.equals("200")){
				System.out.println("MyApplication.getInstance().getLineStatus()--:"+MyApplication.getInstance().getLineStatus());
				if(status==1){
					
					if(MyApplication.getInstance().getLineStatus()==1){
						Intent pass_intent = new Intent(WelComeActivity.this,DriverWaitingActivity.class);
						pass_intent.putExtra("line_id", MyApplication.getInstance().getLineId());
						startActivity(pass_intent);
						overridePendingTransition(R.anim.roll_left	, R.anim.roll_right);
						finish();
					}else{
						Intent pass_intent = new Intent(WelComeActivity.this,DriverWaitingActivity.class);
						pass_intent.putExtra("line_id", MyApplication.getInstance().getLineId());
						startActivity(pass_intent);
						overridePendingTransition(R.anim.roll_left	, R.anim.roll_right);
						finish();
					}
				}else if(status==2){
					Intent pass_intent = new Intent(WelComeActivity.this,NavigationActivity.class);
					pass_intent.putExtra("line_id", MyApplication.getInstance().getLineId());
					startActivity(pass_intent);
					overridePendingTransition(R.anim.roll_left	, R.anim.roll_right);
					finish();
				}else{
					startActivity(NearLineActivity.class);
					overridePendingTransition(R.anim.roll_left	, R.anim.roll_right);
					finish();
				}
			}
		}else{
			startActivity(NearLineActivity.class);
			overridePendingTransition(R.anim.roll_left	, R.anim.roll_right);
			finish();
		}
	}
	
	@Override
	public void setListner() {
		
	}
	
	/**
     * ÏÂÂä
     */
    public void freeFall() {

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(tv_content, "translationY", 0, 150);
        final ObjectAnimator scaleIndication_2 = ObjectAnimator.ofFloat(tv_content, "alpha", 0, 1.0f);
        final ObjectAnimator scaleIndication = ObjectAnimator.ofFloat(layout_welcome, "alpha", 0, 1.0f);
        scaleIndication.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator arg0) {
				System.out.println("end");
				bar.setVisibility(View.VISIBLE);
				if(MyApplication.getInstance().getUserName()!=null && MyApplication.getInstance().getPwd()!=null){
					if(MyApplication.getInstance().getLineId()!=-1){
						
						Map<String , String> map = new HashMap<String, String>();
						map.put("method", "get_line_status");
						map.put("line_id", MyApplication.getInstance().getLineId()+"");
						
						string_request(Constant.LINE_URL, Request.Method.POST, map);
					}else{
						startActivity(NearLineActivity.class);
						overridePendingTransition(R.anim.roll_left	, R.anim.roll_right);
						finish();
					}
				}else{
					startActivity(LoginActivity.class);
					overridePendingTransition(R.anim.roll_left	, R.anim.roll_right);
					finish();
				}
			}
			
			@Override
			public void onAnimationCancel(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
		});
      
        objectAnimator.setDuration(ANIMATION_DURATION);
        scaleIndication.setDuration(ALPHA_ANIMATION_DURATION);
       // objectAnimator.setInterpolator(new AccelerateInterpolator(factor));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(ANIMATION_DURATION);
        animatorSet.playTogether(objectAnimator,scaleIndication_2);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
            	layout_welcome.setVisibility(View.VISIBLE);
            	//animation.start();
            	scaleIndication.start();

            	
            	
//                mShapeLoadingView.changeShape();
//                upThrow();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();


    }


}
