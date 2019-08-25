package com.cluster.common.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HDFS uploadUtils
 * 
 * @author Administrator
 *
 */
public class HDFSUtil {
	private static final Logger logger = LoggerFactory.getLogger(HDFSUtil.class);

	/**
	 * 上传本地文件到文件系统
	 * 
	 * @param hdfs 文件处理对象
	 * @param fileName 文件名称
	 * @param src 本地文件目录
	 * @param dst 服务器文件夹地址
	 * @return
	 */
	public static boolean uploadLocalFile2HDFS(FileSystem hdfs, String fileName, String src, String dst) {
		logger.info("上传本地文件到文件系统 : {} {} {}", new Date(), src, dst);
		boolean flag = false;
		try {
			hdfs.copyFromLocalFile(new Path(src), new Path(dst));
			flag = hdfs.exists(new Path(dst + "/" + fileName));
			// hdfs.close();
		} catch (IOException e) {
			logger.error("上传本地文件到文件系统异常 : {} {}", new Date(), e);
		} finally {
			// IOUtils.closeStream(hdfs);
		}
		return flag;
	}

	/**
	 * 上传本地文件到文件系统
	 * 
	 * @param hdfs 文件处理对象
	 * @param in 输入流
	 * @param fileName 文件名
	 * @param dst 服务器目录地址
	 * @return
	 */
	public static boolean uploadLocalFileInputStream2HDFS(FileSystem hdfs,byte[] in,long size, String fileName,
			String dst) {
		logger.info("上传本地文件到文件系统 : {} {} {}", new Date(), fileName, dst);
		String hdfsURI = dst + "/" + fileName;
		OutputStream out = null;
		boolean flag = false;
		try {
			// 创建文件系统对象
			out = hdfs.create(new Path(hdfsURI), new Progressable() { // 输出流
				@Override
				public void progress() {
					System.out.print(">");
				}
			});
			out.write(in);
			//IOUtils.copyBytes(in, out, size, true); // 连接两个流，形成通道，使输入流向输出流传输数据
			flag = hdfs.exists(new Path(hdfsURI));
			//hdfs.close();
		} catch (IOException e) {
			logger.error("上传本地文件到文件系统异常 : {} {}", new Date(), e);
		} finally {
			IOUtils.closeStream(out);
			//IOUtils.closeStream(in);
			// IOUtils.closeStream(hdfs);
		}
		return flag;
	}

	/**
	 * HDFS 文件下载
	 * 
	 * @param hdfs 文件处理对象
	 * @param localFName 本地文件路径
	 * @param hdfsFPath 文件路径+文件名
	 * @param size 缓冲区大小
	 */
	public static void downFileFromHDFS(FileSystem hdfs, String localFName, String hdfsFPath) {
		logger.info("文件下载 : {} {}{}", new Date(), localFName, hdfsFPath);
		FSDataInputStream outHDFS = null;
		OutputStream inLocal = null;
		try {
			// 创建文件系统对象
			outHDFS = hdfs.open(new Path(hdfsFPath)); // 从HDFS读出文件流
			inLocal = new FileOutputStream(localFName); // 写入本地文件
			IOUtils.copyBytes(outHDFS, inLocal, outHDFS.available(), true);
			// hdfs.close();
		} catch (IOException e) {
			logger.error("文件下载异常 : {} {}", new Date(), e);
		} finally {
			IOUtils.closeStream(outHDFS);
			IOUtils.closeStream(inLocal);
			// IOUtils.closeStream(hdfs);
		}

	}

	/**
	 * 多文件下载
	 * @param path hdfs路径
	 * @param hdfs 文件处理对象
	 * @param out 输出流
	 */
	public static void downFileFromHDFS(Path path, FileSystem hdfs, OutputStream out) {
		logger.info("多文件下载 : {} {} {}", new Date());
		FileStatus[] fileStatus;
		FSDataInputStream fsDataInputStream = null;
		try {
			fileStatus = hdfs.listStatus(path);
			for (int i = 0; i < fileStatus.length; i++) {
				if (fileStatus[i].isDirectory()) {
					downFileFromHDFS(fileStatus[i].getPath(), hdfs, out);
				} else {
					fsDataInputStream = hdfs.open(fileStatus[i].getPath());
					int readSize = 0;
					byte[] bufferByte = new byte[fsDataInputStream.available()];
					while ((readSize = fsDataInputStream.read(bufferByte)) > 0) {
						out.write(bufferByte, 0, readSize);
					}
					IOUtils.closeStream(fsDataInputStream);
				}
			}
		} catch (FileNotFoundException e) {
			logger.error("多文件下载 : {} {} {}", new Date(),"File Not Found Exception",e);
		} catch (IOException e) {
			logger.error("多文件下载 : {} {} {}", new Date(),"File download Exception",e);
		}

	}

