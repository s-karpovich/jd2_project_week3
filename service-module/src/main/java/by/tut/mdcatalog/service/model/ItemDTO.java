package by.tut.mdcatalog.service.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import static by.tut.mdcatalog.service.constant.ItemValidationErrors.*;

public class ItemDTO {
    private Long id;
    @NotNull(message = ID_EMPTY)
    @Size(min = 1, max = 40, message = WRONG_NAME)
    private String name;
    @NotNull(message = STATUS_EMPTY)
    @Pattern(regexp = STATUS_REGEXP, message = INCORRECT_STATUS)

    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}