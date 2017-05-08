package com.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import com.hcl.faceUtils.PortraitModel;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class PortraitModelAction extends ActionSupport  {
	ActionContext actionContext = ActionContext.getContext();
	private static final long serialVersionUID = 1L;
	public PortraitModel portraitModel = new PortraitModel();
	public Map<String, Object> responseJson;
	
	public Map<String, Object> getResponseJson() {
		return responseJson;
	}

	public void setResponseJson(Map<String, Object> responseJson) {
		this.responseJson = responseJson;
	}
	
	private List<File> file;
	private List<String> fileFileName;
	private List<String> fileContentType;
	private List<String> dataUrl;
	
	public List<File> getFile() {
		return file;
	}

	public void setFile(List<File> file) {
		this.file = file;
	}

	public List<String> getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(List<String> fileFileName) {
		this.fileFileName = fileFileName;
	}

	public List<String> getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(List<String> fileContentType) {
		this.fileContentType = fileContentType;
	}

	public List<String> getDataUrl() {
		return dataUrl;
	}

	public void setDataUrl(List<String> dataUrl) {
		this.dataUrl = dataUrl;
	}
	
	public String getModel() throws Exception{
		String[] infor = new String[5];
		dataUrl = new ArrayList<String>();
		String imgpath = "upload/";
		for(int i=0;i<file.size();++i){
			InputStream is = new FileInputStream(file.get(i));
			String path = ServletActionContext.getServletContext().getRealPath("/");
			dataUrl.add(imgpath+this.getFileFileName().get(i));
			File destFile = new File(path+imgpath, this.getFileFileName().get(i));
			OutputStream os = new FileOutputStream(destFile);
			byte[] buffer = new byte[400];
			int length = 0;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
			String PicPath = path+imgpath+this.getFileFileName().get(i);
			System.out.println(PicPath);
			infor = portraitModel.PortraitModelResult(PicPath,this.getFileFileName().get(i));
			is.close();
			os.close();
		}
		System.out.println(infor);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", infor);
		this.setResponseJson(map);
		return "success";
	}
}
