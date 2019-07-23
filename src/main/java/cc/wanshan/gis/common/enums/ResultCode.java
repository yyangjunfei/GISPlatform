package cc.wanshan.gis.common.enums;

public enum ResultCode {

    /**
     * 成功状态码
     */
    SUCCESS(200, "成功"),

    /**
     * 参数错误：10001-19999
     */
    PARAM_NOT_JSON(10001, "参数不是JSON"),

    PARAM_IS_NULL(10002, "参数为空"),

    PARAMS_TYPE_ERROR(10003, "参数类型错误"),

    PARAM_NOT_COMPLETE(10004, "参数缺失"),

    PARAM_TYPE_IS_NULL(10005, "参数类型为空"),

    JWT_TOKEN_NULL(10100, "token不存在"),

    JWT_TOKEN_EXPIRE(10101, "token过期"),

    JWT_TOKEN_FAIL(10102, "token验证不通过"),

    /**
     * 用户错误：20001-29999
     */
    USER_NOT_LOGGED_IN(20001, "用户未登录"),

    USER_LOGIN_ERROR(20002, "账号或密码错误"),

    USER_ACCOUNT_FORBIDDEN(20003, "账号已被禁用"),

    USER_NOT_EXIST(20004, "用户不存在"),

    USER_HAS_EXISTED(20005, "用户已存在"),

    /**
     * 业务错误：30001-39999
     */
    BUSINESS_ERROR(30001, "业务出现问题"),

    SAVE_FAIL(30002, "保存失败"),

    UPDATE_FAIL(30003, "修改失败"),

    DELETE_FAIL(30004, "删除失败"),

    FIND_NULL(30005, "查询结果为空"),

    /**
     * 文件上传
     */
    FILE_PATH_IS_NULL(31001, "文件路径不能为空"),

    UPLOAD_FILE_NULL(31002, "上传为空"),

    UPLOAD_FAIL(31003, "上传失败"),

    DOWNLOAD_FAIL(31004, "下载失败"),

    UPLOAD_FILE_TYPE_FAIL(31005, "上传的资源类型字段不匹配"),

    SHP_PUBLISH_FAIL(31101, "shp数据发布失败"),

    TIF_PUBLISH_FAIL(31102, "tif数据发布失败"),

    DEM_PUBLISH_FAIL(31103, "dem数据转化失败"),

    LAS_PUBLISH_FAIL(31104, "las数据转化失败"),

    OSGB_PUBLISH_FAIL(31105, "osgb数据转化失败"),

    GEOMETRY_TRANSFORM_FAIL(31201, "Geometry 转化失败"),

    /**
     * 系统错误：40001-49999
     */
    SYSTEM_INNER_ERROR(40001, "系统繁忙，请稍后重试"),

    /**
     * 数据错误：50001-599999
     */
    DATA_NOT_FOUND(50001, "数据未找到"),

    DATA_IS_WRONG(50002, "数据有误"),

    DATA_ALREADY_EXISTED(50003, "数据已存在"),

    DATA_NOT_SUPPORT(50004, "数据不支持"),

    /**
     * 接口错误：60001-69999
     */
    INTERFACE_INNER_INVOKE_ERROR(60001, "内部系统接口调用异常"),

    INTERFACE_OUTER_INVOKE_ERROR(60002, "外部系统接口调用异常"),

    INTERFACE_FORBID_VISIT(60003, "该接口禁止访问"),

    INTERFACE_ADDRESS_INVALID(60004, "接口地址无效"),

    INTERFACE_REQUEST_TIMEOUT(60005, "接口请求超时"),

    /**
     * 权限错误：70001-79999
     */
    PERMISSION_NO_ACCESS(70001, "无访问权限");

    private Integer code;
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

    public static String getMessage(String name) {
        for (ResultCode resultCode : ResultCode.values()) {
            if (resultCode.name().equals(name)) {
                return resultCode.message;
            }
        }
        return name;
    }

    public static Integer getCode(String name) {
        for (ResultCode resultCode : ResultCode.values()) {
            if (resultCode.name().equals(name)) {
                return resultCode.code;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
