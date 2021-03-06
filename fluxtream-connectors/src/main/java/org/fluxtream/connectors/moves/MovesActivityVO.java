package org.fluxtream.connectors.moves;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;
import org.codehaus.plexus.util.StringUtils;
import org.fluxtream.OutsideTimeBoundariesException;
import org.fluxtream.connectors.vos.AbstractTimedFacetVO;
import org.fluxtream.connectors.vos.TimeOfDayVO;
import org.fluxtream.domain.GuestSettings;
import org.fluxtream.mvc.models.DurationModel;

/**
 * User: candide
 * Date: 20/06/13
 * Time: 17:33
 */
public class MovesActivityVO {

    public int startMinute;
    public int endMinute;
    public boolean manual;
    public String activity, activityCode = "generic";
    public String activityGroup;
    public String distance;
    public Integer steps;
    public DurationModel duration;
    public TimeOfDayVO startTime, endTime;
    public String date;
    public final String type = "moves-move-activity";
    public long start, end;

    private static final ArrayList<String> validActivityCodes = new ArrayList<String>(Arrays.asList(new String[]{"running", "walking", "cycling", "transport"}));

    public MovesActivityVO(MovesActivity activity, TimeZone timeZone,
                           long dateStart, long dateEnd,
                           GuestSettings settings, boolean doDateBoundsCheck) throws OutsideTimeBoundariesException {
        this.activity = StringUtils.capitalise(activity.activity);
        if (activity.activityGroup!=null&&validActivityCodes.contains(activity.activityGroup))
            this.activityCode = activity.activityGroup;
        else if (activity.activityGroup==null&&validActivityCodes.contains(activity.activity))
            this.activityCode = activity.activity;
        this.date = activity.date;
        this.start = activity.start;
        this.end = activity.end;
        this.activityGroup = activity.activityGroup;
        this.manual = activity.manual!=null?activity.manual:false;

        // Potentially trucate to fit within the date
        long truncStartMilli = activity.start;
        long truncEndMilli = activity.end;
        boolean timeTruncated = false;

        // If we're doing date bounds checking, check if facet is entirely outside the time bounds of this date
        // If so, throw an exception so this facet isn't returned.  Otherwise potentially trim the start and end times
        if(doDateBoundsCheck) {
            if (activity.end<dateStart || activity.start>dateEnd) {
                throw new OutsideTimeBoundariesException();
            }

            // We know this facet overlaps the time bounds of date, check if it needs
            // to be truncated.
            if(activity.start<dateStart){
                truncStartMilli = dateStart;
                timeTruncated=true;
            }

            if(activity.end>=dateEnd) {
                truncEndMilli = dateEnd-1;
                timeTruncated=true;
            }
        }

        if (this.manual) {
            this.duration = new DurationModel(activity.duration);
        } else {
            // Calculate start/end Minute and Time based on truncated millisecond time
            this.startMinute = AbstractTimedFacetVO.toMinuteOfDay(new Date(truncStartMilli), timeZone);
            this.endMinute = AbstractTimedFacetVO.toMinuteOfDay(new Date(truncEndMilli), timeZone);

            this.startTime = new TimeOfDayVO(this.startMinute, true);
            this.endTime = new TimeOfDayVO(this.endMinute, true);

            // The args for creating a DurationModel are in seconds.
            // The units of start and end are milliseconds, so divide by 1000 to
            // calculate the duration in seconds to pass to the Duration Model.
            this.duration = new DurationModel((int)((truncEndMilli-truncStartMilli)/1000));

            // Note that the distance isn't going to be accurate here if we've done truncation
            // In that case, skip distance and steps for now
            if (activity.distance>0 && !timeTruncated) {
                if (settings.distanceMeasureUnit==GuestSettings.DistanceMeasureUnit.SI)
                    getMetricDistance(activity);
                else
                    getImperialdistance(activity);
            }
            if(activity.steps!=null && !timeTruncated) {
                this.steps = activity.steps;
            }
        }
    }

    private void getImperialdistance(final MovesActivity activity) {
        double yards = activity.distance / 0.9144;
        double miles = activity.distance * 0.00062137119;

        if (miles>1) {
            DecimalFormat df = new DecimalFormat("0.#");
            String mstr = df.format(miles);
            if(mstr.contentEquals("1")) {
                this.distance = "1 mile";
            }
            else {
                this.distance = mstr + " miles";
            }
        }
        else {
            int yint = (int)(round(yards));
            if(yint == 1) {
                this.distance = "1 yard";
            }
            else {
                this.distance = yint + " yards";
            }
        }
    }

    private void getMetricDistance(final MovesActivity activity) {
        if (activity.distance>1000) {
            double km = round((double)activity.distance/1000d);
            this.distance = km + " km";
        } else {
            this.distance = activity.distance + " m";
        }
    }

    double round(double v) {
        return (double) Math.round(v * 100) / 100;
    }
}
