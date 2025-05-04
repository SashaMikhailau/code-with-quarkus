package org.habittracker.contentful.richtext;

public class HtmlProcessor extends Processor<HtmlContext, String> {
    /**
     * Construct the processor, including all html renderer.
     */
    public HtmlProcessor() {
        super();

        new HtmlRendererProvider().provide(this);
    }
}