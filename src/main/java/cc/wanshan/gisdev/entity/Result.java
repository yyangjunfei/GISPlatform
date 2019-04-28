package cc.wanshan.gisdev.entity;

import cc.wanshan.gisdev.common.enums.ResultCode;

/** 请求返回类型的最外层封装 */
public class Result<T> {

  /** 错误码. */
  private Integer code;

  /** 提示信息. */
  private String msg;

  /** 具体的内容. */
  private T data;

  public Result() {}

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public void setResultCode(ResultCode code) {
    this.code = code.code();
    this.msg = code.message();
  }
}
