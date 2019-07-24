package cc.wanshan.gis.controller.layer;

import cc.wanshan.gis.entity.plot.of2d.Layer;
import cc.wanshan.gis.service.layer.export.ExportService;
import cc.wanshan.gis.service.layer.thematic.LayerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Li Cheng
 * @date 2019/6/18 9:28
 */
@Api(value = "FileController", tags = "文件处理模块")
@RestController
@EnableTransactionManagement(proxyTargetClass = true)
@RequestMapping("/file")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Resource(name = "layerServiceImpl")
    private LayerService layerServiceImpl;
    @Resource(name = "exportServiceImpl")
    private ExportService exportServiceImpl;

    @ApiOperation(value = "导出图层", notes = "根据图层Id和文件类型导出图层数据")
    @ApiImplicitParams({@ApiImplicitParam(name = "layerId", value = "图层Id", required = true), @ApiImplicitParam(name = "suffix", value = "导出文件格式，当前支持json，shp，kml格式", required = true)})
    @GetMapping("/exportlayer/{layerId}/{suffix}")
    @ResponseBody
    public void exportLayer(@PathVariable String layerId, @PathVariable String suffix, HttpServletResponse response) throws Exception {
        logger.info("file::layerId = [{}], response = [{}]", layerId, response);
        if (!StringUtils.isNotBlank(layerId)) {
            return;
        }
        Layer layerByLayerId = layerServiceImpl.findLayerByLayerId(layerId);
        if (layerByLayerId == null) {
            return;
        }
        if ("shp".equals(suffix.toLowerCase())) {
            writeShp(layerByLayerId, response);
        }
        if ("json".equals(suffix.toLowerCase())) {
            writeJson(layerByLayerId, response);
        }
        if ("kml".equals(suffix.toLowerCase())) {
            writeKml(layerByLayerId, response);
        }
        if ("gpx".equals(suffix.toLowerCase())) {

        }
    }

    private void writeShp(Layer layer, HttpServletResponse response) throws Exception {
        Boolean aBoolean = exportServiceImpl.writeSHP(layer);
        if (aBoolean) {
            String path = "./download/" + layer.getLayerNameZH() + layer.getLayerName();
            List files = new ArrayList();
            File file1 = new File(path + ".shp");
            File file2 = new File(path + ".dbf");
            File file3 = new File(path + ".fix");
            File file4 = new File(path + ".prj");
            File file5 = new File(path + ".shx");
            files.add(file1);
            files.add(file2);
            files.add(file3);
            files.add(file4);
            files.add(file5);
            downLoadFiles(path, files, response);
        }
    }

    private void writeJson(Layer layer, HttpServletResponse response) throws Exception {
        Boolean aBoolean = exportServiceImpl.writeJSON(layer);
        if (aBoolean) {
            String path = "./download/" + layer.getLayerNameZH() + layer.getLayerName();
            List files = new ArrayList();
            File file1 = new File(path + ".json");
            files.add(file1);
            downLoadFiles(path, files, response);
        }
    }

    private void writeKml(Layer layer, HttpServletResponse response) throws Exception {
        Boolean aBoolean = exportServiceImpl.writeKML(layer);
        if (aBoolean) {
            String path = "./download/" + layer.getLayerNameZH() + layer.getLayerName();
            List files = new ArrayList();
            File file1 = new File(path + ".kml");
            files.add(file1);
            downLoadFiles(path, files, response);
        }
    }

    private static HttpServletResponse downLoadFiles(String path, List<File> files,
                                                     HttpServletResponse response)
            throws Exception {
        logger.info("downLoadFiles::files = [{}], response = [{}]", files, response);
        try {
            // List<File> 作为参数传进来，就是把多个文件的路径放到一个list里面
            // 创建一个临时压缩文件

            // 临时文件可以放在CDEF盘中，但不建议这么做，因为需要先设置磁盘的访问权限，最好是放在服务器上，方法最后有删除临时文件的步骤
            String zipFilename = path + ".zip";
            File file = new File(zipFilename);
            file.createNewFile();
            if (!file.exists()) {
                file.createNewFile();
            }
            response.reset();
            // response.getWriter()
            // 创建文件输出流
            FileOutputStream fous = new FileOutputStream(file);
            ZipOutputStream zipOut = new ZipOutputStream(fous);
            zipFile(files, zipOut);
            zipOut.close();
            fous.close();
            return downloadZip(file, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 把接受的全部文件打成压缩包
     */
    private static void zipFile(List files, ZipOutputStream outputStream) {
        int size = files.size();
        for (int i = 0; i < size; i++) {
            File file = (File) files.get(i);
            zipFile(file, outputStream);
        }
    }

    /**
     * 根据输入的文件与输出流对文件进行打包
     */
    private static void zipFile(File inputFile, ZipOutputStream ouputStream) {
        try {
            if (inputFile.exists()) {
                if (inputFile.isFile()) {
                    FileInputStream IN = new FileInputStream(inputFile);
                    BufferedInputStream bins = new BufferedInputStream(IN, 512);
                    ZipEntry entry = new ZipEntry(inputFile.getName());
                    ouputStream.putNextEntry(entry);
                    // 向压缩文件中输出数据
                    int nNumber;
                    byte[] buffer = new byte[512];
                    while ((nNumber = bins.read(buffer)) != -1) {
                        ouputStream.write(buffer, 0, nNumber);
                    }
                    // 关闭创建的流对象
                    bins.close();
                    IN.close();
                } else {
                    try {
                        File[] files = inputFile.listFiles();
                        for (int i = 0; i < files.length; i++) {
                            zipFile(files[i], ouputStream);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                File f = new File(inputFile.getPath());
                f.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static HttpServletResponse downloadZip(File file, HttpServletResponse response) {
        if (!file.exists()) {
            System.out.println("待压缩的文件目录：" + file + "不存在.");
        } else {
            try {
                // 以流的形式下载文件。
                InputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                // 清空response
                response.reset();
                OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/octet-stream");
                // 如果输出的是中文名的文件，在此处就要用URLEncoder.encode方法进行处理
                response.setHeader("Content-Disposition",
                        "attachment;filename=" + new String(file.getName().getBytes("GB2312"), "ISO8859-1"));
                toClient.write(buffer);
                toClient.flush();
                toClient.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    File f = new File(file.getPath());
                    f.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

}
