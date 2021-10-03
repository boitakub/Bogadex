package fr.boitakub.bgg.client;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Xml;

@Xml
public class Name {

    @Attribute
    public String value;

    @Attribute
    public String type;

}
