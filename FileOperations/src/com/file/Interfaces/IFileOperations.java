package com.file.Interfaces;

import java.io.IOException;
import java.util.List;

import net.lingala.zip4j.exception.ZipException;

import com.dropbox.core.DbxException;

public interface IFileOperations {

	/**
	 * 
	 * @param fromPath folder path or file absolute path
	 * @param toPath upload folder path  
	 * @return true if all files uploaded successfully otherwise false
	 * @throws IOException 
	 * @throws DbxException 
	 */
	public boolean upload(String fromPath, String toPath) throws IOException, DbxException;
	
	/**
	 * 
	 * @param fromPath folder path or file absolute path
	 * @param toPath download folder path  
	 * @return filepath of downloaded file
	 * @throws IOException 
	 */
	public String download(String fromPath, String toPath) throws Exception;
	
	/**
	 * 
	 * @param searchStr Search String 
	 * @param searchFolder Search folder path
	 * @return List of folder paths of matches
	 */
	public List<String> search(String searchStr, String searchFolder);
	
	/**
	 * 
	 * @param fromPath folder path or file absolute path
	 * @param toPath folder path where zip will be created
	 * @return filepath of zipped file
	 * @throws IOException 
	 * @throws ZipException 
	 */
	public String zip(String fromPath, String toPath) throws IOException, ZipException;
	
	/**
	 * 
	 * @param filePath path of file which needs to be opened
	 * @return true if file is opened otherwise false
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
    public boolean openFile(String filePath) throws IOException, InterruptedException;
    
    /**
     * 
     * @param filePath folder path or file absolute path
     * @return true if successfully deleted
     */
    public boolean deleteFile(String filePath);

}
