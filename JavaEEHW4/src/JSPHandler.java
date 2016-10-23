import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


public class JSPHandler {

	public static void parseJSP(String fileName,Request req,Response res){
		
		File file = new File(System.getProperty("user.dir")+File.separator+"jsp"+File.separator+fileName);
		File file2 = new File(System.getProperty("user.dir")+File.separator+"src"+File.separator+"Servlet"+File.separator+""+fileName.substring(0,fileName.length()-4)+"_jsp.java");
		String jsp="",servlet="";
		int index1=0,index2=0;
		
		if(!file2.exists()){//如果.java不存在，则生成一个
			//读取jsp全部内容
			try {
		        FileReader reader = new FileReader(file);
				int fileLen = (int)file.length();
		        char[] chars = new char[fileLen];
		        reader.read(chars);
		        jsp = String.valueOf(chars);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			servlet+="package Servlet;\n\nimport javax.servlet.*;\nimport java.io.IOException;\nimport java.io.PrintWriter;\n";
			//获取import
			if(jsp.contains("<%@ page")){
				index1=jsp.indexOf("<%@ page");
				index2=jsp.substring(index1).indexOf("%>");
				String page=jsp.substring(index1+9,index1+index2);
				jsp=jsp.substring(0,index1)+jsp.substring(index2+2);
				if(page.contains("import=\"")){
					index1=page.indexOf("import=\"");
					index2=page.substring(index1+8).indexOf("\"");
					String str_import=page.substring(index1+8,index1+8+index2);
					String imports[]=str_import.split(",");
					for(String s:imports){
						servlet+="import "+s+";\n";
					}
				}
			}
			
			servlet+="public class "+fileName.substring(0,fileName.length()-4)+"_jsp"+" implements Servlet {\n";
			servlet+="public void init(ServletConfig config) throws ServletException {System.out.println(\"init\");}\n";
			servlet+="public void destroy() {System.out.println(\"destroy\");}\n";
			servlet+="public String getServletInfo() {return null;}\n";
			servlet+="public ServletConfig getServletConfig() {return null;}\n";
			
			servlet+="public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {\n";
			servlet+="PrintWriter out = response.getWriter();\n";
			
			//剩余部分，<%%>照常输出，<%=%>与剩余用out.print()包围
			while(true){
				if(jsp.contains("<%")){
					index1=jsp.indexOf("<%");
					index2=jsp.indexOf("%>");
					if(index1!=0){
						servlet+="out.print(\""+jsp.substring(0,index1).replace("\"", "\\\"").replace("\r\n", "").replace("\n", "")+"\");\n";
					}
					if(jsp.charAt(index1+2)=='='){
						//<%=...%>
						servlet+="out.print("+jsp.substring(index1+3,index2).replace("\r\n", "").replace("\n", "")+");\n";
					}else{
						//<%...%>
						servlet+=jsp.substring(index1+2,index2)+"\n";
					}
					jsp=jsp.substring(index2+2);
				}
				//如果没有jsp标签，则全部out.print，结束
				else{
					servlet+="out.print(\""+jsp.replace("\"", "\\\"").replace("\r\n", "").replace("\n", "")+"\");\n";
					break;
				}
			}
			servlet+="out.flush();\n}\n}";
			
			//把String servlet写入对应的.java文件
			 try {
				PrintStream ps = new PrintStream(new FileOutputStream(file2));
				ps.append(servlet);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//读取
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
			myClass = loader.loadClass("Servlet."+fileName.replace(".", "_"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Servlet serv = null;
		try {
			serv = (Servlet) myClass.newInstance();
			serv.service((ServletRequest) req,(ServletResponse) res);
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
		
		
	}

}
