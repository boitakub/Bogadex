package fr.boitakub.bgg_api_client;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Path;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

@Xml
public class UserBoardGameStatistics {

    @Attribute
    int minplayers;

    @Attribute
    int maxplayers;

    @Attribute
    int minplaytime;

    @Attribute
    int maxplaytime;

    @Attribute
    int playingtime;

    @Attribute
    int numowned;

    @Path("rating/usersrated")
    @Attribute(name = "value")
    String usersrated;

    @Path("rating/average")
    @Attribute(name = "value")
    String average;

    @Path("rating/bayesaverage")
    @Attribute(name = "value")
    String bayesaverage;

    @Path("rating/stddev")
    @Attribute(name = "value")
    String stddev;

    @Path("rating/median")
    @Attribute(name = "value")
    String median;

    @Path("rating/ranks")
    @Element
    List<Rank> ranks;

}
