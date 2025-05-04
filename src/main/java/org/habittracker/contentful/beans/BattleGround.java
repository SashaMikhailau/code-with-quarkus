package org.habittracker.contentful.beans;

import com.contentful.java.cda.CDAAsset;
import com.contentful.java.cda.TransformQuery.ContentfulEntryModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.habittracker.contentful.ContentFulUtils;
import org.habittracker.jackson.JsonViews;

import java.util.List;

import static com.contentful.java.cda.TransformQuery.ContentfulField;
import static com.contentful.java.cda.TransformQuery.ContentfulSystemField;

@Data
@NoArgsConstructor
@ContentfulEntryModel(value = "battleGround", additionalModelHints = {Monster.class})
public class BattleGround {

    @JsonView({JsonViews.LocalMapView.class, JsonViews.BattleGroundView.class})
    @ContentfulSystemField
    private String id;

    @JsonView({JsonViews.LocalMapView.class, JsonViews.BattleGroundView.class})
    @ContentfulField
    private String title;

    @ContentfulField
    @JsonIgnore
    private CDAAsset background;

    @JsonView({JsonViews.BattleGroundView.class})
    @ContentfulField
    private List<Monster> monsters;

    @JsonView({JsonViews.BattleGroundView.class})
    public String getBackgroundUrl() {
        return ContentFulUtils.getCDAAssetUrl(background);
    }

}
