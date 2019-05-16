package cc.wanshan.gisdev.utils;

import com.csvreader.CsvReader;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileReader {

    /**
     * 通过excel文件路劲获取到值存到map中（一行K-V数据形式）
     *
     * @param filePath 文件excel路劲
     * @throws IOException
     */
    public static Map<String, String> readExcel(String filePath) throws IOException {
        HashMap<String, String> map = Maps.newHashMap();
        Workbook workbook = WorkbookFactory.create(new File(filePath));

        DataFormatter dataFormatter = new DataFormatter();
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            String K = dataFormatter.formatCellValue(row.getCell(0));
            String V = dataFormatter.formatCellValue(row.getCell(1));
            map.put(K, V);
        }
        return map;
    }

    /**
     * 读取csv文件的数据
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static List<String[]> readCsv(String filePath) throws IOException {
        ArrayList<String[]> csvList = Lists.newArrayList();
        CsvReader csvReader = new CsvReader(filePath, ',', Charset.forName("GBK"));
        csvReader.readHeaders(); // 跳过表头
        while (csvReader.readRecord()) {
            csvList.add(csvReader.getValues());
        }
        csvReader.close();
        return csvList;
    }

    /**
     * 读取xml文件信息
     *
     * @param filePath xml文件路劲
     * @return
     * @throws DocumentException
     */
    public static Map<String, String> readXml(String filePath) throws DocumentException {
        HashMap<String, String> map = Maps.newHashMap();
        SAXReader saxReader = new SAXReader();
        // 读取xml文档
        Document document = saxReader.read(new File(filePath));
        // 获取根元素
        Element root = document.getRootElement();

        List<Element> elements = root.elements();
        for (Element element : elements) {
            map.put(element.getName(), element.getText());
        }
        return map;
    }

    /**
     * 判断是否为csv文件
     */
    private boolean isCsv(String filePath) {
        return filePath.matches("^.+\\.(?i)(csv)$");
    }

    /**
     * 判断是否为xlsx文件
     */
    private boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

    /**
     * 判断是否为xls文件
     */
    private boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }
}
