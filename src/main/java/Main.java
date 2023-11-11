import java.io.File;

public class Main {
    private static final String SRC_FOLDER = "data/source";
    private static final String DST_FOLDER = "data/dst";
    private static final int CORES_NUMB = Runtime.getRuntime().availableProcessors();
    private static int newWidth = 300;

    public static void main(String[] args) {
        File srcDir = new File(SRC_FOLDER);
        long start = System.currentTimeMillis();
        File[] files = srcDir.listFiles();

        assert files != null;
        int oneThreadFilesCount = files.length / CORES_NUMB;
        int additionalFiles = files.length % CORES_NUMB;

        for (int i = 0; i < CORES_NUMB; i++) {
            File[] filesToCopy;
            int filesToCopyCount = (i == CORES_NUMB - 1) ?
                    oneThreadFilesCount + additionalFiles : oneThreadFilesCount;

            filesToCopy = new File[filesToCopyCount];
            System.arraycopy(files, i * oneThreadFilesCount, filesToCopy, 0, filesToCopyCount);

            ImageResizer resizer = new ImageResizer(filesToCopy, newWidth, DST_FOLDER, start);
            new Thread(resizer).start();
        }
    }
}
