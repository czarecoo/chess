package org.czareg.ui.swing;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.czareg.piece.Piece;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@UtilityClass
class ImageCache {

    private static final String PATH_TEMPLATE = "/pieces/default/%s/%s.png";
    private static final String KEY_TEMPLATE = "%s_%s";

    private static final Map<String, Image> cache = new HashMap<>();

    static Image getPieceImage(Piece piece) {
        String color = piece.getPlayer().toString();
        String type = piece.getClass().getSimpleName();
        return getPieceImage(color, type);
    }

    static Image getPieceImage(String player, String pieceName) {
        player = player.toLowerCase();
        pieceName = pieceName.toLowerCase();
        String key = KEY_TEMPLATE.formatted(player, pieceName);

        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        String path = PATH_TEMPLATE.formatted(player, pieceName);
        try {
            InputStream resourceAsStream = Objects.requireNonNull(ImageCache.class.getResourceAsStream(path), "cannot find resource at: " + path);
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
