package pcl.bridgebot.chatserverlink.impl;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
import org.junit.Test;

import pcl.bridgebot.chatserverlink.InvalidPackedMessageException;

import static org.junit.Assert.*;

public class PackedMessageDataTest {
    @Test
    public void itShouldWrapAndUnwrapAValidMessageProperly() throws InvalidPackedMessageException {
        PackedMessageData originalData = new PackedMessageData("chatroom", 0x10, "userNickname", "message");
        byte[] packedMessage = originalData.getPackedMessage();
        PackedMessageData unpackedData = PackedMessageData.getDataFromPackedMessage(packedMessage,
                packedMessage.length);
        assertEquals(originalData.getChatroom(), unpackedData.getChatroom());
        assertEquals(originalData.getMessage(), unpackedData.getMessage());
        assertEquals(originalData.getMessageType(), unpackedData.getMessageType());
        assertEquals(originalData.getUserId(), unpackedData.getUserId());
        assertEquals(originalData.getUserNickname(), unpackedData.getUserNickname());
    }

    @Test
    public void itShouldWrapAndUnwrapAnUTFMessageProperly() throws InvalidPackedMessageException {
        PackedMessageData originalData = new PackedMessageData("√chat", 0x10, "∀a∈B", "∑a");
        byte[] packedMessage = originalData.getPackedMessage();
        PackedMessageData unpackedData = PackedMessageData.getDataFromPackedMessage(packedMessage,
                packedMessage.length);
        assertEquals(originalData.getChatroom(), unpackedData.getChatroom());
        assertEquals(originalData.getMessage(), unpackedData.getMessage());
        assertEquals(originalData.getMessageType(), unpackedData.getMessageType());
        assertEquals(originalData.getUserId(), unpackedData.getUserId());
        assertEquals(originalData.getUserNickname(), unpackedData.getUserNickname());
    }

    @Test
    public void itShouldWrapAndUnwrapAValidEmptyMessageProperly() throws InvalidPackedMessageException {
        PackedMessageData originalData = new PackedMessageData("", 0x00, "", "");
        byte[] packedMessage = originalData.getPackedMessage();
        PackedMessageData unpackedData = PackedMessageData.getDataFromPackedMessage(packedMessage,
                packedMessage.length);
        assertEquals(originalData.getChatroom(), unpackedData.getChatroom());
        assertEquals(originalData.getMessage(), unpackedData.getMessage());
        assertEquals(originalData.getMessageType(), unpackedData.getMessageType());
        assertEquals(originalData.getUserId(), unpackedData.getUserId());
        assertEquals(originalData.getUserNickname(), unpackedData.getUserNickname());
    }

    @Test
    public void itShouldGracefullyHandleAnEmptyBuffer() {
        try {
            PackedMessageData.getDataFromPackedMessage(new byte[] {}, 0);
            fail("Exception not thrown");
        } catch (InvalidPackedMessageException e) {
            assertTrue(true);
        }
    }

    @Test
    public void itShouldGracefullyHandleMessagesWithMismatchedAnnouncedSizes() {
        try {
            PackedMessageData.getDataFromPackedMessage(new byte[] { 0x00, 0x01, 0x64, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }, 1);
            fail("Exception not thrown");
        } catch (InvalidPackedMessageException e) {
            assertTrue(true);
        }
    }

    @Test
    public void itShouldGracefullyHandleMessagesWithInvalidStrings() {
        try {
            PackedMessageData.getDataFromPackedMessage(new byte[] { 0x00, 0x01, 0x64, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }, 0);
            fail("Exception not thrown");
        } catch (InvalidPackedMessageException e) {
            assertTrue(true);
        }
    }
}