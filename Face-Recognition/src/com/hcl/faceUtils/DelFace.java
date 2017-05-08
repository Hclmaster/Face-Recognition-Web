package com.hcl.faceUtils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.youtu.Youtu;

/**
 * 进行删除个体(Person)操作
 * @author hclmaster
 *
 */
public class DelFace {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Youtu faceYoutu = new Youtu(FaceComparison.APP_ID, FaceComparison.SECRET_ID, FaceComparison.SECRET_KEY,Youtu.API_YOUTU_END_POINT);
    	JSONObject respose;
    	String person_id = "simple.jpg";
    	try {
			respose = faceYoutu.DelPerson(person_id);
			System.out.println(respose);
		} catch (KeyManagementException | NoSuchAlgorithmException | IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
