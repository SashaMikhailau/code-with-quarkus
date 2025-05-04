package org.habittracker.contentful.richtext.renderers;

import com.contentful.java.cda.rich.CDARichNode;
import jakarta.annotation.Nonnull;
import org.habittracker.contentful.richtext.HtmlContext;
import org.habittracker.contentful.richtext.Processor;

public class DynamicTagRenderer extends TagRenderer {
    private final TagNameProvider provider;

    /**
     * Constructs a new tag renderer
     *
     * @param processor the processor containing all renderer
     * @param provider  a provider for dynamic tag names
     */
    public DynamicTagRenderer(Processor<HtmlContext, String> processor, TagNameProvider provider) {
        super(processor, null);
        this.provider = provider;
    }

    @Nonnull
    @Override protected String startTag(@Nonnull CDARichNode node) {
        return "<" + provider.getTag(node) + ">";
    }

    @Nonnull @Override protected String endTag(@Nonnull CDARichNode node) {
        return "</" + provider.getTag(node) + ">";
    }

    public interface TagNameProvider {
        @Nonnull
        String getTag(@Nonnull CDARichNode node);
    }
}