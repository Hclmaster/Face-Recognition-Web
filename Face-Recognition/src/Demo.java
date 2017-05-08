
import org.json.JSONArray;
import org.json.JSONObject;
import com.youtu.*;

public class Demo {

	// appid, secretid secretkey请到http://open.youtu.qq.com/获取
	// 请正确填写把下面的APP_ID、SECRET_ID和SECRET_KEY
	public static final String APP_ID = "10081316";
	public static final String SECRET_ID = "AKIDlqLgq4VynWnwufxRYqDsQqLhr8GEqQel";
	public static final String SECRET_KEY = "Ed5MXcWHrGoG3hAoS0NZcT9d0WRDRlt3";
	public static final String USER_ID = "690060189";

	public static void main(String[] args) {

		boolean isGlass;	// 判断它是否戴眼镜
		int gender;		// 性别
		int pointX;		// 人脸框左上角X坐标
		int pointY;		// 人脸框左上角Y坐标
		double width;		//人脸框宽度
		double height;		// 人脸框高度
		
		try {
			// 优图初始化方式
			Youtu faceYoutu = new Youtu(APP_ID, SECRET_ID, SECRET_KEY,Youtu.API_YOUTU_END_POINT);
			JSONObject respose;
			//respose= faceYoutu.FaceCompareUrl("http://open.youtu.qq.com/content/img/slide-1.jpg","http://open.youtu.qq.com/content/img/slide-1.jpg");
			//respose = faceYoutu.DetectFace("test.jpg",1);
			respose = faceYoutu.DetectFaceUrl("http://face--recognition.oss-cn-shanghai.aliyuncs.com/3-1.jpg",1);
			//get respose
			System.out.println(respose);
			
			String errMsg = respose.getString("errormsg");
			if("OK".equals(errMsg)!=true){
				System.out.println("出现错误了！");
			}
			// 这里获取到的是照片中总共有几个人头，所以是一个数组
			JSONArray jsonArray = respose.getJSONArray("face");
			for(int i=0; i<jsonArray.length(); i++){
				JSONObject PersonData = jsonArray.getJSONObject(i);
				isGlass = PersonData.getBoolean("glass");
				gender = PersonData.getInt("gender");
				pointX = PersonData.getInt("x");
				pointY = PersonData.getInt("y");
				width = PersonData.getDouble("width");
				height = PersonData.getDouble("height");
				System.out.println("isGlass: "+isGlass+"gender: "+gender+"pointX: "+pointX
						+"pointY: "+pointY+"width: "+width+"height: "+height);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
