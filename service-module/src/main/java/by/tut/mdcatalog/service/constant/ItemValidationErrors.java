package by.tut.mdcatalog.service.constant;

public class ItemValidationErrors {
    public static final String STATUS_EMPTY = "STATUS CAN NOT BE EMTY";
    public static final String INCORRECT_STATUS = "CAN BE READY OR COMPLETED";
    public static final String ITEM_ID_EMPTY = "ID CAN NOT BE EMPTY";
    public static final String ID_INVALID = "ID LENGTH IS 1-19";
    public static final String ID_EMPTY = "NAME CAN NOT BE EMPTY";
    public static final String WRONG_NAME = "MUST BE FROM 1-40 CHARACTERS";
    public static final String STATUS_REGEXP = "^(READY|COMPLETED)$";

    private ItemValidationErrors() {
    }
}
