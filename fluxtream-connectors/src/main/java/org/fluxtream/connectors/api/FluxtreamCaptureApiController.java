package org.fluxtream.connectors.api;

import com.google.gson.Gson;
import org.fluxtream.connectors.fluxtream_capture.FluxtreamCaptureHelper;
import org.fluxtream.connectors.fluxtream_capture.FluxtreamCaptureObservationFacet;
import org.fluxtream.connectors.fluxtream_capture.FluxtreamCaptureTopic;
import org.fluxtream.core.auth.AuthHelper;
import org.fluxtream.core.connectors.Connector;
import org.fluxtream.core.domain.ApiKey;
import org.fluxtream.core.domain.Guest;
import org.fluxtream.core.mvc.models.StatusModel;
import org.fluxtream.core.services.ApiDataService;
import org.fluxtream.core.services.GuestService;
import org.fluxtream.core.services.JPADaoService;
import org.fluxtream.core.utils.JPAUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;


// This will actually appear under /api/fluxtream_capture/ because of the
// configuration for org.fluxtream.connectors.api in web.xml
@Path("/fluxtream_capture")
@Component("RESTFluxtreamCaptureApiController")
@Scope("request")
public class FluxtreamCaptureApiController {
    @Autowired
    GuestService guestService;

    @Autowired
    protected JPADaoService jpaDaoService;

    @Autowired
    protected ApiDataService apiDataService;

    @Autowired
    FluxtreamCaptureHelper fluxtreamCaptureHelper;

    Gson gson = new Gson();

    @GET
    @Path("/test")
    @Produces({ MediaType.APPLICATION_JSON })
    public String test() {
        long guestId;
        try {
            Guest guest = AuthHelper.getGuest();
            guestId = guest.getId();
        } catch (Throwable e) {
            return gson.toJson(new StatusModel(false,"Access Denied"));
        }

        String response = "Works! guestId is "+ Long.toString(guestId);
        StatusModel statusModel = new StatusModel(true,response);
        return gson.toJson(statusModel);
    }

    @GET
    @Path("/getLatestObservation")
    @Produces({ MediaType.APPLICATION_JSON })
    public String getLatestObservation() {
        long guestId;
         try {
             Guest guest = AuthHelper.getGuest();
             guestId = guest.getId();
         } catch (Throwable e) {
             return gson.toJson(new StatusModel(false,"Access Denied"));
         }

        // Get the apiKey for fluxtream_capture for this user, or create one if they don't already have one
        // Note that this is susceptible to race conditions, as is the one in BodyTrackController.java
        final Connector connector = Connector.getConnector("fluxtream_capture");
        final ApiKey apiKey;
        List<ApiKey> apiKeys = guestService.getApiKeys(guestId, connector);
        if (apiKeys != null && !apiKeys.isEmpty()) {
            apiKey = apiKeys.get(0);
        }
        else {
            apiKey = guestService.createApiKey(guestId, connector);
        }

        // Get the latest FluxtreamCaptureObservationFacets from the facet table
        final String entityName = JPAUtils.getEntityName(FluxtreamCaptureObservationFacet.class);
        final List<FluxtreamCaptureObservationFacet> newest = jpaDaoService.executeQueryWithLimit(
                "SELECT facet from " + entityName + " facet WHERE facet.apiKeyId=? ORDER BY facet.start DESC",
                100,
                FluxtreamCaptureObservationFacet.class, apiKey.getId());

        String response = "";

        // Check if we got anything
        if (newest.size()>0) {
             System.out.println("Found one!");
            response = "Found one!";
            return gson.toJson(newest);
         }
         else {
             System.out.println("No observations");
            response = "No observations";
            StatusModel statusModel = new StatusModel(true,response);
            return gson.toJson(statusModel);
         }
    }

    @GET
    @Path("/getAllTopics")
    @Produces({ MediaType.APPLICATION_JSON })
    public String getAllTopics() {
        long guestId;
        try {
            Guest guest = AuthHelper.getGuest();
            guestId = guest.getId();

            List<FluxtreamCaptureTopic> topics = fluxtreamCaptureHelper.getTopics(guestId);

            // Check if we got anything
            if (topics != null && topics.size()>0) {
                System.out.println("Found topics");
                return gson.toJson(topics);
            }

            // If topics list is empty
            System.out.println("No topics found");
            return "[]";

        } catch (Throwable e) {
            return gson.toJson(new StatusModel(false,"Access Denied"));
        }

    }


    @GET
    @Path("/insertTestObservation")
    @Produces({ MediaType.APPLICATION_JSON })
    public String insertTestObservation() throws Exception {
        long guestId;
        try {
            Guest guest = AuthHelper.getGuest();
            guestId = guest.getId();
        } catch (Throwable e) {
            return gson.toJson(new StatusModel(false,"Access Denied"));
        }

        // Get the apiKey for fluxtream_capture for this user, or create one if they don't already have one
        // Note that this is susceptible to race conditions, as is the one in BodyTrackController.java
        final Connector connector = Connector.getConnector("fluxtream_capture");
        final ApiKey apiKey;
        List<ApiKey> apiKeys = guestService.getApiKeys(guestId, connector);
        if (apiKeys != null && !apiKeys.isEmpty()) {
            apiKey = apiKeys.get(0);
        }
        else {
            apiKey = guestService.createApiKey(guestId, connector);
        }

        FluxtreamCaptureObservationFacet ret = (FluxtreamCaptureObservationFacet)
                // Look at createOrReadModifyWrite in MymeeUpdater for how to add ID to this query!
                apiDataService.createOrReadModifyWrite(FluxtreamCaptureObservationFacet.class,
                                                       new ApiDataService.FacetQuery("e.apiKeyId = ?", apiKey.getId()),
                                                       new ApiDataService.FacetModifier<FluxtreamCaptureObservationFacet>() {
                    // Throw exception if it turns out we can't make sense of the observation's JSON
                    // This will abort the transaction
                    @Override
                    public FluxtreamCaptureObservationFacet createOrModify(FluxtreamCaptureObservationFacet facet, Long apiKeyId) {
                        try {
                            if (facet == null) {
                                facet = new FluxtreamCaptureObservationFacet(apiKey.getId());
                                facet.guestId = apiKey.getGuestId();
                                facet.api = apiKey.getConnector().value();
                            }

                            // Here we would take json from the arguments and use it to fill in the
                            // contents of the facet object if this were a real world write function

                            System.out.println("Saving FluxtreamCaptureObservation");
                        }
                        catch (Throwable ignored) {
                            return null;
                        }

                        return facet;
                    }
                }, apiKey.getId());

        String response = "ok";
        StatusModel statusModel = new StatusModel(true,response);
        return gson.toJson(statusModel);
    }
}