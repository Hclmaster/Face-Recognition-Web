package com.aliyun.samples;

import java.io.File;
import java.io.IOException;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GetObjectRequest;

/**
 * 断点续传下载用法示例
 *
 */
public class ImageSample {
    
    private static String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
    private static String accessKeyId = OSSClientConstants.ACCESS_KEY_ID;
    private static String accessKeySecret = OSSClientConstants.ACCESS_KEY_SECRET;
    private static String bucketName = OSSClientConstants.BACKET_NAME;
    private static String key = "obama.jpeg";
    
    public static void main(String[] args) throws IOException {

        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        
        try {
        	String style = null;
        	GetObjectRequest request;
            
            // 裁剪
            style = "image/crop,w_609,h_480,x_203,y_111"; 
            request = new GetObjectRequest(bucketName, key);
            request.setProcess(style);
            
            // 先将裁剪完的图片放到本地，然后再传到网上去
            String filePath = "C:\\Users\\hclmaster\\Desktop\\example-crop.jpg";
            ossClient.getObject(request, new File(filePath));
            // 以下是将裁剪完的图片放到网上去，获取到新的url
            ossClient = AliyunOSSClientUtil.getOSSClient();
            File file = new File(filePath);
            String resultUrl = null;
            resultUrl = AliyunOSSClientUtil.uploadObject2OSS(ossClient, file, OSSClientConstants.BACKET_NAME, "");    
            System.out.println("-->裁剪后的图片URL为: " + resultUrl);
            
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
    }
}
