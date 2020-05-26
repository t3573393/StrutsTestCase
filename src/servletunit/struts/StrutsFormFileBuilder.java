package servletunit.struts;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.struts.upload.FormFile;

public class StrutsFormFileBuilder {
	
	static class MockFormFile implements FormFile {
		/**
	     * The filepath to the temporary file
	     */
	    protected String filePath;
	    
	    /**
	     * The content type of the file
	     */
	    protected String contentType;
	    
	    /**
	     * The size in bytes of the file
	     */
	    protected int fileSize;
	    
	    /**
	     * The data of the file
	     */
	    protected byte[] fileData;
	    
	    /**
	     * The name of the file
	     */
	    protected String fileName;   
	    
	    public MockFormFile(String filePath) {
	        this.filePath = filePath;
	    }
	    
	    public MockFormFile(String fileName, byte[] fileData) {
	    	this.fileName = fileName;
	    	this.fileData = fileData;
	    }
	    
	    /**
	     * Attempt to read the temporary file and get it's data in byte
	     * array form.  Tries to read the entire file (using a byte array
	     * the size of getFileSize()) at once, in one call to FileInputStream.read(byte[]).
	     * For buffered reading, see {@link #getFileData(int) getFileData(int)}.
	     * Note that this method can be dangerous, and that the size of a file
	     * can cause an OutOfMemoryError quite easily.  You should use 
	     * {@link #getInputStream() getInputStream} and do your own thing.
	     *
	     * @exception FileNotFoundException If the temp file no longer exists
	     * @exception IOException if there is some sort of IO problem.
	     * @see #getFileData(int)
	     */    
	    public byte[] getFileData() throws FileNotFoundException, IOException {
	    	if (fileData != null) {
	    		return fileData;
	    	}
	        
	        byte[] bytes = new byte[getFileSize()];
	        
	        FileInputStream fis = new FileInputStream(filePath);
	        fis.read(bytes);
	        fis.close();
	        return bytes;
	    }
	    
	    /**
	     * Attempts to read a file n bytes at a time, n being equal to "bufferSize".
	     * Note that this method can be dangerous, and that the size of a file
	     * can cause an OutOfMemoryError quite easily.  You should use 
	     * {@link #getInputStream() getInputStream} and do your own thing.
	     *
	     * @param bufferSize The size in bytes that are read from the file at a time
	     * @exception FileNotFoundException If the temp file no longer exists
	     */    
	    public byte[] getFileData(int bufferSize) throws FileNotFoundException, IOException {
	        if (fileData != null) {
	        	return fileData;
	        }
	    	
	        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
	        FileInputStream fis = new FileInputStream(filePath);
	        
	        int readLength  = 0;
	        int totalLength = 0;
	        int offset      = 0;
	        
	        byte[] bytes = new byte[bufferSize];
	        
	        while ((readLength = fis.read(bytes, offset, bufferSize)) != -1) {
	            
	            byteStream.write(bytes, offset, bufferSize);            
	            totalLength += readLength;
	            offset += readLength;
	        }
	        
	        bytes = byteStream.toByteArray();
	        
	        fis.close();
	        byteStream.close();
	        
	        return bytes;      
	    }
	    
	    
	    /**
	     * Delete the temporary file.
	     */
	    public void destroy() {
	        
	        File tempFile = new File(filePath);
	        
	        if (tempFile.exists()) {
	            tempFile.delete();
	        }    
	    }
	    
	    /**
	     * Get the temporary file path for this form file
	     * @return A filepath to the temporary file
	     */
	    public String getFilePath() {
	        return filePath;
	    }
	    
	    /**
	     * Set the file name
	     */
	    public void setFileName(String filename) {
	        this.fileName = filename;
	    }
	    
	    /**
	     * Set the file data
	     */
	    public void setFileData(byte[] fileData) {
	    	this.fileData = fileData;
	    }
	    
	    /**
	     * Set the content type
	     */
	    public void setContentType(String contentType) {
	        this.contentType = contentType;
	    }
	    
	    /**
	     * Set the file size
	     * @param fileSize The size of the file in bytes
	     */
	    public void setFileSize(int fileSize) {
	        this.fileSize = fileSize;
	    }
	    
	    /**
	     * Get the file name
	     */
	    public String getFileName() {
	    	if (filePath != null) {
	    		return Common.getName(filePath);
	    	}
	        return fileName;
	    }
	    
	    /**
	     * Get the content type
	     */
	    public String getContentType() {
	        return contentType;
	    }
	    
	    /**
	     * Get the file size
	     * @return The size of this file in bytes
	     */
	    public int getFileSize() {
	    	if (fileData != null) {
	        	return fileData.length;
	        }
	        return fileSize;
	    }
	    
	    /**
	     * Returns a FileInputStream to the file
	     */
	    public InputStream getInputStream() throws FileNotFoundException, IOException {
	        return new FileInputStream(filePath);
	    }
	}
	
	public static FormFile buildFormFile(String filePath, String contentType) {
		MockFormFile formFile = new MockFormFile(filePath);
		formFile.setContentType(contentType);
		return formFile;
	}
	
	public static FormFile buildFormFile(String fileName, String contentType, byte[] fileData) {
		MockFormFile formFile = new MockFormFile(fileName, fileData);
		formFile.setContentType(contentType);
		return formFile;
	}

}
