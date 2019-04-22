package by.tut.mdcatalog.service;

import by.tut.mdcatalog.service.model.ItemDTO;
import java.util.List;

public interface ItemService {
    ItemDTO add(ItemDTO item);

    List<ItemDTO> getItems();

    int update(Long id, String newStatus);
}