package com.blog.controller;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.blog.daoimpl.ChatDaoImpl;
import com.blog.daoimpl.UserInfoDaoImpl;
import com.blog.pojo.ChatMessage;
import com.blog.pojo.ConversationSummary;
import com.blog.pojo.UserInfo;

@Controller
public class ChatController {
    private static final long MAX_IMAGE_BYTES = 5L * 1024 * 1024;
    private static final long MAX_IMAGE_PIXELS = 20_000_000L;
    private static final int MAX_MESSAGE_CHARS = 2000;
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList("image/jpeg", "image/png", "image/webp");

    @Autowired private ChatDaoImpl chatDao;
    @Autowired private UserInfoDaoImpl userDao;

    @GetMapping("/chat")
    public String chat(@RequestParam(value = "peerId", required = false) Integer peerId,
                       @RequestParam(value = "q", required = false) String search,
                       HttpSession session, Model model) {
        UserInfo me = signedInUser(session);
        if (me == null) return "redirect:/login";
        me = userDao.getUserById(me.getId());
        if (me == null || me.isBlocked()) { session.invalidate(); return "redirect:/login"; }
        List<ConversationSummary> conversations = conversations(me.getId());
        List<UserInfo> users = searchUsers(me.getId(), search);
        UserInfo peer = peerId == null ? null : validPeer(me.getId(), peerId);
        if (peerId != null && peer == null) {
            model.addAttribute("error", "This conversation is unavailable.");
        }
        if (peer == null && !conversations.isEmpty()) peer = conversations.get(0).getUser();
        model.addAttribute("me", me);
        model.addAttribute("csrfToken", csrfToken(session));
        model.addAttribute("peer", peer);
        model.addAttribute("conversations", conversations);
        model.addAttribute("searchUsers", users);
        model.addAttribute("search", search == null ? "" : search.trim());
        if (peer != null) {
            chatDao.markConversationSeen(me.getId(), peer.getId());
            model.addAttribute("messages", chatDao.getConversation(me.getId(), peer.getId()));
        }
        return "chat";
    }

    @GetMapping("/chat/threadFragment")
    public String threadFragment(@RequestParam("peerId") int peerId, HttpSession session, Model model) {
        UserInfo me = signedInUser(session);
        UserInfo peer = me == null ? null : validPeer(me.getId(), peerId);
        if (me == null || peer == null) return "chatUnavailable";
        chatDao.markConversationSeen(me.getId(), peer.getId());
        model.addAttribute("me", me);
        model.addAttribute("messages", chatDao.getConversation(me.getId(), peer.getId()));
        return "chatMessages";
    }

    @GetMapping("/chat/conversationsFragment")
    public String conversationsFragment(@RequestParam(value = "peerId", required = false) Integer peerId,
                                        HttpSession session, Model model) {
        UserInfo me = signedInUser(session);
        if (me == null) return "chatUnavailable";
        model.addAttribute("conversations", conversations(me.getId()));
        model.addAttribute("activePeerId", peerId);
        return "chatConversations";
    }

