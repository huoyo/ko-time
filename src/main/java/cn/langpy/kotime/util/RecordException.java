package cn.langpy.kotime.util;

public class RecordException extends RuntimeException {

    private Exception originalException;

    public RecordException(Exception originalException) {
        this.originalException = originalException;
    }

    public Exception getOriginalException() {
        return originalException;
    }

    public void setOriginalException(Exception originalException) {
        this.originalException = originalException;
    }
}
