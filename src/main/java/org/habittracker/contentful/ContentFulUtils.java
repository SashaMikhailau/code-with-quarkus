package org.habittracker.contentful;

import com.contentful.java.cda.CDAAsset;
import com.contentful.java.cda.rich.CDARichDocument;
import lombok.experimental.UtilityClass;
import org.habittracker.contentful.richtext.HtmlContext;
import org.habittracker.contentful.richtext.HtmlProcessor;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@UtilityClass
public class ContentFulUtils {


    private static final HtmlProcessor PROCESSOR = new HtmlProcessor();

    public static @Nullable String getCDAAssetUrl(CDAAsset locationMap) {
        return Optional.ofNullable(locationMap)
                .map(CDAAsset::url)
                .map(url -> "https:" + url)
                .orElse(null);
    }

    public static String richTextNodeToHtml(CDARichDocument richText){
        final HtmlContext context = new HtmlContext();
        return PROCESSOR.process(context, richText);
    }


}
