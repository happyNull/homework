package Servlet;

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.*;
import java.util.*;
public class test_jsp implements Servlet {
public void init(ServletConfig config) throws ServletException {System.out.println("init");}
public void destroy() {System.out.println("destroy");}
public String getServletInfo() {return null;}
public ServletConfig getServletConfig() {return null;}
public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
PrintWriter out = response.getWriter();
out.print("     <html>     <head>     <title>Show time</title>     </head>     <body>       <h1>this is a simple test</h1>        Hello :           ");
     
         SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");     
         String str = format.format(new Date());     
      
out.print("           ");
out.print(str );
out.print("              ");
for(int i = 0 ; i < 10; i++)
          {
            out.println(i);
        
out.print("       <br>        ");
}
        
out.print("    </body>     </html> ");
out.flush();
}
}