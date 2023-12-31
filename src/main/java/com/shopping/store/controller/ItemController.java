package com.shopping.store.controller;

import com.shopping.store.model.*;
import com.shopping.store.repository.ItemRepository;
import com.shopping.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

        if (userRepository.findByUsername("admin") == null) {
            User currentUser = new User("admin", "admin");
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

//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null) {
//            System.out.println(auth.getPrincipal() + "AUUUUUUUUUUT1");
//        } else {
//            System.out.println("AUUUUUUUUUUT2");
//        }

        return "redirect:/items";
    }

    @GetMapping("/basket")
    public String viewBasket(Model model) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        User currentUser = userRepository.findByUsername(currentUserName);
        Basket basket = currentUser.getBasket();
        model.addAttribute("basket", basket);
        return "basket";
    }


    @GetMapping("/basket/add/{id}")
    public String addItemToBasket(@PathVariable Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        User currentUser = userRepository.findByUsername(currentUserName);
        Basket basket = currentUser.getBasket();

        itemRepository.findById(id).ifPresent(item -> {
            basketService.addBasketItem(basket, item, 1);
        });

        return "redirect:/items";
    }




    @GetMapping("/items/search")
    public String searchItems(@RequestParam String query, Model model) {
        List<Item> items = itemRepository.searchByName(query);
        model.addAttribute("items", items);
        return "items";
    }


    @PostMapping("/basket/delete/{id}")
    public String deleteItemFromBasket(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        // First, delete references in the basket_item table
        // This might involve calling a method in your service layer to handle this
        basketService.deleteItemReferences(id);

        // Then, delete the item itself
        itemRepository.findById(id).ifPresent(itemRepository::delete);
        redirectAttributes.addFlashAttribute("success", "Item deleted successfully!");
        return "redirect:/basket";
    }

}


