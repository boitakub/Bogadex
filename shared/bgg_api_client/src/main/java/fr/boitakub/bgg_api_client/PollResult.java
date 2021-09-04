package fr.boitakub.bgg_api_client;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

@Xml
public class PollResult {

    @Attribute
    String numplayers;

    @Element(name = "result")
    List<PollResultValue> results;

}
