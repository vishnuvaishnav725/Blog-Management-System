package com.blog.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.imageio.ImageIO;
import org.springframework.web.multipart.MultipartFile;
import junit.framework.TestCase;

public class ChatImageValidationTest extends TestCase {
    public void testAcceptsRealPngEvenWhenOriginalFilenameIsUntrusted() throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB), "png", output);
        Object result = validate(new TestUpload("../../unsafe.exe", "application/octet-stream", output.toByteArray()));
        assertEquals(".png", readField(result, "extension"));
        assertEquals("image/png", readField(result, "contentType"));
    }

    public void testRejectsTextFileDisguisedAsImage() throws Exception {
        try {
            validate(new TestUpload("fake.png", "image/png", "not an image".getBytes("UTF-8")));
            fail("Expected malformed image to be rejected");
        } catch (InvocationTargetException ex) {
            assertTrue(ex.getCause() instanceof IllegalArgumentException);
        }
    }

    public void testRejectsOversizedImageBeforeReadingIt() throws Exception {
        TestUpload upload = new TestUpload("big.jpg", "image/jpeg", new byte[0]) {
            @Override public long getSize() { return 5L * 1024 * 1024 + 1; }
            @Override public byte[] getBytes() { throw new AssertionError("Oversized content must not be read"); }
        };
        try {
            validate(upload);
            fail("Expected oversized image to be rejected");
        } catch (InvocationTargetException ex) {
            assertTrue(ex.getCause() instanceof IllegalArgumentException);
        }
    }

    private Object validate(MultipartFile file) throws Exception {
        ChatController controller = new ChatController();
        Method method = ChatController.class.getDeclaredMethod("validateImage", MultipartFile.class);
        method.setAccessible(true);
        Object result = method.invoke(controller, file);
        return result;
    }

    private Object readField(Object object, String name) throws Exception {
        java.lang.reflect.Field field = object.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.get(object);
    }

    private static class TestUpload implements MultipartFile {
        private final String name;
        private final String type;
        private final byte[] bytes;
        TestUpload(String name, String type, byte[] bytes) { this.name = name; this.type = type; this.bytes = bytes; }
        public String getName() { return "image"; }
        public String getOriginalFilename() { return name; }
        public String getContentType() { return type; }
        public boolean isEmpty() { return bytes.length == 0; }
        public long getSize() { return bytes.length; }
        public byte[] getBytes() { return bytes; }
        public InputStream getInputStream() { return new ByteArrayInputStream(bytes); }
        public void transferTo(File dest) { throw new UnsupportedOperationException(); }
    }
}
