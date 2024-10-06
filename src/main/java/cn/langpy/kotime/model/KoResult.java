package cn.langpy.kotime.model;

/**
 * zhangchang
 * @param <T>
 */
public class KoResult<T> {
    private Integer state = 1;
    private String message;
    private T content;


    public static KoResult failed(String message) {
        KoResult result = new KoResult();
        result.setState(0);
        result.setMessage(message);
        return result;
    }

    public static KoResult success(Object content) {
        KoResult result = new KoResult();
        result.setState(1);
        result.setMessage("成功");
        result.setContent(content);
        return result;
    }

    public static KoResult success() {
        KoResult result = new KoResult();
        result.setState(1);
        result.setMessage("成功");
        return result;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
