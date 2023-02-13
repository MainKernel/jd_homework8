package org.homework.goit;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@WebFilter("/time")
public class TimezoneValidateFilter implements Filter {
    static{
        List<String> utcOffsets = new ArrayList<>();
        for (String zoneId : ZoneId.getAvailableZoneIds()) {
            ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of(zoneId));
            String offset = zdt.getOffset().getId().replace("Z", "+00:00");
            if (!utcOffsets.contains(offset)) {
                utcOffsets.add(offset);
            }
        }
        validTimeZones = utcOffsets;
    }
    private static final List<String> validTimeZones;

    @Override
    public void init(FilterConfig filterConfig){
        //No init required
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (request instanceof HttpServletRequest) {

            String timezone = request.getParameter("timezone");

           try{
               timezone = String.join("+", timezone.split(" "));
               if(!timezone.equals("UTC")) {
                   timezone = ZoneId.of(timezone).toString().replace("UTC", "");
               }
           }catch (NullPointerException e){
               timezone = "UTC";
           }

            if (!validTimeZones.contains(timezone) & !timezone.equals("UTC")) {
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.sendError(400, "Invalid timezone");
                return;
            }
            chain.doFilter(request, response);
        }

    }

    @Override
    public void destroy() {
        //No destroy required
    }


}
