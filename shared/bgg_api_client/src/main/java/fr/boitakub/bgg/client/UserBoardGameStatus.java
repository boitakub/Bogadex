package fr.boitakub.bgg.client;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Xml;

@Xml
public class UserBoardGameStatus {

    @Attribute
    String lastmodified;

    @Attribute
    public int own;

    @Attribute
    public int prevowned;

    @Attribute
    public int fortrade;

    @Attribute
    public int want;

    @Attribute
    public int wanttoplay;

    @Attribute
    public int wanttobuy;

    @Attribute
    public int wishlist;

    @Attribute
    public int wishlistpriority;

    @Attribute
    public int preordered;

}
