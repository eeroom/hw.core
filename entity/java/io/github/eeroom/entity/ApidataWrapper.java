package io.github.eeroom.entity;

public class ApidataWrapper {
    public ApidataWrapper(){
        this.code= 200;
    }
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    /**
     * javax.servlet.http.HttpServletResponse.SC*****;
     * @param code
     */
    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    Object data;
    int code;
    String message;
    Object tag;
}
