package com.dhar.automation.exception;

/**
 * @author Dharmendra Chouhan
 */
public enum AutomationErrorType {

    ID_NOT_FOUND("error.notfound.id", "Id not found"),
    DUPLICATE_NAME_EXISTS("error.duplicate.name", "Duplicate name exists"),
    ERROR_ELEMENT_NOTFOUND("error.element.notfound", "Element not found : ${element}"),
    ERROR_DATABASE_UPDATE_FAIL("error.database.update.fail", "Database update has failed"),
    ERROR_DATABASE_HOST_NOTFOUND("error.database.host.notfound", "Host not found for database")
    ,ERROR_DATABASE_SQL("error.database.sql","Database sql exception" )
    ,ERROR_TIMEOUT("error.timeout", "Timeout occurs")
    ,ERROR("error", "Error occurs");

    private String code;
    private String message;
    AutomationErrorType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
