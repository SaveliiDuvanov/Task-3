import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Main {
    public static void main(String[] args) {
        final int W = 800, H = 600;
        BufferedImage img = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < H; y++)
            for (int x = 0; x < W; x++)
                img.setRGB(x, y, 0xFFFFFF);

        PixelSetter setter = (x, y) -> {
            if (x >= 0 && y >= 0 && x < W && y < H)
                img.setRGB(x, y, 0x000000);
        };
        EllipseRasterizer ell = new EllipseRasterizer(setter);

        ell.drawByRect(50, 50, 200, 100);        // по прямоугольнику
        ell.drawByCenter(400, 150, 80, 30);      // по центру и полуосям
        ell.drawByCorners(300, 300, 100, 200);   // вершины «наоборот»
        ell.drawByCenter(600, 400, 40, 40);      // окружность
        ell.drawByRect(500, 50, 3, 200);         // узкий и высокий
        ell.drawByRect(100, 450, 200, 3);        // широкий и низкий
        ell.drawByRect(700, 500, 1, 1);          // точка
        ell.drawByRect(-30, -20, 100, 60);       // частично за экраном

        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Ellipse");
            f.add(new JComponent() {
                @Override protected void paintComponent(Graphics g) {
                    g.drawImage(img, 0, 0, null);
                }
            });
            f.setSize(W + 16, H + 39);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setVisible(true);
        });
    }
}