package com.hcl.faceUtils;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;

import com.aliyun.oss.OSSClient;
import com.aliyun.samples.AliyunOSSClientUtil;
import com.aliyun.samples.OSSClientConstants;
import com.youtu.Youtu;

/**
 * 人像建模工具类
 * 上传一张图片，然后将图片进行裁剪保存到数据库中去
 * @author hclmaster
 *
 */
public class PortraitModel {
	
	public static final String APP_ID = "10081316";
	public static final String SECRET_ID = "AKIDlqLgq4VynWnwufxRYqDsQqLhr8GEqQel";
	public static final String SECRET_KEY = "Ed5MXcWHrGoG3hAoS0NZcT9d0WRDRlt3";
	public static final String USER_ID = "690060189";
	
	/**
	 * 返回图片上传后的url，如果成功则为url链接，失败为null
	 * @param filePath
	 * @return
	 */
	public String UploadPicture(String filePath){
		//初始化OSSClient  
        OSSClient ossClient=AliyunOSSClientUtil.getOSSClient();
        File file = new File(filePath);
        String resultUrl = null;
        resultUrl = AliyunOSSClientUtil.uploadObject2OSS(ossClient, file, OSSClientConstants.BACKET_NAME, "");    
        System.out.println("上传后的图片URL为: " + resultUrl); 
        return resultUrl;
	}
	
	/**
	 * 调用腾讯优图人脸检测api来取出相应的人脸信息并进行建模
	 */
	public String[] PortraitModelResult(String filePath, String fileName){
		String picUrl = UploadPicture(filePath);
		String[] res = new String[5];
		String info = null;
		
		int image_height;	// 图片的高度
		int image_width;	// 图片的宽度
		boolean isGlass;	// 判断它是否戴眼镜
		int gender;		// 性别
		int pointX;		// 人脸框左上角X坐标
		int pointY;		// 人脸框左上角Y坐标
		double width;		//人脸框宽度
		double height;		// 人脸框高度
		int age;		// 人脸的估计年龄
		
		try {
			// 优图初始化方式
			Youtu faceYoutu = new Youtu(APP_ID, SECRET_ID, SECRET_KEY,Youtu.API_YOUTU_END_POINT);
			JSONObject respose;
			respose = faceYoutu.DetectFaceUrl(picUrl, 0);
			String errMsg = respose.getString("errormsg");
			
			// 如果该图片中没有人脸，那么就返回错误信息！
			if("OK".equals(errMsg)!=true){
				System.out.println(respose);
				info = "Detect Face Error!";
			}
			else{
				// 如果成功的话，那么就返回这张图片中人脸的相关信息
				image_width = respose.getInt("image_width");
				image_height = respose.getInt("image_height");
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
					age = PersonData.getInt("age");
					System.out.println("isGlass: "+isGlass+"; gender: "+gender+"; pointX: "+pointX
							+"; pointY: "+pointY+"; width: "+width+"; height: "+height+"; age: "+age);
					if(gender > 50){
						info = "性别为: 男;"+"\n";
					}else{
						info = "性别为: 女;"+"\n";
					}
					info += "预计年龄为: "+age+";\n";
					if(isGlass == true){
						info += "有无佩戴眼镜: 有佩戴眼镜;"+"\n";
					}else{
						info += "有无佩戴眼镜: 无佩戴眼镜;"+"\n";
					}
					res[0] = info;
					String cropImageUrl = ImageCropUtils.CropImages(fileName, pointX, pointY, width, height);
					if(cropImageUrl != null){
						res[1] = cropImageUrl;
						//info += "建模后图像的url为: "+cropImageUrl+"\n";
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * 测试测试！！！
	 * @param args
	 */
	/*public static void main(String[] args) {
		PortraitModel pm = new PortraitModel();
		// 需要传过来文件地址以及文件名
		String info = pm.PortraitModelResult("F:\\图片\\obama.jpeg","obama.jpeg");
		System.out.println(info);
	}*/
}
