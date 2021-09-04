package fr.boitakub.bgg_api_client;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

@Xml
public class UserBoardGame {

    @Attribute
    String objecttype;

    @Attribute
    public String objectid;

    @Attribute
    String subtype;

    @Attribute
    String collid;

    @PropertyElement
    public String name;

    @PropertyElement
    public int yearpublished;

    @PropertyElement
    String image;

    @PropertyElement
    public String thumbnail;

    @PropertyElement
    int numplays;

    @Element(name = "status")
    public UserBoardGameStatus status;

    @Element(name = "stats")
    UserBoardGameStatistics stats;

}
