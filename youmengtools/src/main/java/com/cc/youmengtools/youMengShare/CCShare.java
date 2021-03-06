package com.cc.youmengtools.youMengShare;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.cc.youmengtools.permissions.CCPermission;
import com.cc.youmengtools.utils.IToast;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.List;

/**
 * Title （标题）:
 * Created by （作者）: MapleChan
 * e-mail （邮箱）: cc7126@139.com
 * date （时间）: 2018/11/28 16:00
 * Describe（描述）:
 */
public class CCShare {

	private Context context;
	private UMShareListener shareListener;

	public CCShare(Context context, UMShareListener shareListener) {
		this.context = context;
		this.shareListener = shareListener;
	}

	/**
	 * 分享QQ和微信，友盟的弹窗（测试用）
	 * @param activity
	 */
	public void shareToQQandWeiChat(String string,Activity activity) {
		new ShareAction(activity).withText(string).setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
				.setCallback(shareListener).open();
	}

	/**
	 * 分享到QQ
	 *
	 * @param title
	 * @param shareUrl
	 * @param imgUrl
	 * @param text
	 */
	public void shareToQQ(String title, String shareUrl, String imgUrl, String text) {
		if (isQQClientAvailable(context)) {
			if (CCPermission.checkPermission((Activity) context,
					new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200)) {
				UMImage image = new UMImage(context, imgUrl);//网络图片
				UMWeb web = new UMWeb(shareUrl);
				web.setTitle(title);//标题
				web.setThumb(image);  //缩略图
				web.setDescription(text);//描述
				new ShareAction((Activity) context)
						.setPlatform(SHARE_MEDIA.QQ)//传入平台
						.withMedia(web)
						.setCallback(shareListener)//回调监听器
						.share();
			} else {
				IToast.show(context, "请允许权限！");
			}
		} else {
			IToast.show(context, "请安装QQ！");
		}
	}

	/**
	 * 分享到微信
	 *
	 * @param title
	 * @param shareUrl
	 * @param imgUrl
	 * @param text
	 */
	public void shareWeChat(String title, String shareUrl, String imgUrl, String text) {
		if (isWeixinAvilible(context)) {
			UMImage image = new UMImage(context, imgUrl);//网络图片
			UMWeb web = new UMWeb(shareUrl);
			web.setTitle(title);//标题
			web.setThumb(image);  //缩略图
			web.setDescription(text);//描述
			new ShareAction((Activity) context)
					.setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
					.withMedia(web)
					.setCallback(shareListener)//回调监听器
					.share();
		} else {
			IToast.show(context, "请安装微信！");
		}
	}

	/**
	 * 分享到微信朋友圈
	 *
	 * @param title
	 * @param shareUrl
	 * @param imgUrl
	 * @param text
	 */
	public void shareWeChatFirend(String title, String shareUrl, String imgUrl, String text) {
		if (isWeixinAvilible(context)) {
			UMImage image = new UMImage(context, imgUrl);//网络图片
			UMWeb web = new UMWeb(shareUrl);
			web.setTitle(title);//标题
			web.setThumb(image);  //缩略图
			web.setDescription(text);//描述
			new ShareAction((Activity) context)
					.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)//传入平台
					.withMedia(web)
					.setCallback(shareListener)//回调监听器
					.share();
		} else {
			IToast.show(context, "请安装微信！");
		}
	}

	/**
	 * 复制
	 *
	 * @param context
	 * @param shareUrl
	 */
	public void copyText(Context context, String shareUrl) {
		//获取剪贴板管理器：
		ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		// 创建普通字符型ClipData
		ClipData mClipData = ClipData.newPlainText("Label", shareUrl);
		// 将ClipData内容放到系统剪贴板里。
		cm.setPrimaryClip(mClipData);
		IToast.show(context, "复制成功！");
	}

	/**
	 * 判断微信是否可用
	 *
	 * @param context
	 * @return
	 */
	public static boolean isWeixinAvilible(Context context) {
		// 获取packagemanager
		final PackageManager packageManager = context.getPackageManager();
		// 获取所有已安装程序的包信息
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
		if (pinfo != null) {
			for (int i = 0; i < pinfo.size(); i++) {
				String pn = pinfo.get(i).packageName;
				if (pn.equals("com.tencent.mm")) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断qq是否可用
	 *
	 * @param context
	 * @return
	 */
	public static boolean isQQClientAvailable(Context context) {
		final PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
		if (pinfo != null) {
			for (int i = 0; i < pinfo.size(); i++) {
				String pn = pinfo.get(i).packageName;
				if (pn.equals("com.tencent.mobileqq")) {
					return true;
				}
			}
		}
		return false;
	}
}
