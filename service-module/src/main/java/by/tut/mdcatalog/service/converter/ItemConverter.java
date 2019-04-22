package by.tut.mdcatalog.service.converter;

import by.tut.mdcatalog.data.resources.model.Item;
import by.tut.mdcatalog.service.model.ItemDTO;

public interface ItemConverter {

    Item fromDTO(ItemDTO itemDTO);

    ItemDTO toDTO(Item item);
}
