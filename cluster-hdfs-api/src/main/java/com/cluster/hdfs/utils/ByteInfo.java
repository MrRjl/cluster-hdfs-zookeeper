package com.cluster.hdfs.utils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

public class ByteInfo {
	
	public static  byte[]  inputStream2String  (InputStream  in)  throws  IOException  { 
	    StringBuffer  out  =  new  StringBuffer(); 
	    byte[]  b  =  new  byte[4096]; 
	    for  (int  n;  (n  =  in.read(b))  !=  -1;)  { 
	            out.append(new  String(b,  0,  n)); 
	    } 
	      return out.toString().getBytes("utf-8"); 
	} 
	public static void downloadfileByte(HttpServletResponse response,String fileName,byte[] bytes) throws IOException{
        response.reset();
        response.addHeader("Content-Disposition", "attachment;filename="+ fileName);
        OutputStream stream = new BufferedOutputStream( response.getOutputStream());
        response.setCharacterEncoding("utf-8");  
        response.setHeader("content-type", "application/x-download;charset=utf-8");  
        stream.write(bytes);
        stream.flush();
        stream.close();
	}
}
