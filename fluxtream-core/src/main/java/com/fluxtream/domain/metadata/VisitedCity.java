package com.fluxtream.domain.metadata;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import com.fluxtream.connectors.location.LocationFacet;
import com.fluxtream.domain.AbstractLocalTimeFacet;
import org.hibernate.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * User: candide
 * Date: 28/04/13
 * Time: 10:31
 */
@Entity(name="Facet_VisitedCity")
@Indexed
@NamedQueries( {
       @NamedQuery( name="visitedCities.byApiDateAndCity",
                    query="SELECT facet from Facet_VisitedCity facet WHERE facet.apiKeyId=? AND facet.date=? AND facet.city.id=?")
})
public class VisitedCity extends AbstractLocalTimeFacet implements Comparable<VisitedCity>{

    @Index(name="locationSource_index")
    public LocationFacet.Source locationSource;

    public int sunrise;
    public int sunset;

    public long count;

    public Byte mainCityBitPattern;

    public transient int daysInferred;

    private transient DateTime dateTime;

    protected static final DateTimeFormatter formatter = DateTimeFormat
            .forPattern("yyyy-MM-dd");

    @ManyToOne(fetch= FetchType.EAGER, targetEntity = City.class, optional=false)
    public City city;

    public VisitedCity() {}

    public VisitedCity(long apiKeyId) {
        super(apiKeyId);
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public long getDayStart() {
        final DateTime dateTime = getDateTime();
        return dateTime.getMillis();
    }

    public long getDayEnd() {
        final DateTime dateTime = getDateTime();
        return dateTime.getMillis()+ DateTimeConstants.MILLIS_PER_DAY;
    }

    private DateTime getDateTime() {
        if (dateTime==null)
            dateTime = formatter.withZone(DateTimeZone.forID(city.geo_timezone)).parseDateTime(date);
        return dateTime;
    }

    @Override
    protected void makeFullTextIndexable() {
        if (this.city!=null)
            this.fullTextDescription = city.geo_name;
    }

    @Override
    public int compareTo(final VisitedCity o) {
        return (int)(start-o.start);
    }
}