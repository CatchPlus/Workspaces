<%@ page language="java" %>
<%

    //! Tradetracker Conversion-Tag.

    // Define parameters.
    String campaignID = request.getParameter("campaignID") == null ? "" : request.getParameter("campaignID");
    String productID = request.getParameter("productID") == null ? "" : request.getParameter("productID");
    String conversionType = request.getParameter("conversionType") == null ? "" : request.getParameter("conversionType");
    boolean useHttps = request.getParameter("https").equals("1");

    // Get tracking data from the session created on the redirect-page.
    String trackingData = (String) session.getAttribute("TT2_" + campaignID);
    String trackingType = "1";

    // If tracking data is empty.
    if (trackingData == null) {
        // Get tracking data from the cookie created on the redirect-page.
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals("TT2_" + campaignID)) {
                    trackingData = cookies[i].getValue();
                    trackingType = "2";
                }
            }
        }
    }

    if (trackingData == null)
        trackingData = "";

    // Set transaction information.
    String transactionID = request.getParameter("transactionID") == null ? "" : request.getParameter("transactionID"); // Transaction identifier.
    String transactionAmount = request.getParameter("transactionAmount") == null ? "" : request.getParameter("transactionAmount"); // Transaction amount.
    String quantity = request.getParameter("quantity") == null ? "" : request.getParameter("quantity"); // Quantity (optional).
    String email = request.getParameter("email") == null ? "" : request.getParameter("email"); // Customer e-mail address if available (optional).
    String descriptionMerchant = request.getParameter("descrMerchant") == null ? "" : request.getParameter("descrMerchant"); // Transaction details for merchants (optional).
    String descriptionAffiliate = request.getParameter("descrAffiliate") == null ? "" : request.getParameter("descrAffiliate"); // Transaction details for affiliates (optional).

    // Set track-back URL.
    String trackBackURL = (useHttps ? "https" : "http") + "://" + ("lead".equalsIgnoreCase(conversionType) ? "tl" : "ts") + ".tradetracker.net/?cid=" + campaignID + "&pid=" + productID + "&data="
            + java.net.URLEncoder.encode(trackingData) + "&type=" + trackingType + "&tid=" + transactionID + "&tam=" + transactionAmount + "&qty=" + quantity + "&eml=" + email + "&descrMerchant="
            + descriptionMerchant + "&descrAffiliate=" + descriptionAffiliate;

    // Register transaction.
    response.sendRedirect(trackBackURL);

%>