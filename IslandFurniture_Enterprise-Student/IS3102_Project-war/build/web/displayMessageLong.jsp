<%
    String errMsg = (String)session.getAttribute("errMsg");
    String goodMsg = (String)session.getAttribute("goodMsg");
%>
<div class="row">
    <div class="col-lg-12">
        <%
            if ((errMsg != null) && (goodMsg == null)) {
                if (!errMsg.equals("")) {
                    out.println("<div class='alert alert-danger'>" + errMsg + "</div>");
                }
            } else if ((errMsg == null && goodMsg != null)) {
                if (!goodMsg.equals("")) {
                    out.println("<div class='alert alert-success'>" + goodMsg + "</div>");
                }
            }
        %>
    </div>
</div>
<!-- /.warning -->