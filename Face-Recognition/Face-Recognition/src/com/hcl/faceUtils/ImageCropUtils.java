package com.hcl.faceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.samples.AliyunOSSClientUtil;
import com.aliyun.samples.OSSClientConstants;
import com.youtu.Youtu;

/**
 * 断点续传下载用法示例
 *
 */
public class ImageCropUtils {
    
    private static String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
    private static String accessKeyId = OSSClientConstants.ACCESS_KEY_ID;
    private static String accessKeySecret = OSSClientConstants.ACCESS_KEY_SECRET;
    private static String bucketName = OSSClientConstants.BACKET_NAME;

    /**
     * 进行图片裁剪的功能类
     * @param fileName
     * @return
     */
    public static String CropImages(String fileName,int pointX,int pointY,double width,double height) {

    	String resultUrl = null;
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        JSONObject respose;
        
        try {
        	String style = null;
        	GetObjectRequest request;
        	
        	int ww;
        	ww = (int)width;
        	int hh;
        	hh = (int)height;
        	
            // 裁剪
            style = "image/crop,w_"+ww+",h_"+hh+",x_"+pointX+",y_"+pointY; 
            request = new GetObjectRequest(bucketName, fileName);
            request.setProcess(style);
            
            // 先将裁剪完的图片放到本地，然后再传到网上去
            String filePath = "F:\\"+fileName;
            ossClient.getObject(request, new File(filePath));
            // 以下是将裁剪完的图片放到网上去，获取到新的url
            ossClient = AliyunOSSClientUtil.getOSSClient();
            File file = new File(filePath);
            resultUrl = AliyunOSSClientUtil.uploadObject2OSS(ossClient, file, OSSClientConstants.BACKET_NAME, "");    
            System.out.println("-->裁剪后的图片URL为: " + resultUrl);
            
            Youtu faceYoutu = new Youtu(FaceComparison.APP_ID, FaceComparison.SECRET_ID, FaceComparison.SECRET_KEY,Youtu.API_YOUTU_END_POINT);
            List<String> group_ids = new ArrayList<String>();
            group_ids.add("Face-Recognition");
            respose = faceYoutu.NewPersonUrl(resultUrl, fileName, group_ids);
            System.out.println("成功被加入的group数量为: "+respose.getInt("suc_group")+
            		"成功被加入的face数量为: "+respose.getInt("suc_face")+" 相应的person_id为："+respose.getString("person_id"));
            
            return resultUrl;
            
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message: " + oe.getErrorCode());
            System.out.println("Error Code:       " + oe.getErrorCode());
            System.out.println("Request ID:      " + oe.getRequestId());
            System.out.println("Host ID:           " + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ce.getMessage());
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
		return resultUrl;
    }
}
