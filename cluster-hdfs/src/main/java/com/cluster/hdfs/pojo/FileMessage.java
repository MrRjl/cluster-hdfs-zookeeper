package com.cluster.hdfs.pojo;

import java.io.Serializable;



public class FileMessage implements Serializable{

	private static final long serialVersionUID = 2745487388732149942L;

	private String path;
	
	private String fileName;
	
	private long length;
	
	private boolean isdir;
	
	private short block_replication;
	
	private long blocksize;
	
	private String modification_times;
	
	private long access_time;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public boolean isIsdir() {
		return isdir;
	}

	public void setIsdir(boolean isdir) {
		this.isdir = isdir;
	}

	public short getBlock_replication() {
		return block_replication;
	}

	public void setBlock_replication(short block_replication) {
		this.block_replication = block_replication;
	}

	public long getBlocksize() {
		return blocksize;
	}

	public void setBlocksize(long blocksize) {
		this.blocksize = blocksize;
	}

	public String getModification_times() {
		return modification_times;
	}

	public void setModification_times(String modification_times) {
		this.modification_times = modification_times;
	}

	public long getAccess_time() {
		return access_time;
	}

	public void setAccess_time(long access_time) {
		this.access_time = access_time;
	}
	

}
