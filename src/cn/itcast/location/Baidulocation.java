package cn.itcast.location;

import android.content.Context;
import cn.itcast.utils.TimeUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class Baidulocation {
	private LocationClient mLocationClient = null;
	public LocationClientOption option;
	private BDLocationListener bd;
	public static String longitude="";
	public static String latitude="";
	public static String address="";
	public static String locType="";
	public static String gpstime="";
	public static String radius="";
	public void getlocation(Context context, boolean isgps) {
		
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.stop();
			mLocationClient = null;
		}
		mLocationClient = new LocationClient(context);
		option = new LocationClientOption();
		//68离线定位，即为基站定�?效果很差
		option.disableCache(true);
//		option.setOpenGps(true);
		if (isgps) {
			option.setOpenGps(true);
		} else {
			option.setOpenGps(false);
		}
		//百度坐标�?
		option.setCoorType("bd09ll");
		//all 表示返回地址信息
		option.setAddrType("all");
		option.setProdName("chianwayLocation");  
		//网络 优先，对获取位置有好�?gps 第一次比较慢 在室内获取不�?
		option.setPriority(LocationClientOption.GpsFirst);
		option.setScanSpan(20*60*1000);
		option.disableCache(true);
		
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		//位置的坐标，精度半径等信�?
		bd = new BDLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation location) {
				if (location == null)
					return;
					longitude = Double.toString(location.getLongitude());
					latitude = Double.toString(location.getLatitude());
					locType=location.getLocType()+"";
//					gpstime=location.getTime();
					gpstime = TimeUtils.getTime();
					if(location.hasRadius()){
						radius=location.getRadius()+"";
					}else{
						radius="没有误差";
					}
					if (location.getLatitude() == 0) {
					}
					if (location.getAddrStr() == null) {
						address="暂时无法获取地址";
					} else {
						//只有使用网络定位的情况下，才能获取当前位置的反地理编码描述�?
						address=location.getAddrStr();
					}
			}

			public void onReceivePoi(BDLocation location) {
			}
		};
		mLocationClient.registerLocationListener(bd);
		mLocationClient.start();
		//当定位SDK从定位依据判定，位置和上�?��没发生变化，而且上一次定位结果可用时，则不会发起网络请求，�?是返回上�?��的定位结果�?
		int requestCode=mLocationClient.requestLocation();
		if(requestCode==0){
			
		}else{
//			gpstime="";
		}
		
	}

	public void stop() {
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.unRegisterLocationListener(bd);
			mLocationClient.stop();
//			mLocationClient = null;
		}
	}
}
