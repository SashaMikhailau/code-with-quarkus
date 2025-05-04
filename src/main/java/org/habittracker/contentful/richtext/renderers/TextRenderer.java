package org.habittracker.contentful.richtext.renderers;

import com.contentful.java.cda.rich.CDARichMark;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.java.cda.rich.CDARichText;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.text.StringEscapeUtils;
import org.habittracker.contentful.richtext.HtmlContext;
import org.habittracker.contentful.richtext.Renderer;

public class TextRenderer implements Renderer<HtmlContext, String> {
    /**
     * Transforms (renders) a {@see CDARichNode} as a string.
     *
     * @param context the generic context this node should be rendered in.
     * @param node    the node to be rendered.
     * @return the text represented by the text node or null if no text node is given.
     */
    @Nullable
    @Override
    public String render(@Nonnull HtmlContext context, @Nonnull CDARichNode node) {
        final CharSequence text = ((CDARichText) node).getText();
        final CharSequence escapedText = StringEscapeUtils.escapeHtml4(text.toString());
        final StringBuilder result = new StringBuilder(escapedText);
        for (final CDARichMark mark : ((CDARichText) node).getMarks()) {

            if (mark instanceof CDARichMark.CDARichMarkUnderline) {
                result.insert(0, "<u>").append("</u>");
            }
            if (mark instanceof CDARichMark.CDARichMarkBold) {
                result.insert(0, "<b>").append("</b>");
            }
            if (mark instanceof CDARichMark.CDARichMarkItalic) {
                result.insert(0, "<i>").append("</i>");
            }
            if (mark instanceof CDARichMark.CDARichMarkSuperscript) {
                result.insert(0, "<sup>").append("</sup>");
            }
            if (mark instanceof CDARichMark.CDARichMarkSubscript) {
                result.insert(0, "<sub>").append("</sub>");
            }
            if (mark instanceof CDARichMark.CDARichMarkCode) {
                result.insert(0, "<code>").append("</code>");
            }
            if (mark instanceof CDARichMark.CDARichMarkCustom) {
                final String tag = ((CDARichMark.CDARichMarkCustom) mark).getType();
                result.insert(0, ">").insert(0, tag).insert(0, "<").append("</").append(tag).append(">");
            }
        }

        return result.toString();
    }
}