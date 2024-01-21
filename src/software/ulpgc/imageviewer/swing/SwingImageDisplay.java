package software.ulpgc.imageviewer.swing;

import software.ulpgc.imageviewer.ImageDisplay;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwingImageDisplay extends JPanel implements ImageDisplay {
    private Shift shift = Shift.Null;
    private Released released = Released.Null;
    private int initShift;
    private List<Paint> paints = new ArrayList<>();
    private final Map<String, BufferedImage> images = new HashMap<>();

    public SwingImageDisplay() {
        loadImages();
        this.addMouseListener(mouseListener());
        this.addMouseMotionListener(mouseMotionListener());
    }

    private MouseListener mouseListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                initShift = e.getX();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                released.offset(e.getX() - initShift);
            }

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) { }
        };
    }

    private MouseMotionListener mouseMotionListener() {
        return new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                shift.offset(e.getX() - initShift);
            }

            @Override
            public void mouseMoved(MouseEvent e) {}
        };
    }

    @Override
    public void paint(String id, int offset) {
        paints.add(new Paint(id, offset));
        repaint();
    }

    @Override
    public void clear() {
        paints.clear();
    }

    @Override
    public void paint(Graphics g) {
        for (Paint paint : paints) {
            BufferedImage image = images.get(paint.id);
            if (image != null) {
                g.drawImage(image, paint.offset, 0, 800, 600, null);
            }
        }
    }

    @Override
    public void on(Shift shift) {
        this.shift = shift != null ? shift : Shift.Null;
    }

    @Override
    public void on(Released released) {
        this.released = released != null ? released : Released.Null;
    }

    private record Paint(String id, int offset) {
    }

    private void loadImages() {

        String[] imagePaths = new String[] {"C:\\Users\\sheil\\Desktop\\image-viewer-2\\shiba.jpg",
                "C:\\Users\\sheil\\Desktop\\image-viewer-2\\shiba2.jpeg",
                "C:\\Users\\sheil\\Desktop\\image-viewer-2\\shiba3.jpg"};

        for (String path : imagePaths) {
            try {
                BufferedImage image = ImageIO.read(new File(path));
                images.put(path, image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
