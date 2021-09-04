package fr.boitakub.bgg_api_client;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

@Xml
public class UserCollection {

    @Attribute
    String termsofuse;

    @Attribute
    String totalitems;

    @Attribute
    String pubdate;

    @Element(name = "item")
    public List<UserBoardGame> games;

}
