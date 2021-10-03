package fr.boitakub.bgg.client;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

@Xml
public class BggGameInfoResult {

    @Attribute
    String termsofuse;

    @Element(name = "item")
    public List<BoardGame> games;

}
