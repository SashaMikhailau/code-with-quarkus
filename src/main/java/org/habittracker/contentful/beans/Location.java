package org.habittracker.contentful.beans;

import com.contentful.java.cda.CDAAsset;
import com.contentful.java.cda.TransformQuery.ContentfulEntryModel;
import com.contentful.java.cda.TransformQuery.ContentfulField;
import com.contentful.java.cda.TransformQuery.ContentfulSystemField;
import com.contentful.java.cda.rich.CDARichDocument;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.habittracker.contentful.ContentFulUtils;
import org.habittracker.jackson.JsonViews;

import java.util.List;

@Data
@NoArgsConstructor
@ContentfulEntryModel(value = "location", additionalModelHints ={BattleGround.class, Monster.class})
public class Location {

    @JsonView({JsonViews.GlobalMapView.class, JsonViews.LocalMapView.class})
    @ContentfulField
    private String title;

    @JsonIgnore
    @ContentfulField
    private CDARichDocument description;

    @ContentfulField
    @JsonIgnore
    private CDAAsset icon;

    @JsonView({JsonViews.GlobalMapView.class})
    @ContentfulField
    private Double x;

    @JsonView({JsonViews.GlobalMapView.class})
    @ContentfulField
    private Double y;

    @JsonView({JsonViews.GlobalMapView.class, JsonViews.LocalMapView.class})
    @ContentfulSystemField
    private String id;

    @ContentfulField
    @JsonIgnore
    private CDAAsset locationMap;

    @JsonView({JsonViews.LocalMapView.class})
    @ContentfulField
    private List<BattleGround> battleGrounds;

    @JsonView({JsonViews.GlobalMapView.class, JsonViews.LocalMapView.class})
    public String getDescriptionHtml() {
        return ContentFulUtils.richTextNodeToHtml(description);
    }

    @JsonView({JsonViews.GlobalMapView.class})
    public String getIconUrl() {
        return ContentFulUtils.getCDAAssetUrl(icon);
    }

    @JsonView({JsonViews.LocalMapView.class})
    public String getLocationMapUrl() {
        return ContentFulUtils.getCDAAssetUrl(locationMap);
    }
}

