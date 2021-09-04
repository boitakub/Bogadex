package fr.boitakub.bgg_api_client;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Xml;

@Xml
public class PollResultValue {

    @Attribute
    String value;

    @Attribute
    int numvotes;

}
