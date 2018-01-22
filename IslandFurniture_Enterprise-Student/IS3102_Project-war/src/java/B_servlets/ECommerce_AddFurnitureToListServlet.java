package B_servlets;

import HelperClasses.ShoppingCartLineItem;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Double.parseDouble;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@WebServlet(name = "ECommerce_AddFurnitureToListServlet", urlPatterns = {"/ECommerce_AddFurnitureToListServlet"})
public class ECommerce_AddFurnitureToListServlet extends HttpServlet {

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
            ArrayList<ShoppingCartLineItem> shoppingCart = (ArrayList<ShoppingCartLineItem>) session.getAttribute("shoppingCart");
            ShoppingCartLineItem newItem = new ShoppingCartLineItem();
            String sku = request.getParameter("SKU");
            int qtyAvailable = getStockAvailable(sku);
            boolean itemExist = false;
            
            String goodMsg = "Item successfully added into the cart!";
            String errMsg = "Item not added to cart, not enough quantity available.";
            
            if (shoppingCart == null) {
                shoppingCart = new ArrayList<>();
            }
            else {
                for (ShoppingCartLineItem item: shoppingCart) {
                    if (item.getSKU().equalsIgnoreCase(sku)) {
                        itemExist = true;
                        if (qtyAvailable > item.getQuantity()) {
                            item = addItemQuantity(item);
                            errMsg = null;
                        }
                        else {
                            goodMsg = null;
                        }
                        break;
                    }
                }
            }    
            
            if (itemExist == false && qtyAvailable > 0) {
                newItem.setId(request.getParameter("id"));
                newItem.setSKU(sku);
                newItem.setName(request.getParameter("name"));
                newItem.setImageURL(request.getParameter("imageURL"));
                newItem.setPrice(parseDouble(request.getParameter("price")));
                newItem.setQuantity(1);
                shoppingCart = addNewItem(newItem,shoppingCart);
                errMsg=null;
            }
            else if (qtyAvailable == 0) {
                goodMsg=null;
            }
            
            session.setAttribute("shoppingCart", shoppingCart);
            session.setAttribute("errMsg", errMsg);
            session.setAttribute("goodMsg", goodMsg);
            response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp");
        } catch(Exception ex) {
        }
    }

    public ArrayList<ShoppingCartLineItem> addNewItem(ShoppingCartLineItem newItem, ArrayList<ShoppingCartLineItem> shoppingCart) {
        shoppingCart.add(newItem);
        return shoppingCart;
    }
    
    public ShoppingCartLineItem addItemQuantity(ShoppingCartLineItem item) {
        item.setQuantity(item.getQuantity()+1);
        return item;
    }
    
    public int getStockAvailable(String SKU) {
        try {
            System.out.println("getQuantity() SKU: " + SKU);
            Client client = ClientBuilder.newClient();
            WebTarget target = client
                    .target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.countryentity/")
                    .path("getQuantity")
                    .queryParam("SKU", SKU);
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();
            System.out.println("status: " + response.getStatus());
            if (response.getStatus() != 200) {
                return 0;
            }
            String result = (String) response.readEntity(String.class);
            return Integer.parseInt(result);

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
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
