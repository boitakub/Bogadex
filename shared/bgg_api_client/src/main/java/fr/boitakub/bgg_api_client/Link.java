package fr.boitakub.bgg_api_client;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Xml;

@Xml
public class Link {

    @Attribute
    String id;

    @Attribute
    String type;

    @Attribute
    String value;

    @Attribute
    boolean inbound;

}
