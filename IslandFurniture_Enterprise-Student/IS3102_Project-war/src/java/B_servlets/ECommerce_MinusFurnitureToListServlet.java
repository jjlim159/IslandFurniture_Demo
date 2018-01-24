package B_servlets;

import HelperClasses.ShoppingCartLineItem;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet(name = "ECommerce_MinusFurnitureToListServlet", urlPatterns = {"/ECommerce_MinusFurnitureToListServlet"})
public class ECommerce_MinusFurnitureToListServlet extends HttpServlet {

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
        
        try {
            HttpSession session = request.getSession();
            String sku = request.getParameter("SKU");
            ArrayList<ShoppingCartLineItem> shoppingCart = (ArrayList<ShoppingCartLineItem>) session.getAttribute("shoppingCart");
            minusQuantity(shoppingCart,sku);
            
            session.setAttribute("goodMsg", null);
            session.setAttribute("shoppingCart", shoppingCart);
            response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp");
        } catch(Exception ex) {
        }
    }

    public void minusQuantity(ArrayList<ShoppingCartLineItem> shoppingCart, String sku) {
        for (ShoppingCartLineItem item: shoppingCart) {
            if (item.getSKU().equals(sku)) {
                if (item.getQuantity() == 1)
                    shoppingCart.remove(item);
                else
                    item.setQuantity(item.getQuantity()-1);
            }
        }
    }
    
    /*
    public boolean minusQuantity(ArrayList<ShoppingCartLineItem> shoppingCart, String sku, int index) {
        ShoppingCartLineItem item = shoppingCart.get(index);
        if (item.getSKU().equals(sku)) {
            if (item.getQuantity() == 1) {
                shoppingCart.remove(item);
            }
            else {
                item.setQuantity(item.getQuantity()-1);
            }
            return true;
        } else {
            return minusQuantity(shoppingCart, sku, index+1);
        }
    }*/
    
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
