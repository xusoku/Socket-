package com.cooge.media.proxy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.cooge.media.proxy.model.Dmsg;
import com.cooge.media.proxy.model.DownMsg;
import com.cooge.media.proxy.model.FileInfo;
import com.cooge.media.proxy.model.IMsgService;
import com.cooge.media.proxy.model.M3u8;
import com.cooge.media.proxy.service.COService;
import com.cooge.media.proxy.type.M3u8Type;

public class DownLoad {
	
	public final static int STARTDOWN = 2659;
	
	
	
	public static String Url(String path,String filename){
		File ff = new File(path);
		ff = new File(ff,filename);
		String encoede = Base164.encodeToString(ff.getAbsolutePath());
		String url = "http://127.0.0.1:"+LiveProxy.port+"/"+Request.PATH+encoede;
		return url;
	}
	public static String Url(String path){
		File ff = new File(path);
		String encoede = Base164.encodeToString(ff.getAbsolutePath());
		String url = "http://127.0.0.1:"+LiveProxy.port+"/"+Request.PATH+encoede;
		return url;
	}
//断点续传必须先保存m3u8文件才能使用
	public static String DownLoadUrl(String url,String path,String filename) {
		//UrlAnalysis url = new UrlAnalysis("http://pl.youku.com/playlist/m3u8?ts=1419243787&keyframe=1&vid=XODUyNjQxNTY0&sid=242243421404030c29cc7&token=9166&oip=1883341046&type=hd2&did=3151cdbf1449478fad97c27cd5fa755b2fff49fa&ctype=30&ev=1&ep=zpF6YV%2BFKfQ2Bs%2F5mOSC86SLxVjACengXVj9tC617Tl8gEfral6hs46SUelx4PRa");
		try {
			File p = new File(path);
			if(!p.exists()){
				p.mkdirs();
			}
			final FileInfo  fileInfo = new FileInfo();
			File bak = new File(path,filename+".m3u8.bak");
			String html = 	null;
			List<M3u8> ms = null;
			if(bak.exists()){
				InputStream myInput=new FileInputStream(bak);
				html = 	NetUtil.getHtml(myInput);
				ms = M3u8Type.getVideoList(fileInfo,html);
			}else{
				NetUtil netUtil = new NetUtil(); 
				InputStream in = netUtil.getInputStream(url);
				html = 	NetUtil.getHtml(in);
				ms = M3u8Type.getVideoList(fileInfo,html,netUtil);
				///////////////备份m3u8文件///////////////////////
				System.out.println("备份m3u8文件中----------------------------");
				FileOutputStream  fos = new FileOutputStream(bak);
				OutputStreamWriter opw= new OutputStreamWriter(fos,"utf-8");
				PrintWriter pw = new PrintWriter(opw, true);
				pw.println("#EXTM3U");
				pw.println("#EXT-X-TARGETDURATION:10000");
				pw.println("#EXT-X-VERSION:3");
				for(final M3u8 m:ms){
					pw.println(m.getTime());
					pw.println(m.getUrl());
				}
				pw.println("#EXT-X-ENDLIST");
				pw.flush();
				pw.close();
				System.out.println("备份m3u8文件完毕----------------------------");
				//////////////////////////
			}
			File mf = new File(path,filename+".m3u8");
			/////////////////////////删除最后一个文件,防止出现文件损坏///////////////////////////
			if(mf.exists()){
				InputStream newM3u8=new FileInputStream(mf);
				String nhtml = 	NetUtil.getHtml(newM3u8);
				List<M3u8> nms = M3u8Type.getVideoList(nhtml);
				if(nms.size()>0){
					String npath = nms.get(nms.size()-1).getUrl().substring(nms.get(nms.size()-1).getUrl().indexOf(Request.PATH)+Request.PATH.length());
					File fs = new File(Base164.decodeToString(npath));
					if(fs.exists()){
						fs.delete();
					}
					
				}
			}
			/////////////////////////////////////////////////
			FileOutputStream  fos = new FileOutputStream(mf);
			OutputStreamWriter opw= new OutputStreamWriter(fos,"utf-8");
			final PrintWriter pw = new PrintWriter(opw, true);
			pw.println("#EXTM3U");
			pw.println("#EXT-X-TARGETDURATION:10000");
			pw.println("#EXT-X-VERSION:3");
			boolean all = true;
			final long startTime = System.currentTimeMillis();
			for(final M3u8 m:ms){
				String dpath = NetUtil.GetDownloadFile(m.getUrl(),path, new BufferSync(){
					@Override
					public void finish(String path) {
						pw.println(m.getTime());
						String onepath = DownLoad.Url(path);
						double t = Double.valueOf(m.getTime().replaceAll("[^0-9|.]", ""));
						long s = new File(path).length();
						fileInfo.addSize(s);
						fileInfo.addTime(t);
						///////////////////////////////////////////////////////////////
						System.out.println("文件的大小为:------------"+fileInfo.getFileSize());
						System.out.println("当前速度-------------"+(s/(System.currentTimeMillis() - startTime)));
						///////////////////////////////////////////////////////////////
						pw.println(onepath);
					}

					@Override
					public void error() {
						System.out.println("下载被终止:------------");
					}
					
				});
				if(dpath==null){
					all = false;
					break;
				}
			}
			if(all){
				pw.println("#EXT-X-ENDLIST");
				System.out.println("下载完成--------------------------------------------------");
				pw.flush();
			}
			pw.close();
			return mf.getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	
	public static String DownLoadUrl(Context context,final String url,final String path) {
		final String file = String.valueOf(System.currentTimeMillis());
		ServiceConnection conn = new ServiceConnection(){
			@Override
			public void onServiceConnected(ComponentName arg0, IBinder service) {
				System.out.println("开始下载:----------------------");
				Messenger mService = new Messenger(service);
				Message msg = Message.obtain(null,DownLoad.STARTDOWN, 0, 0);
				int key = file.hashCode();
				msg.arg1=key;
				Dmsg d = new Dmsg();
				d.setFileName(file);
				d.setPath(path);
				d.setUrl(url);
				msg.getData().putString(String.valueOf(key), d.toString());
				try {
					mService.send(msg);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onServiceDisconnected(ComponentName arg0) {
				System.out.println("下载停止:----------------------");
			}
		};
		Intent intent = new Intent(context,COService.class);
		context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
		return file;
	}
	
	
	public static void getDownMsg(Context context,final String key,final DownLoadMsg downLoadMsg){
	     ServiceConnection mRemoteConnection = new ServiceConnection(){
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				IMsgService iMsgService = IMsgService.Stub.asInterface(service);
				try {
					DownMsg dm = iMsgService.getPrice(key);
					downLoadMsg.downloadProgress(dm.getProgress());
					downLoadMsg.downloadSpeed(dm.getSpeed());
					downLoadMsg.fileSize(dm.getSize());
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onServiceDisconnected(ComponentName name) {
			}
	    };
		Intent intent = new Intent(context,COService.class);
		context.bindService(intent, mRemoteConnection, Context.BIND_AUTO_CREATE);
	}	
	public static void main(String[] args) {
		DownLoad.DownLoadUrl("http://pl.youku.com/playlist/m3u8?ts=1419243787&keyframe=1&vid=XODUyNjQxNTY0&sid=242243421404030c29cc7&token=9166&oip=1883341046&type=hd2&did=3151cdbf1449478fad97c27cd5fa755b2fff49fa&ctype=30&ev=1&ep=zpF6YV%2BFKfQ2Bs%2F5mOSC86SLxVjACengXVj9tC617Tl8gEfral6hs46SUelx4PRa", "D:\\video",System.currentTimeMillis()+"");
	}

}
