package org.czareg.ui.swing;

import lombok.extern.slf4j.Slf4j;
import org.czareg.piece.Piece;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Slf4j
class ImageCache {

    private static final String PATH_TEMPLATE = "/pieces/default/%s/%s.png";
    private static final String KEY_TEMPLATE = "%s_%s";

    private final java.util.Map<String, Image> cache = new java.util.HashMap<>();

    Image getPieceImage(Piece piece) {
        String color = piece.getPlayer().toString().toLowerCase();
        String type = piece.getClass().getSimpleName().toLowerCase();
        String key = KEY_TEMPLATE.formatted(color, type);

        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        String path = PATH_TEMPLATE.formatted(color, type);
        try {
            InputStream resourceAsStream = Objects.requireNonNull(getClass().getResourceAsStream(path), "cannot find resource at: " + path);
            Image img = ImageIO.read(resourceAsStream);
            cache.put(key, img);
            return img;
        } catch (IOException e) {
            log.error("Missing image for {}: {}", key, path);
            System.exit(1);
            return null;
        }
    }
}
