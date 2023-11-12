import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ScalrResizer implements Runnable, ResizerStrategy {
    private File[] files;
    private int newWidth;
    private String dstFolder;
    private long start;

    public ScalrResizer(File[] files, int newWidth, String dstFolder, long start) {
        this.files = files;
        this.newWidth = newWidth;
        this.dstFolder = dstFolder;
        this.start = start;
    }

    @Override
    public void run() {
        resize();
        System.out.println("Finished " + Thread.currentThread().getName()
                + " after start: " + (System.currentTimeMillis() - start) + "ms");
    }

    @Override
    public void resize() {
        try {
            for (File file : files) {
                BufferedImage image = ImageIO.read(file);
                if (image == null) {
                    continue;
                }

                int newHeight = (int) Math.round(image.getHeight() / (image.getWidth() / (double) newWidth));
                BufferedImage newImage = Scalr
                        .resize(image, Scalr.Method.ULTRA_QUALITY ,newWidth, newHeight, Scalr.OP_ANTIALIAS);

                File newFile = new File(dstFolder + "/" + file.getName());
                ImageIO.write(newImage, "jpg", newFile);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
