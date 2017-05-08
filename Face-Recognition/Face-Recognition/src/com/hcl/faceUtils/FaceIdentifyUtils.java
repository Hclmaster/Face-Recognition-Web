package com.hcl.faceUtils;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.samples.AliyunOSSClientUtil;
import com.aliyun.samples.OSSClientConstants;
import com.youtu.Youtu;

/**
 * 人脸识别类，返回在一个group中最相似的top5 person作为返回
 * @author hclmaster
 *
 */
public class FaceIdentifyUtils {
	private static String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
    private static String accessKeyId = OSSClientConstants.ACCESS_KEY_ID;
    private static String accessKeySecret = OSSClientConstants.ACCESS_KEY_SECRET;
    private static String bucketName = OSSClientConstants.BACKET_NAME;
    
    /**
	 * 返回图片上传后的url，如果成功则为url链接，失败为null
	 * @param filePath
	 * @return
	 */
	public static String UploadPicture(String filePath){
		//初始化OSSClient  
        OSSClient ossClient=AliyunOSSClientUtil.getOSSClient();
        File file = new File(filePath);
        String resultUrl = null;
        resultUrl = AliyunOSSClientUtil.uploadObject2OSS(ossClient, file, OSSClientConstants.BACKET_NAME, "");    
        return resultUrl;
	}
    
	/**
	 * 进行人脸识别操作，返回库中最相似的5张人脸
	 * @param filePath
	 */
    public static String[] FaceIdentify(String filePath){
    	String resultUrl = null;
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        JSONObject respose;
        String[] res = new String[11];
        
        resultUrl = UploadPicture(filePath);
        
        try {
        	String person_id = null;
        	double confidence;
        	Youtu faceYoutu = new Youtu(FaceComparison.APP_ID, FaceComparison.SECRET_ID, FaceComparison.SECRET_KEY,Youtu.API_YOUTU_END_POINT);
			respose = faceYoutu.FaceIdentifyUrl(resultUrl, "Face-Recognition");
			
			int errorcode = respose.getInt("errorcode");
			//System.out.print("errorcode 为："+errorcode);
			
			// 说明情况是正确的!
			if(errorcode == 0){
				JSONArray jsonArray = respose.getJSONArray("candidates");
				int tot = 0;
				// 总共会有5个人
				for(int i=0;i<jsonArray.length();i++){
					JSONObject PersonData = jsonArray.getJSONObject(i);
					person_id = PersonData.getString("person_id");
					confidence = PersonData.getDouble("confidence");
					res[tot++] = "http://face--recognition.oss-cn-shanghai.aliyuncs.com/"+person_id;	// 这个返回的图片链接
					res[tot++] = confidence+"";		// 这个返回的是图片的相似度
					//System.out.println("图片链接为："+"http://face--recognition.oss-cn-shanghai.aliyuncs.com/"+person_id
					//		+" 置信度为: "+confidence);
				}
			}
		} catch (KeyManagementException | NoSuchAlgorithmException | IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return res;
    }
    
    // 测试
    public static void main(String[] args) {
    	FaceIdentifyUtils.FaceIdentify("C:\\Users\\hclmaster\\Desktop\\lll.jpg");
	}
}
