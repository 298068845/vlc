package com.nmbb.vlc.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.videolan.libvlc.EventHandler;
import org.videolan.libvlc.IVideoPlayer;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.LibVlcException;
import org.videolan.libvlc.Media;
import org.videolan.vlc.util.VLCInstance;

import android.R.integer;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.nmbb.vlc.R;

public class VlcVideoActivity extends Activity implements SurfaceHolder.Callback, IVideoPlayer {

	private final static String TAG = "[VlcVideoActivity]";
	private Callback callback;
	private SurfaceView mSurfaceView,mSurfaceView2;
	private LibVLC mMediaPlayer;
	private SurfaceHolder mSurfaceHolder,mSurfaceHolder2;
    private View mLoadingView;
	private int mVideoHeight;
	private int mVideoWidth;
	private int mVideoVisibleHeight;
	private int mVideoVisibleWidth;
	private int mSarNum;
	private int mSarDen;
    private GridView gridView;
    private int flag=0;
    private int curposition=5;
    private float curX,curY;
    private float midX,midY;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_vlc);
		callback = this;
        gridView = (GridView) this.findViewById(R.id.gridView);
		mSurfaceView = (SurfaceView) findViewById(R.id.video);
		mSurfaceView2 = (SurfaceView) findViewById(R.id.video2);
		try {
			mMediaPlayer = VLCInstance.getLibVlcInstance();
		} catch (LibVlcException e) {
			e.printStackTrace();
		}

		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.setFormat(PixelFormat.RGBX_8888);
		mSurfaceHolder.addCallback(this);
		mSurfaceView.setZOrderOnTop(true);
		mSurfaceView2.setZOrderOnTop(true);

		mMediaPlayer.eventVideoPlayerActivityCreated(true);
		
		EventHandler em = EventHandler.getInstance();
		em.addHandler(mVlcHandler);
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		mSurfaceView.setKeepScreenOn(true);
		mSurfaceView2.setKeepScreenOn(true);

		//		mMediaPlayer.setMediaList();
				mMediaPlayer.getMediaList().add(new Media(mMediaPlayer, "http://live.3gv.ifeng.com/zixun.m3u8"), false);
				mMediaPlayer.getMediaList().add(new Media(mMediaPlayer, "http://vshare.ys7.com:80/hcnp/504143029_2_2_1_0_183.136.184.7_6500.m3u8"), false);

				mMediaPlayer.playIndex(0);


		//mMediaPlayer.playMRL("http://live.3gv.ifeng.com/zixun.m3u8");
