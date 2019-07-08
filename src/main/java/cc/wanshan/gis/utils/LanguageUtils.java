package cc.wanshan.gis.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class LanguageUtils {
    /**
     * 根据汉字得到对应的拼音
     *
     * @param source
     * @return
     */
    public static String getPinYin(String source) {

        char[] t1 = source.toCharArray();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        StringBuilder result = new StringBuilder();
        for (char aT1 : t1) {
            if (Character.toString(aT1).matches("[\\u4E00-\\u9FA5]")) {
                try {
                    result.append(PinyinHelper.toHanyuPinyinStringArray(aT1, format)[0]);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                // 非汉字不进行转换，直接添加
                result.append(aT1);
            }
        }
        return result.toString();
    }
}
