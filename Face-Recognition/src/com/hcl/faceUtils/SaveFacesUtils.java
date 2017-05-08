package com.hcl.faceUtils;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.aliyun.oss.OSSClient;
import com.aliyun.samples.AliyunOSSClientUtil;
import com.aliyun.samples.OSSClientConstants;
import com.youtu.Youtu;

/**
 * 个体管理，进行每个person的一张图片的保存
 * @author hclmaster
 *
 */
public class SaveFacesUtils {
	private static String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
    private static String accessKeyId = OSSClientConstants.ACCESS_KEY_ID;
    private static String accessKeySecret = OSSClientConstants.ACCESS_KEY_SECRET;
    private static String bucketName = OSSClientConstants.BACKET_NAME;
    
    public void TrainPic(String filePath, String fileName) throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException{
    	OSSClient ossClient=AliyunOSSClientUtil.getOSSClient();
    	JSONObject respose;
    	File file = new File(filePath);
    	String resultUrl = null;
    	resultUrl = AliyunOSSClientUtil.uploadObject2OSS(ossClient, file, OSSClientConstants.BACKET_NAME, "");    
    	
    	Youtu faceYoutu = new Youtu(FaceComparison.APP_ID, FaceComparison.SECRET_ID, FaceComparison.SECRET_KEY,Youtu.API_YOUTU_END_POINT);
        List<String> group_ids = new ArrayList<String>();
        group_ids.add("Face-Recognition");
        respose = faceYoutu.NewPersonUrl(resultUrl, fileName, group_ids);
        System.out.println("成功被加入的group数量为: "+respose.getInt("suc_group")+
        		"成功被加入的face数量为: "+respose.getInt("suc_face")+" 相应的person_id为："+respose.getString("person_id"));
    }
    
    public static void main(String[] args) {
		SaveFacesUtils sfu = new SaveFacesUtils();
		String filePath = "C:\\Users\\hclmaster\\Desktop\\训练图集\\dcy6.jpg";
		String fileName = "dcy6.jpg";
		try {
			sfu.TrainPic(filePath, fileName);
		} catch (KeyManagementException | NoSuchAlgorithmException | IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
