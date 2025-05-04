package org.habittracker.contentful.beans;

import com.contentful.java.cda.CDAAsset;
import com.contentful.java.cda.rich.CDARichDocument;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.habittracker.contentful.ContentFulUtils;

import static com.contentful.java.cda.TransformQuery.*;

@Data
@NoArgsConstructor
@ContentfulEntryModel("monster")
public class Monster {
    @ContentfulSystemField
    private String id;

    @ContentfulField
    private String name;

    @ContentfulField
    private String description;

    @ContentfulField
    @JsonIgnore
    private CDAAsset idleSpritesheet;

    public String getIdleSpritesheetUrl() {
        return ContentFulUtils.getCDAAssetUrl(idleSpritesheet);
    }
}
