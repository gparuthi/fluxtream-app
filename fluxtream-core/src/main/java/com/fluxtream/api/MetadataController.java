package com.fluxtream.api;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.fluxtream.aspects.FlxLogger;
import com.fluxtream.auth.AuthHelper;
import com.fluxtream.mvc.models.StatusModel;
import com.fluxtream.services.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * User: candide
 * Date: 05/06/13
 * Time: 12:07
 */
@Path("/metadata")
@Component("RESTMetadataController")
@Scope("request")
public class MetadataController {

    FlxLogger logger = FlxLogger.getLogger(MetadataController.class);

    @Autowired
    MetadataService metadataService;

    @POST
    @Path(value="/mainCity/date/{date}")
    @Produces({ MediaType.APPLICATION_JSON } )
    public StatusModel setDayMainCity(@FormParam("latitude") float latitude,
                                      @FormParam("longitude") float longitude,
                                      @PathParam("date") String date) {
        final long guestId = AuthHelper.getGuestId();
        StringBuilder sb = new StringBuilder("module=API component=calendarController action=setDayMainCity")
                .append(" guestId=").append(guestId);
        logger.info(sb.toString());
        metadataService.setDayMainCity(guestId, latitude, longitude, date);
        return new StatusModel(true, "OK");
    }

    @POST
    @Path(value="/mainCity/week/{year}/{week}")
    @Produces({ MediaType.APPLICATION_JSON } )
    public StatusModel setWeekMainCity(@FormParam("latitude") float latitude,
                                       @FormParam("longitude") float longitude,
                                       @PathParam("year") int year,
                                       @PathParam("week") int week) {
        final long guestId = AuthHelper.getGuestId();
        StringBuilder sb = new StringBuilder("module=API component=calendarController action=setWeekMainCity")
                .append(" guestId=").append(guestId);
        logger.info(sb.toString());
        metadataService.setWeekMainCity(guestId, latitude, longitude, year, week);
        return new StatusModel(true, "OK");
    }

    @POST
    @Path(value="/mainCity/month/{year}/{month}")
    @Produces({ MediaType.APPLICATION_JSON } )
    public StatusModel setMonthMainCity(@FormParam("latitude") float latitude,
                                        @FormParam("longitude") float longitude,
                                        @PathParam("year") int year,
                                        @PathParam("month") int month) {
        final long guestId = AuthHelper.getGuestId();
        StringBuilder sb = new StringBuilder("module=API component=calendarController action=setMonthMainCity")
                .append(" guestId=").append(guestId);
        logger.info(sb.toString());
        metadataService.setMonthMainCity(guestId, latitude, longitude, year, month);
        return new StatusModel(true, "OK");
    }

    @DELETE
    @Path(value="/mainCity/date/{date}")
    @Produces({ MediaType.APPLICATION_JSON } )
    public StatusModel resetDayMainCity(@PathParam("date") String date) {
        final long guestId = AuthHelper.getGuestId();
        StringBuilder sb = new StringBuilder("module=API component=calendarController action=resetDayMainCity")
                .append(" guestId=").append(guestId);
        logger.info(sb.toString());
        metadataService.resetDayMainCity(guestId, date);
        return new StatusModel(true, "OK");
    }

    @DELETE
    @Path(value="/mainCity/week/{year}/{week}")
    @Produces({ MediaType.APPLICATION_JSON } )
    public StatusModel resetWeekMainCity(@PathParam("year") int year,
                                         @PathParam("week") int week) {
        final long guestId = AuthHelper.getGuestId();
        StringBuilder sb = new StringBuilder("module=API component=calendarController action=resetWeekMainCity")
                .append(" guestId=").append(guestId);
        logger.info(sb.toString());
        metadataService.resetWeekMainCity(guestId, year, week);
        return new StatusModel(true, "OK");
    }

    @DELETE
    @Path(value="/mainCity/month/{year}/{month}")
    @Produces({ MediaType.APPLICATION_JSON } )
    public StatusModel resetMonthMainCity(@PathParam("year") int year,
                                          @PathParam("month") int month) {
        final long guestId = AuthHelper.getGuestId();
        StringBuilder sb = new StringBuilder("module=API component=calendarController action=resetMonthMainCity")
                .append(" guestId=").append(guestId);
        logger.info(sb.toString());
        metadataService.resetMonthMainCity(guestId, year, month);
        return new StatusModel(true, "OK");
    }

    @POST
    @Path(value="/mainCity/{visitedCityId}/date/{date}")
    @Produces({ MediaType.APPLICATION_JSON } )
    public StatusModel setDayMainCity(@PathParam("visitedCityId") long visitedCityId,
                                      @PathParam("date") String date) {
        final long guestId = AuthHelper.getGuestId();
        StringBuilder sb = new StringBuilder("module=API component=calendarController action=setDayMainCity")
                .append(" guestId=").append(guestId);
        logger.info(sb.toString());
        metadataService.setDayMainCity(guestId, visitedCityId, date);
        return new StatusModel(true, "OK");
    }

    @POST
    @Path(value="/mainCity/{visitedCityId}/week/{year}/{week}")
    @Produces({ MediaType.APPLICATION_JSON } )
    public StatusModel setWeekMainCity(@PathParam("visitedCityId") long visitedCityId,
                                       @PathParam("year") int year,
                                       @PathParam("week") int week) {
        final long guestId = AuthHelper.getGuestId();
        StringBuilder sb = new StringBuilder("module=API component=calendarController action=setWeekMainCity")
                .append(" guestId=").append(guestId);
        logger.info(sb.toString());
        metadataService.setWeekMainCity(guestId, visitedCityId, year, week);
        return new StatusModel(true, "OK");
    }

    @POST
    @Path(value="/mainCity/{visitedCityId}/month/{year}/{month}")
    @Produces({ MediaType.APPLICATION_JSON } )
    public StatusModel setMonthMainCity(@PathParam("visitedCityId") long visitedCityId,
                                        @PathParam("year") int year,
                                        @PathParam("month") int month) {
        final long guestId = AuthHelper.getGuestId();
        StringBuilder sb = new StringBuilder("module=API component=calendarController action=setMonthMainCity")
                .append(" guestId=").append(guestId);
        logger.info(sb.toString());
        metadataService.setMonthMainCity(guestId, visitedCityId, year, month);
        return new StatusModel(true, "OK");
    }


}