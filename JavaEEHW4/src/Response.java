import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;


public class Response implements ServletResponse {

	private static final int BUFFER_SIZE = 1024;  
	Request request;  
	OutputStream output;  
	PrintWriter writer; 
	
	public Response(OutputStream output) {  
		this.output = output;
		writer = new PrintWriter(output, true);
	}    
	
	public void setRequest(Request request) {  
		this.request = request;  
	}
	
	public void sendStatic() throws IOException{
		
		byte[] bytes = new byte[BUFFER_SIZE];  
		FileInputStream fis = null;
		
		try {
			File file = new File(System.getProperty("user.dir")+File.separator+"www", request.getUri());
			fis = new FileInputStream(file);
			int ch = fis.read(bytes, 0, BUFFER_SIZE);  
			while (ch != -1) {  
				output.write(bytes, 0, ch);  
				ch = fis.read(bytes, 0, BUFFER_SIZE);  
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {  
			if (fis != null)  
				fis.close();  
		} 
	}
	
	public PrintWriter getWriter() throws IOException {
		 return writer;
	}
	
	@Override
	public void flushBuffer() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getBufferSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCharacterEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCommitted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resetBuffer() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBufferSize(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCharacterEncoding(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setContentLength(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setContentType(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLocale(Locale arg0) {
		// TODO Auto-generated method stub

	}

}
