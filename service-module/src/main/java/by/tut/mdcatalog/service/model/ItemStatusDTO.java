package by.tut.mdcatalog.service.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static by.tut.mdcatalog.service.constant.ItemValidationErrors.*;

public class ItemStatusDTO {
    @NotNull(message = ITEM_ID_EMPTY)
    @Min(value = 1, message = ID_INVALID)
    @Max(value = Long.MAX_VALUE, message = ID_INVALID)
    private Long id;
    @NotNull(message = STATUS_EMPTY)
    @Pattern(regexp = STATUS_REGEXP, message = INCORRECT_STATUS)

    private String newStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }
}