    @PostMapping("/chat/send")
    public String send(@RequestParam("peerId") int peerId,
                       @RequestParam(value = "message", required = false) String text,
                       @RequestParam(value = "image", required = false) MultipartFile image,
                       @RequestParam("clientToken") String clientToken,
                       @RequestParam("csrfToken") String submittedCsrfToken,
                       HttpSession session, RedirectAttributes flash) {
        UserInfo me = signedInUser(session);
        if (me == null) return "redirect:/login";
        String expectedCsrfToken = csrfToken(session);
        if (submittedCsrfToken == null || !MessageDigest.isEqual(
                expectedCsrfToken.getBytes(StandardCharsets.UTF_8), submittedCsrfToken.getBytes(StandardCharsets.UTF_8))) {
            flash.addFlashAttribute("error", "Your session expired. Refresh the chat and try again.");
            return "redirect:/chat?peerId=" + peerId;
        }
        UserInfo sender = userDao.getUserById(me.getId());
        UserInfo receiver = validPeer(me.getId(), peerId);
        if (sender == null || sender.isBlocked() || receiver == null) {
            flash.addFlashAttribute("error", "This user is not available for chat.");
            return "redirect:/chat";
        }
        String body = text == null ? "" : text.trim();
        if (body.length() > MAX_MESSAGE_CHARS) {
            flash.addFlashAttribute("error", "Message must be 2,000 characters or fewer.");
            return "redirect:/chat?peerId=" + peerId;
        }
        if (body.isEmpty() && (image == null || image.isEmpty())) {
            flash.addFlashAttribute("error", "Write a message or choose an image.");
            return "redirect:/chat?peerId=" + peerId;
        }
        if (clientToken == null || !clientToken.matches("^[0-9a-fA-F-]{36}$")) {
            flash.addFlashAttribute("error", "Please retry sending your message.");
            return "redirect:/chat?peerId=" + peerId;
        }
        if (chatDao.findByClientToken(sender.getId(), clientToken) != null) {
            return "redirect:/chat?peerId=" + peerId;
        }

        Path savedImage = null;
        try {
            String filename = null;
            if (image != null && !image.isEmpty()) {
                ValidatedImage validated = validateImage(image);
                Path directory = Paths.get(System.getProperty("user.home"), ".blogger", "chat-images").toAbsolutePath().normalize();
                Files.createDirectories(directory);
                filename = UUID.randomUUID().toString() + validated.extension;
                savedImage = directory.resolve(filename).normalize();
                if (!savedImage.getParent().equals(directory)) throw new IOException("Invalid destination");
                Files.write(savedImage, validated.bytes);
            }
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setSender(sender);
            chatMessage.setReceiver(receiver);
            chatMessage.setMessage(body.isEmpty() ? null : body);
            chatMessage.setImagePath(filename);
            chatMessage.setMessageType(filename == null ? "TEXT" : (body.isEmpty() ? "IMAGE" : "TEXT_IMAGE"));
            chatMessage.setClientToken(clientToken);
            chatMessage.setSeen(false);
            chatMessage.setCreatedAt(new Date());
            chatDao.save(chatMessage);
        } catch (IllegalArgumentException ex) {
            deleteQuietly(savedImage);
            flash.addFlashAttribute("error", ex.getMessage());
        } catch (Exception ex) {
            deleteQuietly(savedImage);
            flash.addFlashAttribute("error", "Message could not be sent. Please try again.");
        }
        return "redirect:/chat?peerId=" + peerId;
    }

