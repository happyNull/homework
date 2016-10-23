import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;
import java.net.UnknownHostException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class HttpServer {

	public static void main(String[] args) {
		
		HttpServer server = new HttpServer();
		server.await();
		
	}
	
	public void await(){
		
		ServerSocket serverSocket=null;
		int port=8080;
		try {
			serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(true){
			
			Socket socket = null;
			InputStream input = null;
			OutputStream output = null;
			
			try {
				socket = serverSocket.accept();
				input = socket.getInputStream();  
				output = socket.getOutputStream();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}  
			
			Request request=new Request(input);
			request.parse();
			
			if (request.getUri().equals("/SHUTDOWN"))
				break;
			
			Response response = new Response(output);  
			response.setRequest(request);
			String uri = request.getUri();
			if (uri.startsWith("/servlet/")){
				//动态 
				String servletName = uri.substring(uri.lastIndexOf("/") + 1);
				if(servletName.contains("?"))
					servletName=servletName.substring(0,servletName.indexOf("?"));
				//解析web.xml
				File file = new File(System.getProperty("user.dir")+File.separator+"src"+File.separator+"web.xml");
				String xml="";
				try {
			        FileReader reader = new FileReader(file);
					int fileLen = (int)file.length();
			        char[] chars = new char[fileLen];
			        reader.read(chars);
			        xml = String.valueOf(chars);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Document doc = Jsoup.parse(xml);
				Elements servlets = doc.getElementsByTag("servlet");
			    for(Element i_servlet:servlets){
			    	if(servletName.equals(i_servlet.getElementsByTag("servlet-name").get(0).text())){
			    		URLClassLoader loader = null;
			    		URLStreamHandler streamHandler = null;
			    		try {
							loader = new URLClassLoader(new URL[]{new URL(null, "file:"+System.getProperty("user.dir")+File.separator+"src"+File.separator+"Servlet", streamHandler)});
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    		
			    		Class<?> myClass = null;
			    		try {
							myClass = loader.loadClass("Servlet."+servletName);
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    		
			    		Servlet servlet = null;
			    		
			    		try {
							servlet = (Servlet) myClass.newInstance();
							servlet.service((ServletRequest) request,(ServletResponse) response);
						} catch (InstantiationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ServletException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    		break;
			    	}
			    }
			    
			}else if(uri.endsWith(".html")){
				//静态
				try {
					response.sendStatic();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(uri.endsWith(".jsp")){
				//jsp转换为servlet
				JSPHandler.parseJSP(uri.substring(1),request,response);
			}
			
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