	/**
	 * 创建新文件，并写入
	 * 
	 * @param hdfs 文件处理对象
	 * @param toCreateFilePath 文件夹地址+文件名
	 * @param content  文件内容
	 * @return
	 */
	public static boolean createNewHDFSFile(FileSystem hdfs, String toCreateFilePath, String content) {
		logger.info("创建新文件，并写入 : {} {} {}", new Date(), toCreateFilePath, content);
		FSDataOutputStream os = null;
		boolean flag = false;
		try {
			os = hdfs.create(new Path(toCreateFilePath));
			os.write(content.getBytes("UTF-8"));
			os.close();
			flag = hdfs.exists(new Path(toCreateFilePath));
			// hdfs.close();
		} catch (IOException e) {
			logger.error("创建新文件,并写入数据  异常 : {} {} ", new Date(), e);
		} finally {
			IOUtils.closeStream(os);
			// IOUtils.closeStream(hdfs);
		}
		return flag;
	}

	/**
	 * 删除文件
	 * 
	 * @param hdfs 文件处理对象
	 * @param dst 文件夹+文件名
	 * @return
	 */
	public static boolean deleteHDFSFile(FileSystem hdfs, String dst) {
		logger.info("删除文件 : {} {}", new Date(), dst);
		boolean flag = false;
		try {
			hdfs.delete(new Path(dst), true);
			flag = hdfs.exists(new Path(dst));
			// hdfs.close();
		} catch (IOException e) {
			logger.error("删除文件 : {} {}", new Date(), e);
			return false;
		} finally {
			// IOUtils.closeStream(hdfs);
		}
		return flag;
	}

	/**
	 * 读取文件
	 * 
	 * @param hdfs 文件处理对象
	 * @param dst 文件夹+文件名
	 * @return
	 */
	public static byte[] readHDFSFile(FileSystem hdfs, String dst) {
		logger.info("下载单文件 : {} {}", new Date(), dst);
		FSDataInputStream dataInputStream = null;
		try {
			Path path = new Path(dst);
			if (hdfs.exists(path)) {
				return inputStream2String(hdfs.open(new Path(dst)));
			} else {
				logger.error("下载单文件 : {} {}", new Date(), "读取失败");
				throw new Exception("the file is not found .");
			}
		} catch (Exception e) {
			logger.error("下载文件异常 : {} {}", new Date(), e);
		} finally {
			IOUtils.closeStream(dataInputStream);
			// IOUtils.closeStream(hdfs);
		}
		return null;
	}
	
	public static   byte[]  inputStream2String(InputStream  in)  throws  IOException  { 
	    StringBuffer  out  =  new  StringBuffer(); 
	    
	    byte[]  b  =  new  byte[4096]; 
	    for  (int  n;  (n  =  in.read(b))  !=  -1;)  { 
	            out.append(new  String(b,  0,  n)); 
	    } 
	      return out.toString().getBytes("utf-8"); 
	} 

	/**
	 * 创建目录
	 * 
	 * @param hdfs 文件处理对象
	 * @param dir 创建目录的路径
	 * @return
	 */
	public static boolean mkdir(FileSystem hdfs, String dir) {
		logger.info("创建目录 : {} {}", new Date(), dir);
		boolean flag = false;
		try {
			hdfs.mkdirs(new Path(dir));// mkdirs 表示可以创建多级目录,不带s只能创建单级目录
			flag = hdfs.exists(new Path(dir));
			// hdfs.close();
			logger.info("创建目录 : {} {} {}", "成功", new Date(), dir);
		} catch (IOException e) {
			logger.error("创建目录异常 : {} {}", new Date(), e);
		} finally {
			// IOUtils.closeStream(hdfs);
		}
		return flag;
	}

	/**
	 * 删除目录
	 * 
	 * @param hdfs 文件处理对象
	 * @param dir 文件夹路径
	 * @return
	 */
	public static boolean deleteDir(FileSystem hdfs, String dir) {
		logger.info("删除目录 : {} {}", new Date(), dir);
		boolean flag = false;
		try {
			hdfs.delete(new Path(dir), true);
			flag = hdfs.exists(new Path(dir));
			// hdfs.close();
		} catch (IOException e) {
			logger.error("删除目录异常 : {} {}", new Date(), e);
		} finally {
			// IOUtils.closeStream(hdfs);
		}
		return flag;
	}

	/**
	 * 读取某个目录下的所有文件
	 * 
	 * @param hdfs 文件处理对象
	 * @param dir 文件夹路径
	 * @return
	 */
	public static FileStatus[] listAll(FileSystem hdfs, String dir) {
		logger.info("读取某个目录下的所有文件 : {} {}", new Date(), dir);
		FileStatus[] fileStatus = null;
		try {
			fileStatus = hdfs.listStatus(new Path(dir));
			return fileStatus;
		} catch (IOException e) {
			logger.error("读取某个目录下的所有文件异常 : {} {}", new Date(), e);
		} finally {
			// IOUtils.closeStream(hdfs);
		}
		return fileStatus;
	}

}
