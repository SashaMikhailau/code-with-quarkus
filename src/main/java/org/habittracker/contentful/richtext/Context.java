package org.habittracker.contentful.richtext;

import com.contentful.java.cda.rich.CDARichBlock;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Class for handling context switches while parsing the rich text.
 *
 * @param <T> Parameter class to be used for the path.
 */
public class Context<T> {

    /**
     * This method gets called by the {@see Processor} when a new block node was found and entered.
     *
     * @param block the block node entered
     */
    public void onBlockEntered(@Nonnull CDARichBlock block) {
    }

    /**
     * This method gets called by the {@see Processor} when a block was exited.
     *
     * @param block the block node exited
     */
    public void onBlockExited(@Nonnull CDARichBlock block) {
    }

    /**
     * See implementors and or test cases for usage of this method.
     *
     * @return the accumulated path of this context.
     */
    @Nullable
    public T getPath() {
        return null;
    }
}