    @GetMapping("/chat/image/{messageId}")
    public ResponseEntity<byte[]> chatImage(@PathVariable int messageId, HttpSession session) {
        UserInfo me = signedInUser(session);
        if (me == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        ChatMessage message = chatDao.getMessage(messageId);
        if (message == null || message.getImagePath() == null
                || (message.getSender().getId() != me.getId() && message.getReceiver().getId() != me.getId())) {
            return ResponseEntity.notFound().build();
        }
        String filename = message.getImagePath();
        if (!filename.matches("^[0-9a-fA-F-]{36}\\.(jpg|png|webp)$")) return ResponseEntity.notFound().build();
        Path directory = Paths.get(System.getProperty("user.home"), ".blogger", "chat-images").toAbsolutePath().normalize();
        Path file = directory.resolve(filename).normalize();
        try {
            if (!file.getParent().equals(directory) || !Files.isRegularFile(file)) return ResponseEntity.notFound().build();
            String type = filename.endsWith(".png") ? "image/png" : filename.endsWith(".webp") ? "image/webp" : "image/jpeg";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(type));
            headers.setCacheControl(CacheControl.noStore().cachePrivate());
            headers.set("X-Content-Type-Options", "nosniff");
            return new ResponseEntity<>(Files.readAllBytes(file), headers, HttpStatus.OK);
        } catch (IOException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    private List<ConversationSummary> conversations(int userId) {
        Map<Integer, ChatMessage> latestByPeer = new LinkedHashMap<>();
        for (ChatMessage message : chatDao.getRecentMessages(userId)) {
            int peerId = message.getSender().getId() == userId ? message.getReceiver().getId() : message.getSender().getId();
            if (!latestByPeer.containsKey(peerId)) latestByPeer.put(peerId, message);
        }
        List<ConversationSummary> result = new ArrayList<>();
        for (Map.Entry<Integer, ChatMessage> entry : latestByPeer.entrySet()) {
            UserInfo peer = userDao.getUserById(entry.getKey());
            if (peer != null && !peer.isBlocked()) result.add(new ConversationSummary(peer, entry.getValue(), chatDao.getUnreadCount(userId, peer.getId())));
        }
        return result;
    }

    private List<UserInfo> searchUsers(int userId, String query) {
        String q = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
        List<UserInfo> result = new ArrayList<>();
        if (q.isEmpty()) return result;
        List<UserInfo> users = userDao.getAllUser();
        if (users == null) return result;
        for (UserInfo user : users) {
            String username = user.getUsername() == null ? "" : user.getUsername();
            if (user.getId() != userId && !user.isBlocked() && username.toLowerCase(Locale.ROOT).contains(q)) result.add(user);
        }
        return result;
    }

    private UserInfo validPeer(int currentId, int peerId) {
        if (peerId <= 0 || peerId == currentId) return null;
        UserInfo peer = userDao.getUserById(peerId);
        return peer == null || peer.isBlocked() ? null : peer;
    }

    private UserInfo signedInUser(HttpSession session) {
        if (session == null) return null;
        Object value = session.getAttribute("userId");
        UserInfo fromSession = (UserInfo) session.getAttribute("currentUser");
        if (value instanceof Integer && fromSession != null && ((Integer) value) == fromSession.getId()) return fromSession;
        return null;
    }

    private String csrfToken(HttpSession session) {
        String token = (String) session.getAttribute("chatCsrfToken");
        if (token == null) {
            token = UUID.randomUUID().toString();
            session.setAttribute("chatCsrfToken", token);
        }
        return token;
    }

    private ValidatedImage validateImage(MultipartFile file) throws IOException {
        if (file.getSize() > MAX_IMAGE_BYTES) throw new IllegalArgumentException("Chat images must be 5 MB or smaller.");
        byte[] bytes = file.getBytes();
        if (bytes.length < 12) throw new IllegalArgumentException("That image file is invalid.");
        String ext; String type;
        if ((bytes[0] & 0xff) == 0xff && (bytes[1] & 0xff) == 0xd8 && (bytes[2] & 0xff) == 0xff) {
            ext = ".jpg"; type = "image/jpeg";
        } else if (bytes.length >= 8 && (bytes[0] & 0xff) == 0x89 && bytes[1] == 0x50 && bytes[2] == 0x4e && bytes[3] == 0x47
                && bytes[4] == 0x0d && bytes[5] == 0x0a && bytes[6] == 0x1a && bytes[7] == 0x0a) {
            ext = ".png"; type = "image/png";
        } else if (bytes[0] == 'R' && bytes[1] == 'I' && bytes[2] == 'F' && bytes[3] == 'F'
                && bytes[8] == 'W' && bytes[9] == 'E' && bytes[10] == 'B' && bytes[11] == 'P') {
            ext = ".webp"; type = "image/webp";
        } else {
            throw new IllegalArgumentException("Choose a valid JPG, PNG, or WEBP image.");
        }
        if (!ALLOWED_IMAGE_TYPES.contains(type)) throw new IllegalArgumentException("Choose a valid JPG, PNG, or WEBP image.");
        if ("image/webp".equals(type)) {
            validateWebp(bytes);
        } else {
            validateRasterImage(bytes);
        }
        return new ValidatedImage(bytes, ext, type);
    }

    private void validateWebp(byte[] bytes) {
        long declaredLength = ((long) bytes[4] & 0xff) | (((long) bytes[5] & 0xff) << 8)
                | (((long) bytes[6] & 0xff) << 16) | (((long) bytes[7] & 0xff) << 24);
        if (declaredLength + 8 != bytes.length) throw new IllegalArgumentException("That WEBP image is invalid.");
        String chunk = new String(bytes, 12, 4, StandardCharsets.US_ASCII);
        long chunkLength = ((long) bytes[16] & 0xff) | (((long) bytes[17] & 0xff) << 8)
                | (((long) bytes[18] & 0xff) << 16) | (((long) bytes[19] & 0xff) << 24);
        if (chunkLength + 20 > bytes.length) throw new IllegalArgumentException("That WEBP image is invalid.");
        long width; long height;
        if ("VP8X".equals(chunk) && chunkLength >= 10) {
            width = 1 + (((long) bytes[24] & 0xff) | (((long) bytes[25] & 0xff) << 8) | (((long) bytes[26] & 0xff) << 16));
            height = 1 + (((long) bytes[27] & 0xff) | (((long) bytes[28] & 0xff) << 8) | (((long) bytes[29] & 0xff) << 16));
        } else if ("VP8L".equals(chunk) && chunkLength >= 5 && (bytes[20] & 0xff) == 0x2f) {
            width = 1 + ((bytes[21] & 0xff) | ((bytes[22] & 0x3f) << 8));
            height = 1 + (((bytes[22] & 0xc0) >> 6) | ((bytes[23] & 0xff) << 2) | ((bytes[24] & 0x0f) << 10));
        } else if ("VP8 ".equals(chunk) && chunkLength >= 10 && (bytes[23] & 0xff) == 0x9d
                && (bytes[24] & 0xff) == 0x01 && (bytes[25] & 0xff) == 0x2a) {
            width = (((bytes[26] & 0xff) << 8) | (bytes[27] & 0xff)) & 0x3fff;
            height = (((bytes[28] & 0xff) << 8) | (bytes[29] & 0xff)) & 0x3fff;
        } else {
            throw new IllegalArgumentException("That WEBP image is invalid or unsupported.");
        }
        if (width <= 0 || height <= 0 || width * height > MAX_IMAGE_PIXELS) {
            throw new IllegalArgumentException("That image is invalid or has excessive dimensions.");
        }
    }

    private void validateRasterImage(byte[] bytes) throws IOException {
        try (ImageInputStream stream = ImageIO.createImageInputStream(new ByteArrayInputStream(bytes))) {
            if (stream == null) throw new IllegalArgumentException("That image is invalid.");
            Iterator<ImageReader> readers = ImageIO.getImageReaders(stream);
            if (!readers.hasNext()) throw new IllegalArgumentException("That image is invalid.");
            ImageReader reader = readers.next();
            try {
                reader.setInput(stream, true, true);
                int width = reader.getWidth(0);
                int height = reader.getHeight(0);
                if (width <= 0 || height <= 0 || (long) width * height > MAX_IMAGE_PIXELS || reader.read(0) == null) {
                    throw new IllegalArgumentException("That image is invalid or has excessive dimensions.");
                }
            } finally {
                reader.dispose();
            }
        }
    }

    private void deleteQuietly(Path path) {
        if (path != null) try { Files.deleteIfExists(path); } catch (IOException ignored) { }
    }

    private static final class ValidatedImage {
        private final byte[] bytes; private final String extension; private final String contentType;
        private ValidatedImage(byte[] bytes, String extension, String contentType) {
            this.bytes = bytes; this.extension = extension; this.contentType = contentType;
        }
    }
}
