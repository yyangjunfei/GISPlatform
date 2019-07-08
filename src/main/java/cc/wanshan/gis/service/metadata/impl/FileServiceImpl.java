package cc.wanshan.gis.service.metadata.impl;

import cc.wanshan.gis.common.enums.ResultCode;
import cc.wanshan.gis.controller.metadata.DataManagementController;
import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.service.metadata.FileService;
import cc.wanshan.gis.utils.ResultUtil;
import cc.wanshan.gis.utils.ShpReader;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileServiceImpl implements FileService {

    private static Logger LOG = LoggerFactory.getLogger(DataManagementController.class);

    @Value("${file.path}")
    private String uploadFilePath;

    /**
     * 上传单文件
     *
     * @param file
     * @return
     */
    @Override
    public Result upload(MultipartFile file) {
        return uploadFile(file, uploadFilePath);
    }

    /**
     * 上传单文件，带文件夹路径
     *
     * @param file
     * @param folderPath
     * @return
     */
    @Override
    public Result upload(MultipartFile file, String folderPath) {
        return uploadFile(file, uploadFilePath + File.separator + folderPath);
    }

    /**
     * 上传多文件
     *
     * @param fileList
     * @return
     */
    @Override
    public Result upload(List<MultipartFile> fileList) {
        ArrayList<Map<String, String>> list = Lists.newArrayList();
        for (MultipartFile file : fileList) {
            // 调用单文件
            Result result = upload(file);
            if (succeed(list, result)) {
                return result;
            }
        }
        return ResultUtil.success(list);
    }

    /**
     * 上传多文件,带文件夹路径
     *
     * @param fileList
     * @param folderPath
     * @return
     */
    @Override
    public Result upload(List<MultipartFile> fileList, String folderPath) {
        List<Map<String, String>> pathList = Lists.newArrayList();

        for (MultipartFile file : fileList) {
            // 调用单文件
            Result result = upload(file, folderPath);
            if (succeed(pathList, result)) {
                return result;
            }
        }

        return ResultUtil.success(pathList);
    }

    /**
     * 删除文件
     *
     * @param path 文件完整路径
     * @return
     */
    @Override
    public String delFile(String path) {
        String resultInfo;
        File file = new File(path);
        if (file.exists()) {
            if (file.delete()) {
                resultInfo = "删除成功";
            } else {
                resultInfo = "删除失败";
            }
        } else {
            resultInfo = "文件不存在！";
        }
        return resultInfo;
    }

    /**
     * 上传文件
     *
     * @param file     文件
     * @param filePath 上传的目标路径
     * @return
     */
    private Result uploadFile(MultipartFile file, String filePath) {
        if (file.isEmpty()) {
            return ResultUtil.error(ResultCode.UPLOAD_FILE_NULL);
        }

        HashMap<String, Object> map = Maps.newHashMap();
        try {
            String filename = file.getOriginalFilename();

            map.put("filename", filename);

            File targetFile = new File(filePath + File.separator + filename);
            // 检测是否存在目录
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            // 将上传文件保存到目标文件目录
            file.transferTo(targetFile);

            map.put("filePath", targetFile.getAbsolutePath());
            return ResultUtil.success(map);

        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtil.error(ResultCode.UPLOAD_FAIL);
        }
    }

    /**
     * 判断是否上传成功
     *
     * @param pathList
     * @param result
     * @return
     */
    private boolean succeed(List<Map<String, String>> pathList, Result result) {
        if (result.getCode() == 0) {
            pathList.add((Map) result.getData());
        } else {
            for (Map map : pathList) {
                delFile(map.get("filePath").toString());
            }
            result.setMsg(result.getMsg() + ",已回滚");
            return true;
        }
        return false;
    }
}
