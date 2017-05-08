package com.hcl.faceUtils;

import java.io.File;

import org.json.JSONObject;

import com.aliyun.oss.OSSClient;
import com.aliyun.samples.AliyunOSSClientUtil;
import com.aliyun.samples.OSSClientConstants;
import com.youtu.Youtu;

/**
 * 人脸识别工具类
 * @author hclmaster
 *
 */
public class FaceComparison {
	
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
	 * 调用腾讯优图人脸识别api进行人脸比对
	 * @param picUrl1
	 * @param picUrl2
	 * @return
	 */
	public String CompareResult(String picUrl1, String picUrl2){
		double similarity;
		int fail_flag;
		String info = null;
		try {
			// 优图初始化方式
			Youtu faceYoutu = new Youtu(APP_ID, SECRET_ID, SECRET_KEY,Youtu.API_YOUTU_END_POINT);
			JSONObject respose;
			respose = faceYoutu.FaceCompareUrl(picUrl1, picUrl2);
			String errMsg = respose.getString("errormsg");
			
			// 如果有一张图片中没有人脸，那么就返回错误信息以及是哪一张图片出现了问题
			if("OK".equals(errMsg)!=true){
				System.out.println(respose);
				fail_flag = respose.getInt("fail_flag");
				info = "Compare Error! Error Picture is "+fail_flag;
			}
			else{
				// 如果成功的话，那么就返回两张图片的相似度(字符串类型)
				similarity = respose.getDouble("similarity");
				info = "相似度："+similarity+"";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}
	
	// 测试测试！
	/*public static void main(String[] args) {
		FaceComparison f = new FaceComparison();
		//f.UploadPicture("C:\\Users\\hclmaster\\Desktop\\simple.jpg");
		
		String result = f.CompareResult("http://face--recognition.oss-cn-shanghai.aliyuncs.com/3-1.jpg", "http://face--recognition.oss-cn-shanghai.aliyuncs.com/obama.jpeg");
		System.out.println(result);
	}*/
}
