package fr.boitakub.bgg.client;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Xml;

@Xml
public class PollResultValue {

    @Attribute
    String value;

    @Attribute
    int numvotes;

}
