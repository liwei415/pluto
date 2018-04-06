package xyz.inux.pluto.model.enums;

public enum ResultEnum {

    SUCCESS(200, "SUCCESS", "成功"),
    PATH_NOT_FOUND(404, "PATH_NOT_FOUND", "请求地址不存在"),

    /**
     * 参数错误4xxx
     */
    PARAM_ERROR(4000, "PARAM_ERROR", "参数错误"),

    /**
     * 业务错误5xxx
     */
    SYS_ERROR(5000, "SYS_ERROR", "系统异常"),
    NOT_FOUND(5001, "NOT_FOUND", "数据未找到"),
    ADD_FAIL(5002, "ADD_FAIL", "数据添加失败"),

    ASYNC_TASK_ERROR(5999, "ASYNC_TASK_ERROR", "异步执行任务失败"),

    /**
     * 系统错误99xx
     */
    DB_ERROR(9998, "DB_ERROR", "数据库异常"),
    FAILURE(9999, "FAILURE", "业务失败");

    private int code;
    private String message;
    private String desc;

    private ResultEnum(int code, String message, String desc) {
        this.code = code;
        this.message = message;
        this.desc = desc;
    }

    public static ResultEnum valueOf(int code) {
        ResultEnum[] enums = values();
        if (enums == null || enums.length == 0) {
            return FAILURE;
        }

        for (ResultEnum _enu : enums) {
            if (code == _enu.getCode()) {
                return _enu;
            }
        }

        return FAILURE;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

//    @Override
//    public String toString() {
//        try {
//            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
//                    .append("code", code)
//                    .append("message", message)
//                    .append("desc", desc)
//                    .toString();
//        } catch (Exception e) {
//            // NOTICE: 这样做的目的是避免由于toString()的异常导致系统异常终止
//            // 大部分情况下，toString()用在日志输出等调试场景
//            return super.toString();
//        }
//    }


}
