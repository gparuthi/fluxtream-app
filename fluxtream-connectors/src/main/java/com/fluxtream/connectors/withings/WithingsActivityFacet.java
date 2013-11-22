package com.fluxtream.connectors.withings;

import javax.persistence.Entity;
import com.fluxtream.connectors.annotations.ObjectTypeSpec;
import com.fluxtream.domain.AbstractLocalTimeFacet;
import org.hibernate.search.annotations.Indexed;

/**
 * User: candide
 * Date: 21/11/13
 * Time: 15:05
 */
@Entity(name="Facet_WithingsActivity")
@ObjectTypeSpec(name = "activity", value = 8, extractor=WithingsFacetExtractor.class, prettyname = "Activity")
@Indexed
public class WithingsActivityFacet extends AbstractLocalTimeFacet {

    public String timezone;
    public int steps;
    public float distance;
    public float calories;
    public float elevation;

    @Override
    protected void makeFullTextIndexable() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}