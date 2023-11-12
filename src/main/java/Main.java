import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final String SRC_FOLDER = "data/source";
    private static final String DST_FOLDER = "data/dst";
    private static final String DST_FOLDER_2 = "data/dstScalr";
    private static final int CORES_NUMB = Runtime.getRuntime().availableProcessors();
    private static int newWidth = 300;

    public static void main(String[] args) {
        File srcDir = new File(SRC_FOLDER);
        long start = System.currentTimeMillis();
        File[] files = srcDir.listFiles();

        assert files != null;
        resizeImages(start, files, new ImageResizer(files, newWidth, DST_FOLDER, start));
        resizeImages(start, files, new ScalrResizer(files, newWidth, DST_FOLDER_2, start));
    }

    private static void resizeImages(long start, File[] files, ResizerStrategy resizer) {
        int oneThreadFilesCount = files.length / CORES_NUMB;
        int additionalFiles = files.length % CORES_NUMB;
        ExecutorService executorService = Executors.newFixedThreadPool(CORES_NUMB);

        for (int i = 0; i < CORES_NUMB; i++) {
            File[] filesToCopy;
            int filesToCopyCount = (i == CORES_NUMB - 1) ?
                    oneThreadFilesCount + additionalFiles : oneThreadFilesCount;

            filesToCopy = new File[filesToCopyCount];
            System.arraycopy(files, i * oneThreadFilesCount, filesToCopy, 0, filesToCopyCount);

            if (resizer instanceof ImageResizer) {
                resizer = new ImageResizer(filesToCopy, newWidth, DST_FOLDER, start);
            } else if (resizer instanceof ScalrResizer) {
                resizer = new ScalrResizer(filesToCopy, newWidth, DST_FOLDER_2, start);
            } else {
                throw new IllegalArgumentException("Unsupported resizer class");
            }

            executorService.submit((Runnable) resizer);
        }
        executorService.shutdown();
    }
}
