package fr.boitakub.bgg_api_client;

import java.util.List;

public class BoardGameHelper {

    public static String nameChooser(List<Name> nameList) {
        for (Name name : nameList) {
            if (name.type.contentEquals("primary")) {
                return name.value;
            }
        }
        if (!nameList.isEmpty()) {
            return nameList.get(0).value;
        }
        return "No title found";
    }

}
