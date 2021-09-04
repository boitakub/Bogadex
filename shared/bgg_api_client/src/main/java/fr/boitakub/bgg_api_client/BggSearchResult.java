package fr.boitakub.bgg_api_client;

import com.tickaroo.tikxml.annotation.*;

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
