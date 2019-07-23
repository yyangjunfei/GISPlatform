package cc.wanshan.gis.common.pojo;

import cc.wanshan.gis.common.enums.ResultCode;
import lombok.Data;

/**
 * 请求返回类型的最外层封装
 */
@Data
public class Result<T> {

    /**
     * 错误码.
     */
    private Integer code;

    /**
     * 提示信息.
     */
    private String msg;

    /**
     * 具体的内容.
     */
    private T data;

    public void setResultCode(ResultCode code) {
        this.code = code.code();
        this.msg = code.message();
    }
}
