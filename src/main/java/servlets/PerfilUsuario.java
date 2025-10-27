/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.PerfilusuarioJpaController;
import dto.Perfilusuario;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author ABNER
 */
@WebServlet(name = "PerfilUsuario", urlPatterns = {"/perfilusuario"})
public class PerfilUsuario extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            String op = request.getParameter("op");
            
            switch(op){
                case "1":
                    //Cargar perfil de usuario
                    HttpSession session = request.getSession();
                    String coduni = (String) session.getAttribute("codunisession");
                    //String coduni = request.getParameter("coduni");
                    
                    PerfilusuarioJpaController perfDAO = new PerfilusuarioJpaController();
                    Perfilusuario perfil = perfDAO.findPerfilByCodUni(coduni);
                    
                    if (perfil != null) {
                        //Gson g = new Gson();
                        //String perfil = g.toJson(perfUsuario);
                        //out.print("{\"resultado\" : \""+perfUsuario.getNombre()+"\"}");
                        //out.print("{\"resultado\" : \"ok\"}");
                        //out.print(perfil);

                        StringBuilder sb = new StringBuilder();
                        sb.append("{");
                        sb.append("\"nombre\":\"").append(perfil.getNombre()).append("\",");
                        sb.append("\"apellido\":\"").append(perfil.getApellido()).append("\",");
                        sb.append("\"fechnaci\":\"").append(new SimpleDateFormat("yyyy-MM-dd").format(perfil.getFechnaci())).append("\",");
                        sb.append("\"coduni\":\"").append(perfil.getCoduni()).append("\",");
                        sb.append("\"facultad\":\"").append(perfil.getFacultad()).append("\",");
                        sb.append("\"escuela\":\"").append(perfil.getEscuela()).append("\",");
                        sb.append("\"direccion\":\"").append(perfil.getDireccion()).append("\",");
                        sb.append("\"correo\":\"").append(perfil.getCorreo()).append("\",");
                        sb.append("\"celular\":\"").append(perfil.getCelular()).append("\",");
                        sb.append("\"dni\":\"").append(perfil.getDni()).append("\"");
                        sb.append("}");

                        String json = sb.toString();
                        out.print(json);
                        //out.print("{\"resultado\" : \"ok\"}");
                    }else{
                        out.print("{\"resultado\" : \"error\"}");
                    }
                    break;
            }
            
            
            
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
