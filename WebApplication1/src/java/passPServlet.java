import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class passPServlet extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        res.setContentType("text/html");
        PrintWriter pwl = res.getWriter();

        String uid = req.getParameter("email");
        String pass = req.getParameter("password");

        try {
            HttpSession ses = req.getSession();
            ses.setAttribute("email", uid);
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/medicSystem", "root", "PHW#84#jeor"); 

            String query = "SELECT * FROM patientInfo WHERE e_mail = ? AND password = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            
            pstmt.setString(1, uid);
            pstmt.setString(2, pass);

            ResultSet rs = pstmt.executeQuery();
               if (rs.next()) {
                HttpSession session = req.getSession();
                String username = rs.getString("name");
                
                session.setAttribute("username", username);
                pwl.println("<html><body>");
                pwl.println("<script>");
                pwl.println("localStorage.setItem('username', '" + username + "');");
                pwl.println("window.location.href = 'Patient/index.html';"); 
                pwl.println("</script>");
                pwl.println("</body></html>");
            } else {
                pwl.println("<html><body>Invalid Login</body></html>");
            }

            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            pwl.print("<html><body>Error: " + e.getMessage() + "</body></html>");
        }
    }
} 