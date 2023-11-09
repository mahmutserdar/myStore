package com.shopping.store.controller;

import com.shopping.store.model.*;
import com.shopping.store.repository.ItemRepository;
import com.shopping.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
public class ItemController {

    private final ItemRepository itemRepository;
    private final BasketService basketService;

    private final UserRepository userRepository;

    @Autowired
    public ItemController(ItemRepository itemRepository, BasketService basketService, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.basketService = basketService;
        this.userRepository = userRepository;

        if (userRepository.findByUsername("user") == null) {
            User currentUser = new User("user", "user");
            userRepository.save(currentUser);
        }

    }

    @GetMapping("/items")
    public String listItems(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "items";
    }

    @GetMapping("items/add")
    public String addItemForm(Model model) {
        model.addAttribute("item", new Item());
        return "add-item";
    }

    @GetMapping("/items/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item Id:" + id));
        model.addAttribute("item", item);
        return "update-item";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/items/update/{id}")
    public String updateItem(@PathVariable Long id, @ModelAttribute Item item) {
        itemRepository.findById(id).ifPresent(i -> {
            i.setItemName(item.getItemName());
            i.setItemPrice(item.getItemPrice());
            i.setItemDesc(item.getItemDesc());
            itemRepository.save(i);
        });
//        redirectAttributes.addFlashAttribute("success", "Item updated successfully!");
        return "redirect:/items";
    }


    @PostMapping("/items/delete/{id}")
    public String deleteItem(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item Id:" + id));
        itemRepository.delete(item);
        redirectAttributes.addFlashAttribute("success", "Item deleted successfully!");
        return "redirect:/items";
    }


    @PostMapping("/items")
    public String addItem(@ModelAttribute Item item) {
        itemRepository.save(item);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            System.out.println(auth.getPrincipal() + "AUUUUUUUUUUT1");
        } else {
            System.out.println("AUUUUUUUUUUT2");
        }

        return "redirect:/items";
    }

    @GetMapping("/basket/add/{id}")
    public String addItemToBasket(@PathVariable Long id) {
        User currentUser = userRepository.findByUsername("user");
        Basket basket = currentUser.getBasket();

        itemRepository.findById(id).ifPresent(item -> {
            basketService.addBasketItem(basket, item, 1);
        });

        return "redirect:/items";
    }


    @GetMapping("/basket")
    public String viewBasket(Model model) {

        User currentUser = userRepository.findByUsername("user");
        Basket basket = currentUser.getBasket();
        model.addAttribute("basket", basket);
        return "basket";
    }


    @GetMapping("/test")
    public String testMapping() {
        return "This is a test page";
    }
}


