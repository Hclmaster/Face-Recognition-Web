package com.hcl.faceUtils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;

import com.youtu.Youtu;

/**
 * 该类能够查询出一个组Group中所有person列表
 * @author hclmaster
 *
 */
public class GetPersonIds {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Youtu faceYoutu = new Youtu(FaceComparison.APP_ID, FaceComparison.SECRET_ID, FaceComparison.SECRET_KEY,Youtu.API_YOUTU_END_POINT);
    	JSONObject respose;
    	try {
			respose = faceYoutu.GetPersonIds("Face-Recognition");
			System.out.println(respose);
		} catch (KeyManagementException | NoSuchAlgorithmException | IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
