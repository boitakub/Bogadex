package fr.boitakub.bgg_api_client;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Xml;

@Xml
public class Rank {

    @Attribute
    int id;

    @Attribute
    String type;

    @Attribute
    String name;

    @Attribute
    String friendlyname;

    @Attribute
    String value;

    @Attribute
    String bayesaverage;

}
