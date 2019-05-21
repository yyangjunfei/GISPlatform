package cc.wanshan.gis.utils;

import com.google.common.collect.Lists;
import org.apache.poi.util.IOUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileUtil {

    /**
     * 获取相关后缀的文件路劲
     *
     * @param file   文件
     * @param suffix 文件后缀
     * @return
     */
    public static List<String> fileList(File file, String suffix) {

        File[] files = file.listFiles();
        List<String> list = Lists.newArrayList();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    fileList(f, suffix);
                }
                if (f.getName().endsWith(suffix)) {
                    list.add(f.getPath());
                }
            }
        }
        return list;
    }

    /**
     * 递归删除文件夹或文件
     */
    public static void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                deleteFile(f);
            }
        }
        file.delete();
    }

    /**
     * zip文件解压
     *
     * @param zipFilePath 源文件路劲
     * @param destDirPath 目标文件路劲
     */
    public static void unZip(String zipFilePath, String destDirPath) {
        ZipInputStream zis = null;
        BufferedOutputStream bos = null;
        try {
            zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFilePath)));

            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                // 获取目标文件的绝对路劲
                String filePath = destDirPath + File.separator + zipEntry.getName();
                if (zipEntry.isDirectory()) {
                    new File(filePath).mkdirs();
                } else {
                    // 创建父目录
                    File entryFile = new File(filePath);
                    if (!entryFile.getParentFile().exists()) {
                        if (!entryFile.getParentFile().mkdirs()) {
                            throw new FileNotFoundException("zip entry mkdirs failed");
                        }
                    }
                    bos = new BufferedOutputStream(new FileOutputStream(entryFile));
                    byte[] buffer = new byte[1024 << 2];
                    int len;
                    while ((len = zis.read(buffer)) != -1) {
                        bos.write(buffer, 0, len);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(zis);
            IOUtils.closeQuietly(bos);
        }
    }

    /**
     * 文件或者文件夹压缩
     *
     * @param srcDirPath  源文件夹或文件路劲
     * @param zipFilePath 目标压缩zip文件路劲
     */
    public static void toZip(String srcDirPath, String zipFilePath) {

        ZipOutputStream zos = null;
        try {
            File file = new File(srcDirPath);
            zos = new ZipOutputStream(new FileOutputStream(new File(zipFilePath)));
            File[] files = file.listFiles();
            for (File f : files) {
                // TODO 待做
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void recursionZip(ZipOutputStream zipOut, File file, String baseDir)
            throws Exception {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File fileSec : files) {
                recursionZip(zipOut, fileSec, baseDir + file.getName() + File.separator);
            }
        } else {
            byte[] buf = new byte[1024];
            InputStream input = new FileInputStream(file);
            zipOut.putNextEntry(new ZipEntry(baseDir + file.getName()));
            int len;
            while ((len = input.read(buf)) != -1) {
                zipOut.write(buf, 0, len);
            }
            input.close();
        }
    }
}
