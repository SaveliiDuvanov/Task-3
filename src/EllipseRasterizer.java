public class EllipseRasterizer {
    private final PixelSetter setPixel;

    public EllipseRasterizer(PixelSetter setPixel) {
        this.setPixel = setPixel;
    }

    public void drawByRect(int left, int top, int width, int height) {
        if (width <= 0 || height <= 0) return;
        drawByCorners(left, top, left + width - 1, top + height - 1);
    }

    public void drawByCenter(int cx, int cy, int a, int b) {
        if (a < 0 || b < 0) return;
        drawByCorners(cx - a, cy - b, cx + a, cy + b);
    }

    public void drawByCorners(int x0, int y0, int x1, int y1) {
        if (x0 > x1) { int t = x0; x0 = x1; x1 = t; }
        if (y0 > y1) { int t = y0; y0 = y1; y1 = t; }

        long a = x1 - x0;
        long b = y1 - y0;

        if (a == 0 || b == 0) {
            for (int x = x0; x <= x1; x++)
                for (int y = y0; y <= y1; y++)
                    setPixel.set(x, y);
            return;
        }

        long b1 = b & 1;
        long dx = 4 * (1 - a) * b * b;
        long dy = 4 * (b1 + 1) * a * a;
        long err = dx + dy + b1 * a * a;

        long yTop = y0 + (b + 1) / 2;
        long yBot = yTop - b1;
        long xl = x0, xr = x1;

        a = 8 * a * a;
        b1 = 8 * b * b;

        do {
            setPixel.set((int) xr, (int) yTop);
            setPixel.set((int) xl, (int) yTop);
            setPixel.set((int) xl, (int) yBot);
            setPixel.set((int) xr, (int) yBot);

            long e2 = 2 * err;
            if (e2 <= dy) { yTop++; yBot--; dy += a; err += dy; }
            if (e2 >= dx || 2 * err > dy) { xl++; xr--; dx += b1; err += dx; }
        } while (xl <= xr);

        while (yTop - yBot < (y1 - y0)) {
            setPixel.set((int) (xl - 1), (int) yTop);
            setPixel.set((int) (xr + 1), (int) yTop++);
            setPixel.set((int) (xl - 1), (int) yBot);
            setPixel.set((int) (xr + 1), (int) yBot--);
        }
    }
}