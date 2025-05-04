package org.habittracker.contentful.richtext.renderers;

import com.contentful.java.cda.rich.CDARichNode;
import jakarta.annotation.Nonnull;
import org.habittracker.contentful.richtext.HtmlContext;
import org.habittracker.contentful.richtext.Processor;

import java.util.HashMap;
import java.util.Map;

public class TagWithArgumentsRenderer extends TagRenderer {
    private final ArgumentsProvider provider;

    @Nonnull
    public static Map<String, String> mapifyArguments(@Nonnull String... args) {
        final HashMap<String, String> result = new HashMap<>(args.length / 2);
        for (int i = 0; i < args.length; i += 2) {
            final String key = args[i];
            final String value = args[i + 1];
            result.put(key, value);
        }
        return result;
    }

    /**
     * Create a tag renderer.
     *
     * @param processor the processor containing all renderers.
     * @param tag       the tag to be rendered.
     * @param provider  the provider of arguments.
     */
    public TagWithArgumentsRenderer(Processor<HtmlContext, String> processor, String tag, ArgumentsProvider provider) {
        super(processor, tag);
        this.provider = provider;
    }

    /**
     * Overridden to enable arguments parsing.
     *
     * @param node the node to be used by the provider to get the arguments.
     * @return a html tag including arguments.
     */
    @Nonnull @Override
    protected String startTag(@Nonnull CDARichNode node) {
        return "<" + tag + " " + stringifyArgumentMap(provider.provide(node)) + ">";
    }

    String stringifyArgumentMap(Map<String, String> arguments) {
        final StringBuilder builder = new StringBuilder();
        for (final String key : arguments.keySet()) {
            builder.append(key).append("=").append('"').append(arguments.get(key)).append('"');
        }
        return builder.toString();
    }

    /**
     * Create arguments from a node.
     */
    public interface ArgumentsProvider {
        Map<String, String> provide(CDARichNode node);
    }

}