package fr.boitakub.bgg.client;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Path;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

@Xml
class Publishable {

    @Attribute
    String type;

    @Attribute
    public int id;

    @Element
    public List<Name> name;

    @Path("yearpublished")
    @Attribute(name = "value")
    public int yearPublished;

}
