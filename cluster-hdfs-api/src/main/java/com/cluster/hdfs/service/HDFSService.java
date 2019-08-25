package com.cluster.hdfs.service;

import java.io.OutputStream;
import java.util.List;



public interface HDFSService {
	/**
	 * 上传本地文件到文件系统
	 * @param src 客户端文件
	 * @param fileName 文件名
	 * @param dst 服务器文件夹
	 */
	boolean uploadLocalFile2HDFS(String fileName,String src, String dst);
	/**
	 * 上传本地文件到文件系统
	 * @param inputStream 流式上传
	 * @param fileName 文件名
	 * @param dst 服务器文件夹
	 */
	boolean uploadLocalFileInputStream2HDFS(byte[] in,long size,String fileName, String dst);

	/**
	 * 
     * HDFS 文件下载
     * @param localFName  本地文件路径
     * @param hdfsFName  HDFS 文件路径+文件名
	 */
	void downFileFromHDFS(String localFName, String hdfsFName);
	/**
	 * 
     * HDFS 多文件下载
     * @param localFName  本地文件路径
     * @param hdfsFName  HDFS 文件路径+文件名
	 */
	void downFileFromHDFS(List<String> hdfsFName,OutputStream out);
	/**
	 * 创建新文件，并写入
	 * @param toCreateFilePath  文件夹地址+文件名
	 * @param content	文件内容
	 */
	boolean createNewHDFSFile(String toCreateFilePath, String content);
	/**
	 * 删除文件
	 * @param dst 文件夹+文件名
	 * @return
	 */
	boolean deleteHDFSFile(String dst);
	/**
	 * 读取单文件
	 * @param dst 文件夹+文件名
	 * @return
	 */
	byte[] readHDFSFile(String dst);
	/**
	 * 创建目录
	 * @param dir
	 */
	boolean mkdir(String dir);
	/**
	 * 删除目录
	 * @param dir
	 */
	boolean deleteDir(String dir);
	/**
	 * 读取某个目录下的所有文件
	 * @param dir
	 */
	 List<?> listAll(String dir);
}
