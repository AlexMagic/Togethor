package com.android.together.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.sina.push.MPSConsts;
import com.sina.push.model.ActionResult;
import com.sina.push.response.PushDataPacket;
import com.sina.push.service.message.GdidServiceMsg;
import com.sina.push.utils.LogUtil;

public class SDKMsgReceiver extends BroadcastReceiver {

	private String log = new String();

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		LogUtil.debug("SDKMsgReceiver receive Msg.....");

		int msg_type = intent.getIntExtra(MPSConsts.CMD_ACTION, -1);

		switch (msg_type) {

		case MPSConsts.MSG_TYPE_GET_GDID:

			Bundle msgBundle = intent.getBundleExtra(MPSConsts.KEY_MSG_GDID);

			GdidServiceMsg msg = new GdidServiceMsg();

			msg = (GdidServiceMsg) msg.parserFromBundle(msgBundle);

			LogUtil.debug("SDKMsgReceiver gdid ===========" + msg.getGdid());

			Toast.makeText(context, msg.getGdid(), Toast.LENGTH_LONG).show();
			break;
		case MPSConsts.MSG_TYPE_MPS_PUSH_DATA:

			PushDataPacket packet = intent
					.getParcelableExtra(MPSConsts.KEY_MSG_MPS_PUSH_DATA);

			log = "received MPS push:[appid=" + packet.getAppID() + ",msgID="
					+ packet.getMsgID() + ",srcJson=" + packet.getSrcJson()
					+ "\n";

			Toast.makeText(context, "onPush data: " + packet.getSrcJson(),
					Toast.LENGTH_LONG).show();

			break;
		case MPSConsts.MSG_CHANNEL_HAS_BEEN_BUILDED:

			ActionResult actResult = intent
					.getParcelableExtra(MPSConsts.KEY_MSG_ACTION_SWITCH_CHANNEL);

			log = actResult + "\n";

			Toast.makeText(context, log, Toast.LENGTH_LONG).show();

			if (actResult.getResultCode() == 1) {
				// 打开通道成功，可以正常接收Push和调用接口功能

			}
			break;

		case MPSConsts.MSG_TYPE_SAE_DATA:
			String aid = intent.getStringExtra(MPSConsts.KEY_MSG_SAE_DATA);
			LogUtil.info("SDKMsgReceiver aid ===========" + aid);
			// Toast.makeText(context, aid, Toast.LENGTH_LONG).show();
//			MainActivity.tv_aid.setText("aid:" + aid);
			break;
		}

	}

}
