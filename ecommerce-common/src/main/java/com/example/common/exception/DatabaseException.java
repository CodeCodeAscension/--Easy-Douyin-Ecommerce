package com.example.common.exception;

public class DatabaseException extends SystemException {

    public DatabaseException() {
        super("操作数据库时发生了错误");
    }

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseException(Throwable cause) {
        super(cause);
    }
}
