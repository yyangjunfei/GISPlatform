package cc.wanshan.gis.utils.transform;

/***
 * @author   Yang
 * @date     2019-9-3
 * @version [v1.0]
 * @descriptionweb TODO
 */
public class FileUtils {

    public static String getFileName(String fileName){   //ie : yang.txt 返回 yang
        String prefix =fileName.substring(fileName.lastIndexOf("."));
        int num=prefix.length();//得到后缀名长度
        String fileOtherName=fileName.substring(0, fileName.length()-num);//得到文件名,去掉了后缀
        return fileOtherName;
    }

}
