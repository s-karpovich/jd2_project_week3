package by.tut.mdcatalog.springboot.controller;

import by.tut.mdcatalog.service.ItemService;
import by.tut.mdcatalog.service.model.ItemDTO;
import by.tut.mdcatalog.service.model.ItemStatusDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import javax.validation.Valid;
import java.util.List;

@Controller
public class ItemController {
    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items")
    public String getItems(Model model) {
        List<ItemDTO> itemDTOList = itemService.getItems();
        model.addAttribute("items", itemDTOList);
        model.addAttribute("statusDTO", new ItemStatusDTO());
        logger.info("Items {}", itemDTOList);
        return "itemslist";
    }

    @GetMapping("/items/{id}")
    public String getItems(@PathVariable int id, Model model) {
        List<ItemDTO> itemDTOList = itemService.getItems();
        model.addAttribute("items", itemDTOList);
        model.addAttribute("statusDTO", new ItemStatusDTO());
        model.addAttribute("affectedRows", id);
        logger.info("Items {}", itemDTOList);
        return "itemslist";
    }

    @GetMapping("/add")
    public String addItem(ItemDTO itemDTO) {
        return "add";
    }

    @PostMapping("/add")
    public String addItem(
            @Valid ItemDTO itemDTO,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return "add";
        } else {
            itemService.add(itemDTO);
            logger.info("Item added: {}", itemDTO);
            return "redirect:/items";
        }
    }

    @GetMapping("/change")
    public String changeStatus(ItemStatusDTO itemStatusDTO) {
        return "itemslist";
    }

    @PostMapping("/change")
    public String changeStatus(
            @Valid ItemStatusDTO itemStatusDTO,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            List<ItemDTO> itemDTOList = itemService.getItems();
            model.addAttribute("items", itemDTOList);
            logger.info("Item {}", itemDTOList);
            return "itemslist";
        } else {
            logger.info("Change from: {} - to {}", itemStatusDTO.getId(), itemStatusDTO.getNewStatus());
            int affectedRows = itemService.update(itemStatusDTO.getId(), itemStatusDTO.getNewStatus());
            logger.info("AffectedRows: {}", affectedRows);
            return "redirect:/items/" + affectedRows;
        }
    }
}
