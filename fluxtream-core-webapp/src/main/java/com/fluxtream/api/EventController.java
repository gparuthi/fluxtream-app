package com.fluxtream.api;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import com.fluxtream.events.push.PushEvent;
import com.fluxtream.services.EventListenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Candide Kemmler (candide@fluxtream.com)
 */
@Path("/events")
@Component("RESTEventController")
@Scope("request")
/**
 * This controller serves no real purpose but to test the Event Framework
 */
public class EventController {

    @Autowired
    EventListenerService eventListenerService;

    @Path("/push/{connectorName}/{eventType}")
    public void testPushEvent(HttpServletResponse response,
                              @PathParam("connectorName") String connectorName,
                              @PathParam("eventType") String eventType,
                              @RequestParam("flxGuestId") long flxGuestId) throws IOException {
        PushEvent pushEvent = new PushEvent(flxGuestId, connectorName, eventType, null);
        eventListenerService.fireEvent(pushEvent);
        response.getWriter().write("event fired");
    }

}