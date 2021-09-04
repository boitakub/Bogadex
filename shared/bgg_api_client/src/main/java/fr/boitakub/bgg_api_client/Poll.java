package fr.boitakub.bgg_api_client;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Path;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

@Xml
public class Poll {

    @Attribute
    String name;

    @Attribute
    String title;

    @Attribute
    int totalvotes;

    @Element(name = "results")
    List<PollResult> results;

}
