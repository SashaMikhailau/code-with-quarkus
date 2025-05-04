package org.habittracker.contentful.richtext;

import com.contentful.java.cda.rich.*;
import jakarta.annotation.Nonnull;
import org.habittracker.contentful.richtext.renderers.DynamicTagRenderer;
import org.habittracker.contentful.richtext.renderers.TagRenderer;
import org.habittracker.contentful.richtext.renderers.TagWithArgumentsRenderer;
import org.habittracker.contentful.richtext.renderers.TextRenderer;

import java.util.Map;

import static org.habittracker.contentful.richtext.renderers.TagWithArgumentsRenderer.mapifyArguments;

class HtmlRendererProvider {

    /**
     * Call this method with a processor to add all default html renderer.
     *
     * @param processor the processor to be filled up with renderer.
     */
    void provide(@Nonnull Processor<HtmlContext, String> processor) {
        processor.addRenderer(
                (context, node) -> node instanceof CDARichText,
                new TextRenderer()
        );
        processor.addRenderer((context, node) -> node instanceof CDARichHorizontalRule,
                (context, node) -> "<hr/>");
        processor.addRenderer(
                (context, node) -> node instanceof CDARichDocument,
                new TagRenderer(processor, "div")
        );
        processor.addRenderer(
                (context, node) -> node instanceof CDARichHyperLink && ((CDARichHyperLink) node).getData() instanceof String,
                new TagWithArgumentsRenderer(
                        processor,
                        "a",
                        (node) -> mapifyArguments("href", (String) ((CDARichHyperLink) node).getData()))
        );
        processor.addRenderer(
                (context, node) -> node instanceof CDARichHyperLink && ((CDARichHyperLink) node).getData() instanceof Map,
                new TagWithArgumentsRenderer(
                        processor,
                        "a",
                        (node) -> mapifyArguments("href", (String) ((Map<?, ?>) ((CDARichHyperLink) node).getData()).get("uri")))
        );
        processor.addRenderer(
                (context, node) -> node instanceof CDARichQuote,
                new TagRenderer(processor, "blockquote")
        );
        processor.addRenderer(
                (context, node) ->
                        node instanceof CDARichHeading
                                && ((CDARichHeading) node).getLevel() >= 1
                                && ((CDARichHeading) node).getLevel() <= 6,
                new DynamicTagRenderer(processor, (node -> "h" + ((CDARichHeading) node).getLevel()))
        );
        processor.addRenderer(
                (context, node) -> node instanceof CDARichOrderedList,
                new TagRenderer(processor, "ol")
        );
        processor.addRenderer(
                (context, node) -> node instanceof CDARichListItem,
                new TagRenderer(processor, "li")
        );
        processor.addRenderer(
                (context, node) -> node instanceof CDARichUnorderedList,
                new TagRenderer(processor, "ul")
        );
        processor.addRenderer(
                (context, node) -> node instanceof CDARichTable,
                new TagRenderer(processor, "table")
        );
        processor.addRenderer(
                (context, node) -> node instanceof CDARichTableHeaderCell,
                new TagRenderer(processor, "th")
        );
        processor.addRenderer(
                (context, node) -> node instanceof CDARichTableRow,
                new TagRenderer(processor, "tr")
        );
        processor.addRenderer(
                (context, node) -> node instanceof CDARichTableCell,
                new TagRenderer(processor, "td")
        );
        // needs to be last but one
        processor.addRenderer(
                (context, node) -> node instanceof CDARichParagraph,
                new TagRenderer(processor, "p")
        );
    }
}