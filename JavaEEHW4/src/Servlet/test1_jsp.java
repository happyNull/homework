package Servlet;

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
public class test1_jsp implements Servlet {
public void init(ServletConfig config) throws ServletException {System.out.println("init");}
public void destroy() {System.out.println("destroy");}
public String getServletInfo() {return null;}
public ServletConfig getServletConfig() {return null;}
public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
PrintWriter out = response.getWriter();
out.print("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\"><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><title>test jsp1</title></head><body>");

List list=new ArrayList();
list.add("Issac");
list.add("Judas");
list.add("Cain");
list.add("AZ");
list.add("Lost");

out.print("<ul>	");
for(int i=0;i<list.size();i++){ 
out.print("	<li>");
out.print(list.get(i) );
out.print("</li>	");
} 
out.print("</ul></body></html>");
out.flush();
}
}