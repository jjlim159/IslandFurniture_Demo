package service;

import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("commerce")
public class ECommerceFacadeREST {

    @Context
    private UriInfo context;

    public ECommerceFacadeREST() {
    }

    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of ECommerce
     *
     * @param memberID
     * @param amountPaid
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    public Response createECommerceTransactionRecord(@QueryParam("memberID")long memberID, @QueryParam("amountPaid")double amountPaid) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/islandfurniture-it07?zeroDateTimeBehavior=convertToNull&user=root&password=12345");
            String stmt = "INSERT INTO salesrecordentity (member_Id, amountDue, amountPaid, STORE_ID) VALUES (?,?,?,?);";
            PreparedStatement ps = conn.prepareStatement(stmt);
            ps.setLong(1, memberID);
            ps.setDouble(2, amountPaid);
            ps.setDouble(3, 25.0);
            ps.setNull(4, java.sql.Types.BIGINT);
            ps.executeUpdate();
            
            return Response.ok("Transaction Record Successfully Created", MediaType.APPLICATION_JSON).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    
    /*
    @PUT
    @Consumes({"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public Response createECommerceLineItemRecord(@QueryParam("salesRecordID")long salesRecordID, @QueryParam("itemEntityID")long itemEntityID, 
            @QueryParam("quantity")int quantity, @QueryParam("countryID")long countryID) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/islandfurniture-it07?zeroDateTimeBehavior=convertToNull&user=root&password=12345");
            String stmt = "INSERT INTO lineitementity (Quantity, ITEM_ID) VALUES(?,?)"
                    + "INSERT INTO ";
            PreparedStatement ps = conn.prepareStatement(stmt);
            ps.setLong(1, salesRecordID);
            ps.setLong(2, itemEntityID);
            ps.setInt(3, quantity);
            ps.executeUpdate();

            return Response.ok("Transaction Record Successfully Created", MediaType.APPLICATION_JSON).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }*/
}
