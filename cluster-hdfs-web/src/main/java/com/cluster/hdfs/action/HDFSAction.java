package com.cluster.hdfs.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cluster.common.vo.ResultInfo;
import com.cluster.hdfs.service.HDFSService;
import com.cluster.hdfs.utils.ByteInfo;

@Controller
public class HDFSAction {
	@Reference(version = "1.0")
	private HDFSService service;

	/**
	 * 上传本地文件到文件系统
	 * 
	 * @param inputStream
	 *            流式上传
	 * @param fileName
	 *            文件名
	 * @param dst
	 *            服务器文件夹
	 * @throws IOException
	 */
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	@ResponseBody
	public Object uploadLocalFileInputStream2HDFS( MultipartFile file,String dst ) throws IOException {
		if(file==null){
			return ResultInfo.error("file 为空");
		}
		if (!file.isEmpty()) {
			String fileName = file.getOriginalFilename();
			if(service.uploadLocalFileInputStream2HDFS(ByteInfo.inputStream2String(file.getInputStream()),file.getSize(), fileName, dst)){
				
				return ResultInfo.success("上传成功");
			}else{
				return ResultInfo.error("上传失败");
			}
		} else {
			return ResultInfo.error("file 为空");
		}

	}
	
	/**
	 * 创建新文件，并写入
	 * 
	 * @param toCreateFilePath
	 *            文件夹地址+文件名
	 * @param content
	 *            文件内容
	 */
	@RequestMapping(value = "/uploadFile", method = RequestMethod.PUT)
	@ResponseBody
	public Object createNewHDFSFile(String path, String fileName, String content) {
		if (!StringUtils.isEmpty(path)&&!StringUtils.isEmpty(fileName)&&!StringUtils.isEmpty(content))
			return ResultInfo.success(service.createNewHDFSFile(path + "/" + fileName, content));
		else
			return ResultInfo.error(service.createNewHDFSFile(path + "/" + fileName, content));
	}

	/**
	 * 删除文件
	 * 
	 * @param dst
	 *            文件夹+文件名
	 * @return
	 */
	@RequestMapping(value = "/uploadFile", method = RequestMethod.DELETE)
	@ResponseBody
	public Object deleteHDFSFile(String dst) {
		if (!StringUtils.isEmpty(dst))
			return ResultInfo.success(service.deleteHDFSFile(dst));
		else
			return ResultInfo.error(service.deleteHDFSFile(dst));
	}

	/**
	 * 单文件下载
	 * 
	 * @param dst
	 *            文件夹+文件名
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
	public void readHDFSFile(String dst, String fileName, HttpServletResponse response) throws IOException {
		if (!StringUtils.isEmpty(dst) && !StringUtils.isEmpty(fileName)) {
			byte[] buffer = service.readHDFSFile(dst);
			try {
				ByteInfo.downloadfileByte(response, new String(fileName.getBytes("gbk"), "iso8859-1"), buffer);
			} catch (Exception e) {
				System.out.println("发生了错误!");
			} 
		} 

	}

	/**
	 * 
	 * HDFS 多文件下载
	 * 
	 * @param hdfsPath
	 *            HDFS 文件路径或文件路+文件名
	 * @throws IOException
	 */
	@RequestMapping(value = "/download", method = RequestMethod.OPTIONS)
	public Object downFileFromHDFS(String hdfsPath, HttpServletResponse response) throws IOException {
		response.setHeader("content-type", "application/octet-stream");
		response.setContentType("application/octet-stream");
		if (!StringUtils.isEmpty(hdfsPath)) {
			service.downFileFromHDFS(JSON.parseObject(hdfsPath, new TypeReference<List<String>>() {
			}), response.getOutputStream());
			return ResultInfo.success();
		} else {
			return ResultInfo.error();
		}

	}

	/**
	 * 创建目录
	 * 
	 * @param dir
	 */
	@RequestMapping(value = "/cretedirectory", method = RequestMethod.POST)
	@ResponseBody
	public Object mkdir( String dir) {
		if (!StringUtils.isEmpty(dir))
			return ResultInfo.success(service.mkdir(dir));
		else
			return ResultInfo.error();
	}

	/**
	 * 删除目录
	 * 
	 * @param dir
	 */
	@RequestMapping(value = "/delectdirectory")
	@ResponseBody
	public Object deleteDir(String dir) {
		
		if (!StringUtils.isEmpty(dir))
			return ResultInfo.success(service.deleteDir(dir));
		else
			return ResultInfo.error();
	}

	/**
	 * 读取某个目录下的所有文件
	 * 
	 * @param dir
	 */
	@RequestMapping(value = "/directory", method = RequestMethod.GET)
	@ResponseBody
	public Object listAll(String dir) {
		if (!StringUtils.isEmpty(dir))
			return ResultInfo.success(service.listAll(dir));
		else
			return ResultInfo.error(new ArrayList<>());
	}
}
