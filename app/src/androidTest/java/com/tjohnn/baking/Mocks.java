package com.tjohnn.baking;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tjohnn.baking.data.dto.Recipe;
import com.tjohnn.baking.data.dto.Step;

import java.lang.reflect.Type;
import java.util.List;

public class Mocks {

    public static List<Step> mockStepsId1(){
        String json = "[{\n" +
                "        \"id\": 0,\n" +
                "        \"shortDescription\": \"Recipe Introduction\",\n" +
                "        \"description\": \"Recipe Introduction\",\n" +
                "        \"videoURL\": \"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4\",\n" +
                "        \"thumbnailURL\": \"\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 1,\n" +
                "        \"shortDescription\": \"Starting prep\",\n" +
                "        \"description\": \"1. Preheat the oven to 350\\u00b0F. Butter a 9\\\" deep dish pie pan.\",\n" +
                "        \"videoURL\": \"\",\n" +
                "        \"thumbnailURL\": \"\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 2,\n" +
                "        \"shortDescription\": \"Prep the cookie crust.\",\n" +
                "        \"description\": \"2. Whisk the graham cracker crumbs, 50 grams (1/4 cup) of sugar, and 1/2 teaspoon of salt together in a medium bowl. Pour the melted butter and 1 teaspoon of vanilla into the dry ingredients and stir together until evenly mixed.\",\n" +
                "        \"videoURL\": \"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9a6_2-mix-sugar-crackers-creampie/2-mix-sugar-crackers-creampie.mp4\",\n" +
                "        \"thumbnailURL\": \"\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 3,\n" +
                "        \"shortDescription\": \"Press the crust into baking form.\",\n" +
                "        \"description\": \"3. Press the cookie crumb mixture into the prepared pie pan and bake for 12 minutes. Let crust cool to room temperature.\",\n" +
                "        \"videoURL\": \"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9cb_4-press-crumbs-in-pie-plate-creampie/4-press-crumbs-in-pie-plate-creampie.mp4\",\n" +
                "        \"thumbnailURL\": \"\"\n" +
                "      }]";
        Type type = new TypeToken<List<Step>>(){}.getType();
        return new Gson().fromJson(json, type);
    }

}
