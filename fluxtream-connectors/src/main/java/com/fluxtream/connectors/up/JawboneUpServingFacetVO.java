package com.fluxtream.connectors.up;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;
import com.fluxtream.OutsideTimeBoundariesException;
import com.fluxtream.TimeInterval;
import com.fluxtream.connectors.vos.AbstractPhotoFacetVO;
import com.fluxtream.domain.GuestSettings;
import com.fluxtream.mvc.models.DimensionModel;

/**
 * User: candide
 * Date: 11/02/14
 * Time: 15:54
 */
public class JawboneUpServingFacetVO extends AbstractPhotoFacetVO<JawboneUpServingFacet> {

    public Map<Integer, String> thumbnailUrls = new HashMap<Integer, String>();
    public SortedMap<Integer, Dimension> thumbnailSizes = new TreeMap<Integer, Dimension>();
    public String thumbnailUrl;
    public String photoUrl;
    public float[] position;

    @Override
    protected void fromFacet(final JawboneUpServingFacet facet, final TimeInterval timeInterval, final GuestSettings settings) throws OutsideTimeBoundariesException {
        deviceName = "Jawbone UP";
        channelName = "photo";
        UID = facet.getId();
        start = facet.start;
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        c.setTimeInMillis(start);
        startMinute = c.get(Calendar.HOUR_OF_DAY)*60+c.get(Calendar.MINUTE);

        int i = 0;

        String homeBaseUrl = settings.config.get("homeBaseUrl");
        final String thumbnailUrl = JawboneUpVOHelper.getImageURL(facet.image, facet, settings.config);
        this.thumbnailUrl = thumbnailUrl;
        this.photoUrl = JawboneUpVOHelper.getImageURL(facet.image, facet, settings.config, 150);
        thumbnailUrls.put(i, thumbnailUrl);
        thumbnailSizes.put(i, new Dimension(150, 150));
        i++;

        // hereafter, flickr documentation specifies a number of pixels *on longest side* - since we don't have the
        // original image's dimension, we just specify a square of that number
        for (Integer width : new Integer[]{75, 100, 240, 320, 500, 640, 800, 1024}) {
            thumbnailUrls.put(i, JawboneUpVOHelper.getImageURL(facet.image, facet, settings.config, width));
            thumbnailSizes.put(i, new Dimension(width, width));
            i++;
        }

        if (facet.meal.place_lon!=null && facet.meal.place_lat!=null){
            position = new float[2];
            position[0] = facet.meal.place_lat.floatValue();
            position[1] = facet.meal.place_lon.floatValue();
        }
    }

    @Override
    public String getPhotoUrl() {
        return photoUrl;
    }

    @Override
    public String getThumbnail(final int index) {
        return thumbnailUrls.get(index);
    }

    @Override
    public List<DimensionModel> getThumbnailSizes() {
        List<DimensionModel> dimensions = new ArrayList<DimensionModel>();
        for (Dimension dimension : thumbnailSizes.values()) {
            dimensions.add(new DimensionModel(dimension.width, dimension.height));
        }
        return dimensions;
    }
}
