package pcl.bridgebot.chatserverlink.impl;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
import org.junit.Test;

import pcl.bridgebot.chatserverlink.InvalidPackedMessageException;

import static org.junit.Assert.*;

import java.util.Iterator;

public class UTF8MessageSplitterTest {
    @Test
    public void itShouldSplitASmallStringProperly() throws InvalidPackedMessageException {
        UTF8MessageSplitter splitter = new UTF8MessageSplitter(255);
        Iterator<String> result = splitter.splitMessage("Small").iterator();
        assertEquals("Small", result.next());
        assertFalse(result.hasNext());
    }
    @Test
    public void itShouldSplitALargeStringProperly() throws InvalidPackedMessageException {
        UTF8MessageSplitter splitter = new UTF8MessageSplitter(20);
        
        Iterator<String> result = splitter.splitMessage("Large String With More than 20 characters").iterator();
        assertEquals("Large String With", result.next());
        assertEquals("More than 20", result.next());
        assertEquals("characters", result.next());
        assertFalse(result.hasNext());
    }

    @Test
    public void itShouldSplitALargeSingleBlockStringProperly() throws InvalidPackedMessageException {
        UTF8MessageSplitter splitter = new UTF8MessageSplitter(20);
        
        Iterator<String> result = splitter.splitMessage("Large StringWithMorethantwenty characters").iterator();
        assertEquals("Large", result.next());
        assertEquals("StringWithMorethantw", result.next());
        assertEquals("enty characters", result.next());
        assertFalse(result.hasNext());
    }

    @Test
    public void itShouldSplitAnUtf8StringProperly() throws InvalidPackedMessageException {
        UTF8MessageSplitter splitter = new UTF8MessageSplitter(20);
        
        Iterator<String> result = splitter.splitMessage("Large a√√√√√√√√√√√√√√√√ chars").iterator();
        assertEquals("Large", result.next());
        assertEquals("a√√√√√√", result.next());
        assertEquals("√√√√√√", result.next());
        assertEquals("√√√√ chars", result.next());
        assertFalse(result.hasNext());
    }
}