package com.cluster.hdfs.service.impl;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.dubbo.config.annotation.Service;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;

import com.cluster.common.utils.DateUtils;
import com.cluster.common.utils.HDFSUtil;
import com.cluster.hdfs.pojo.FileMessage;
import com.cluster.hdfs.service.HDFSService;
@Service(version="1.0")
public class HDFSServiceImpl implements HDFSService{

	@Autowired
	private FileSystem fileSystem;
	
	@Override
	public boolean uploadLocalFile2HDFS(String fileName,String src, String dst) {
		// TODO Auto-generated method stub
		return HDFSUtil.uploadLocalFile2HDFS(fileSystem, fileName, src, dst);
	}

	@Override
	public boolean uploadLocalFileInputStream2HDFS(byte[] in, long size, String fileName, String dst) {
		// TODO Auto-generated method stub
		return HDFSUtil.uploadLocalFileInputStream2HDFS(fileSystem, in, size,fileName, dst);
	}

	@Override
	public void downFileFromHDFS(String localFName, String hdfsFName) {
		// TODO Auto-generated method stub
		 HDFSUtil.downFileFromHDFS(fileSystem, localFName, hdfsFName);;
	}

	
	@Override
	public void downFileFromHDFS(List<String> hdfsFName,OutputStream out) {
		for (String string : hdfsFName) {
			HDFSUtil.downFileFromHDFS(new Path(string), fileSystem, out);
		}
	}

	@Override
	public boolean createNewHDFSFile(String toCreateFilePath, String content) {
		// TODO Auto-generated method stub
		return HDFSUtil.createNewHDFSFile(fileSystem, toCreateFilePath, content);
	}

	@Override
	public boolean deleteHDFSFile(String dst) {
		// TODO Auto-generated method stub
		return HDFSUtil.deleteHDFSFile(fileSystem, dst);
	}

	@Override
	public byte[] readHDFSFile(String dst) {
		// TODO Auto-generated method stub
		return HDFSUtil.readHDFSFile(fileSystem, dst);
	}

	@Override
	public boolean mkdir(String dir) {
		// TODO Auto-generated method stub
		return HDFSUtil.mkdir(fileSystem, dir);
	}

	@Override
	public boolean deleteDir(String dir) {
		// TODO Auto-generated method stub
		return HDFSUtil.deleteDir(fileSystem, dir);
	}

	@Override
	public List<?> listAll(String dir) {
		
		FileStatus[] fileStatus=HDFSUtil.listAll(fileSystem, dir);
		List<FileMessage> list=new ArrayList<FileMessage>();
		FileMessage fileMessage=null;
		for (FileStatus fileStatus2 : fileStatus) {
			//System.out.println(fileStatus2.toString());
			fileMessage=new FileMessage();
			/*BeanUtils.copyProperties(fileStatus2, fileMessage);
			fileMessage.setPath(fileStatus2.getPath().toUri().getPath());
			fileMessage.setFileName(fileStatus2.getPath().getName());
			fileMessage.setIsdir(fileStatus2.isDirectory());
			fileMessage.setModification_times(DateUtils.getLongTODateFormat(fileStatus2.getModificationTime()));*/
			fileMessage.setPath(fileStatus2.getPath().toUri().getPath());
			fileMessage.setFileName(fileStatus2.getPath().getName());
			fileMessage.setLength(fileStatus2.getLen()/1024);
			fileMessage.setIsdir(fileStatus2.isDirectory());
			fileMessage.setBlock_replication(fileStatus2.getReplication());
			fileMessage.setBlocksize(fileStatus2.getBlockSize());
			fileMessage.setModification_times(DateUtils.getLongTODateFormat(fileStatus2.getModificationTime()));
			fileMessage.setAccess_time(fileStatus2.getAccessTime());
			list.add(fileMessage);
		}
		
		return list;
	}

}