//		mSurfaceView.setOnTouchListener(new OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				if(flag==0){
//				Toast.makeText(getApplicationContext(), "点击放大" , 0).show();
//				flag=1;
//				}else{
//					Toast.makeText(getApplicationContext(), "点击缩小" , 0).show();
//				flag=0;
//				}				return false;
//			}
//		});
		List<Map<String,Object>> item = getData();
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, item, R.layout.gridviewitem, new String[] { "itemImage", "itemName" }, new int[] { R.id.itemImage, R.id.itemName });
		simpleAdapter.isEnabled(5);
		gridView.setAdapter(simpleAdapter);
        gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2,
					long arg3) {
				mSurfaceHolder = mSurfaceView2.getHolder();
				mSurfaceHolder.setFormat(PixelFormat.RGBX_8888);
				mSurfaceHolder.addCallback(callback);
				mMediaPlayer.playMRL("http://live.3gv.ifeng.com/zixun.m3u8");

//				int index = arg2 + 1;// 
//
//
//				if(index==curposition)
//				{
//					if(flag==0){
//					mCurrentSize=SURFACE_BEST_FIT;flag=1;
//					curX=v.getX()+50;curY=v.getY()-10;
//					RelativeLayout.LayoutParams lp = (LayoutParams) mSurfaceView.getLayoutParams();
//					lp.width = 910;
//					lp.height =560;
//					mSurfaceView.setLayoutParams(lp);
//					mSurfaceView.setX(338);
//					mSurfaceView.setY(150);					
//								}
//					else{
//					mCurrentSize=SURFACE_SMALL;flag=0;
//					RelativeLayout.LayoutParams lp = (LayoutParams) mSurfaceView.getLayoutParams();
//					lp.width = 170;
//					lp.height =170;
//					mSurfaceView.setLayoutParams(lp);
//					mSurfaceView.setX(curX-340);
//					mSurfaceView.setY(curY-170);
//					}
//				}// Toast.makeText(getApplicationContext(), "正在播放摄像头" + index, 0).show();
//				else if(flag!=1){
//					curposition=index;
//					if(curposition%2==0){
//						mMediaPlayer.stop();
//						mMediaPlayer.playIndex(1);
//	              				}
//					else{
//						mMediaPlayer.stop();
//						mMediaPlayer.playIndex(0);
//					}
//						
//
//				}
//				else{
//					mCurrentSize=SURFACE_SMALL;flag=0;
//					changeSurfaceSize();
//					mSurfaceView.setX(curX-340);
//					mSurfaceView.setY(curY-170);
//				}
			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();

		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mSurfaceView.setKeepScreenOn(false);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mMediaPlayer != null) {
			mMediaPlayer.eventVideoPlayerActivityCreated(false);
			EventHandler em = EventHandler.getInstance();
			em.removeHandler(mVlcHandler);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		setSurfaceSize(mVideoWidth, mVideoHeight, mVideoVisibleWidth, mVideoVisibleHeight, mSarNum, mSarDen);
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (mMediaPlayer != null) {
			mSurfaceHolder = holder;
			midX=mSurfaceView.getX();
			midY=mSurfaceView.getY();
			mMediaPlayer.attachSurface(holder.getSurface(), this);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		mSurfaceHolder = holder;
		if (mMediaPlayer != null) {
			mMediaPlayer.attachSurface(holder.getSurface(), this);//, width, height
		}
		if (width > 0) {
			mVideoHeight = height;
			mVideoWidth = width;
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mMediaPlayer != null) {
			mMediaPlayer.detachSurface();
		}
	}

	@Override
	public void setSurfaceSize(int width, int height, int visible_width, int visible_height, int sar_num, int sar_den) {
		mVideoHeight = height;
		mVideoWidth = width;
		mVideoVisibleHeight = visible_height;
		mVideoVisibleWidth = visible_width;
		mSarNum = sar_num;
		mSarDen = sar_den;
		mHandler.removeMessages(HANDLER_SURFACE_SIZE);
		mHandler.sendEmptyMessage(HANDLER_SURFACE_SIZE);
	}

	private static final int HANDLER_BUFFER_START = 1;
	private static final int HANDLER_BUFFER_END = 2;
	private static final int HANDLER_SURFACE_SIZE = 3;

	private static final int SURFACE_BEST_FIT = 0;
	private static final int SURFACE_FIT_HORIZONTAL = 1;
	private static final int SURFACE_FIT_VERTICAL = 2;
	private static final int SURFACE_FILL = 3;
	private static final int SURFACE_SMALL = 7;

	private static final int SURFACE_16_9 = 4;
	private static final int SURFACE_4_3 = 5;
	private static final int SURFACE_ORIGINAL = 6;
	
	private int mCurrentSize = SURFACE_SMALL;

	private Handler mVlcHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg == null || msg.getData() == null)
				return;

			switch (msg.getData().getInt("event")) {
			case EventHandler.MediaPlayerTimeChanged:
				float time = mMediaPlayer.getPosition();
				long length = mMediaPlayer.getLength();
				long p = mMediaPlayer.getTime();
				break;
			case EventHandler.MediaPlayerPositionChanged:
				break;
			case EventHandler.MediaPlayerPlaying:
				mHandler.removeMessages(HANDLER_BUFFER_END);
				mHandler.sendEmptyMessage(HANDLER_BUFFER_END);
				break;
			case EventHandler.MediaPlayerBuffering:
				break;
			case EventHandler.MediaPlayerLengthChanged:
				break;
			case EventHandler.MediaPlayerEndReached:
				//播放完成
				break;
			}

		}
	};

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLER_BUFFER_START:
                //showLoading();
				break;
			case HANDLER_BUFFER_END:
               // hideLoading();
				break;
			case HANDLER_SURFACE_SIZE:
				changeSurfaceSize();
				break;
			}
		}
	};

	private void showLoading() {
        mLoadingView.setVisibility(View.VISIBLE);
	}

	private void hideLoading() {
        mLoadingView.setVisibility(View.GONE);
	}

	private void changeSurfaceSize() {
		// get screen size
		int dw = getWindowManager().getDefaultDisplay().getWidth();
		int dh = getWindowManager().getDefaultDisplay().getHeight();

		// calculate aspect ratio
		double ar = (double) mVideoWidth / (double) mVideoHeight;
		// calculate display aspect ratio
		double dar = (double) dw / (double) dh;

		switch (mCurrentSize) {
		case SURFACE_BEST_FIT:
			if (dar < ar)
				dh = (int) (dw / ar);
			else
				dw = (int) (dh * ar);
			break;
		case SURFACE_FIT_HORIZONTAL:
			dh = (int) (dw / ar);
			break;
		case SURFACE_FIT_VERTICAL:
			dw = (int) (dh * ar);
			break;
		case SURFACE_SMALL:
			dw = 170;
			dh = 170;
			break;
		case SURFACE_FILL:
			break;
		case SURFACE_16_9:
			ar = 16.0 / 9.0;
			if (dar < ar)
				dh = (int) (dw / ar);
			else
				dw = (int) (dh * ar);
			break;
		case SURFACE_4_3:
			ar = 4.0 / 3.0;
			if (dar < ar)
				dh = (int) (dw / ar);
			else
				dw = (int) (dh * ar);
			break;
		case SURFACE_ORIGINAL:
			dh = mVideoHeight;
			dw = mVideoWidth;
			break;
		}

		mSurfaceHolder.setFixedSize(mVideoWidth, mVideoHeight);
		//mSurfaceHolder2.setFixedSize(mVideoWidth, mVideoHeight);

		ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();
		lp.width = dw;
		lp.height = dh;
		mSurfaceView.setLayoutParams(lp);
		mSurfaceView.invalidate();
		ViewGroup.LayoutParams lp2 = mSurfaceView2.getLayoutParams();
		lp2.width = dw;
		lp2.height = dh;
		mSurfaceView2.setLayoutParams(lp2);
		mSurfaceView2.invalidate();
	}
	
	private List<Map<String, Object>> getData() {
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();

        int[] listImg = new int[] { R.drawable.video, R.drawable.video, R.drawable.video, R.drawable.video, R.drawable.video, R.drawable.video, R.drawable.video, R.drawable.video, R.drawable.video };
        String[] listName = new String[] { "摄像头1", "摄像头2", "摄像头3", "摄像头4", "摄像头5", "摄像头6", "摄像头7", "摄像头8", "摄像头9"};
        for (int i = 0; i < listImg.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("itemImage", listImg[i]);
            item.put("itemName", listName[i]);
            items.add(item);
        }
        return items;

    }
}
