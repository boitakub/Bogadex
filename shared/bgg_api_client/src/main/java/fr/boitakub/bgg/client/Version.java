package fr.boitakub.bgg.client;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Path;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

@Xml(name = "item")
public class Version {

    @Attribute
    int id;

    @Attribute
    String type;

    @PropertyElement
    String thumbnail;

    @PropertyElement
    String image;

    @Path("name")
    @Attribute(name = "value")
    String name;

    @Path("yearpublished")
    @Attribute(name = "value")
    int yearPublished;

    @Element(name = "link")
    List<Link> links;

    @Path("width")
    @Attribute(name = "value")
    double width;

    @Path("length")
    @Attribute(name = "value")
    double length;

    @Path("depth")
    @Attribute(name = "value")
    double depth;

    @Path("weight")
    @Attribute(name = "value")
    double weight;

}
