package B_servlets;

import CorporateManagement.FacilityManagement.FacilityManagementBeanLocal;
import EntityManager.CountryEntity;
import OperationalCRM.LoyaltyAndRewards.LoyaltyAndRewardsBeanLocal;
import EntityManager.LoyaltyTierEntity;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ECommerce_MemberLoginServlet extends HttpServlet {

    @EJB
    private FacilityManagementBeanLocal facilityManagementBean;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String result = "";
        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            Cookie cookie = new Cookie("memberId", email);
            cookie.setMaxAge(60 * 60); //1 hour
            response.addCookie(cookie);

            HttpSession session = request.getSession();
            RequestDispatcher dispatcher = request.getRequestDispatcher("/ECommerce_GetMember");
            String memberName = loginMember(email, password);
            
            if (memberName != null) {
                List<CountryEntity> countries = facilityManagementBean.getListOfCountries();
                session.setAttribute("countries", countries);
                session.setAttribute("email", email);
                dispatcher.forward(request, response);
            } else {
                result = "Login fail. Username or password is wrong or account is not activated.";
                response.sendRedirect("/IS3102_Project-war/B/SG/memberLogin.jsp?errMsg=" + result);
            }
            
        } catch (Exception ex) {
            out.println(ex);
            ex.printStackTrace();
        }
    }

    public String loginMember(String email, String password) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client
                .target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.memberentity").path("login")
                .queryParam("email", email)
                .queryParam("password", password);
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        System.out.println("status: " + response.getStatus());

        if (response.getStatus() != 200) {
            return null;
        }
        
        String memberName = response.readEntity(String.class);
        return memberName;
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
