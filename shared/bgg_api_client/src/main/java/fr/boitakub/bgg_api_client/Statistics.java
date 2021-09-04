package fr.boitakub.bgg_api_client;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Path;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

@Xml
public class Statistics {

    @Attribute
    int page;

    @Path("ratings/usersrated")
    @Attribute(name = "value")
    public String usersrated;

    @Path("ratings/average")
    @Attribute(name = "value")
    public String average;

    @Path("ratings/bayesaverage")
    @Attribute(name = "value")
    public String bayesaverage;

    @Path("ratings/stddev")
    @Attribute(name = "value")
    String stddev;

    @Path("ratings/median")
    @Attribute(name = "value")
    String median;

    @Path("ratings/owned")
    @Attribute(name = "value")
    public int owned;

    @Path("ratings/trading")
    @Attribute(name = "value")
    public int trading;

    @Path("ratings/wanting")
    @Attribute(name = "value")
    public int wanting;

    @Path("ratings/wishing")
    @Attribute(name = "value")
    public int wishing;

    @Path("ratings/numcomments")
    @Attribute(name = "value")
    int numcomments;

    @Path("ratings/numweights")
    @Attribute(name = "value")
    public int numweights;

    @Path("ratings/averageweight")
    @Attribute(name = "value")
    public double averageweight;

    @Path("ratings/ranks")
    @Element
    List<Rank> ranks;

}
