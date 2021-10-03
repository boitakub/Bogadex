package fr.boitakub.bgg.client;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

@Xml
public class BggSearchResult {

    @Attribute
    String termsofuse;

    @Attribute
    String total;

    @Element(name = "item")
    public List<Publishable> games;

}
