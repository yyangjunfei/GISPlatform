package cc.wanshan.gisdev.entity;

public class MyException  extends RuntimeException{
    private static final long serialVersionUID = 1L;

    private Integer code;

    public MyException() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
