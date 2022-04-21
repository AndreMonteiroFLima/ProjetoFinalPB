package com.adobe.aem.guides.wknd.core.utils;

import com.adobe.aem.guides.wknd.core.models.ErroMessage;
import com.adobe.aem.guides.wknd.core.models.ReportModel;
import com.google.gson.Gson;
import org.apache.sling.api.SlingHttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class ResponseSetter {

    public static void setOkResponse(String message, int httpCode, SlingHttpServletResponse response) throws IOException {
        response.setStatus(httpCode);
        response.getWriter().write(message);
    }

    public static void setResponse(String message, int httpCode, SlingHttpServletResponse response,String urlReturn) throws IOException {
        ErroMessage errorMessage = new ErroMessage(message, httpCode, urlReturn);
        response.setStatus(httpCode);
        response.getWriter().write(new Gson().toJson(errorMessage));
    }

    public static void setHtmlResponse(SlingHttpServletResponse response, ReportModel report,int httpCode) throws IOException {
        response.setStatus(httpCode);
        PrintWriter out = response.getWriter();
        StringBuilder sb = new StringBuilder();
        
        
        sb.append("<!DOCTYPE html>");
        sb.append("<html>");
        sb.append("<head> <meta charset=\"UTF-8\">\n" +
                "<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3\" crossorigin=\"anonymous\">" +
                "<title>User Report</title></head>");
        sb.append("<body class=\"bg-secondary\">");
        sb.append("<div class=\"container mt-5\">");
        sb.append("<h1>User Report</h1>");
        sb.append("<h2>Id: ").append(report.getClient().getId()).append(", Name: ").append(report.getClient().getName()).append("</h2>");
        sb.append("<table class=\"table table-dark table-striped\">");
        sb.append("<thead>");
        sb.append("<tr>");
        sb.append("<th scope=\"col\">Id</th>");
        sb.append("<th scope=\"col\">Name</th>");
        sb.append("<th scope=\"col\">Description</th>");
        sb.append("<th scope=\"col\">Price</th>");
        sb.append("</tr>");
        sb.append("</thead>");
        sb.append("<tbody>");
        report.getProducts().forEach(product -> {
            sb.append("<tr>");
            sb.append("<td>").append(product.getId()).append("</td>");
            sb.append("<td>").append(product.getName()).append("</td>");
            sb.append("<td>").append(product.getDescription()).append("</td>");
            sb.append("<td>").append("R$").append(product.getPrice()).append("</td>");
            sb.append("</tr>");
        });
        sb.append("</tbody>");
        sb.append("</table>");
        sb.append("</div>");
        sb.append("</body></html>");

        out.println(sb.toString());

    }

}
