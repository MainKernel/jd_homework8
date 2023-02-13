package org.homework.goit;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

@WebServlet(value = "/time")
public class UserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //query ?timezone=UTC+2
        String timezone = req.getParameter("timezone");

        try{
            timezone = String.join("+", timezone.split(" "));
        }catch (NullPointerException e){
            timezone = "UTC";
        }


        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");

        ZonedDateTime now = ZonedDateTime.now(java.time.ZoneId.of(timezone));
        String currentTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"));

        try (PrintWriter writer = resp.getWriter()){
            writer.println(getHTMLPage(currentTime));
        }

    }

    private String getHTMLPage(String time){
        return "<!DOCTYPE html" +
                "<html>" +
                "<head>" +
                "<title>Current Time</title>" +
                "</head>" +
                "<body>" +
                 "<h1> Current Time </h1>" +
                "<p>" + time + "</p>" +
                "</body>" +
                "</html";
    }


}