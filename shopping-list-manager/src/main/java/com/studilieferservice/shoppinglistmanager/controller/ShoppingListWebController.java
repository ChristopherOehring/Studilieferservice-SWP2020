package com.studilieferservice.shoppinglistmanager.controller;

import com.studilieferservice.shoppinglistmanager.group.Group;
import com.studilieferservice.shoppinglistmanager.group.GroupService;
import com.studilieferservice.shoppinglistmanager.item.Item;
import com.studilieferservice.shoppinglistmanager.item.ItemService;
import com.studilieferservice.shoppinglistmanager.shoppinglist.ShoppingList;
import com.studilieferservice.shoppinglistmanager.shoppinglist.ShoppingListService;
import com.studilieferservice.shoppinglistmanager.user.User;
import com.studilieferservice.shoppinglistmanager.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Web Controller: accepts all web requests of this module and returns corresponding HTML pages.
 * All requests are accepted at ".../shoppingList/".
 * @author Stefan Horst
 * @version 2.0
 */
@Controller
@RequestMapping("/web/shoppingList")
public class ShoppingListWebController {

    private final ShoppingListService shoppingListService;
    private final ItemService itemService;
    private final UserService userService;
    private final GroupService groupService;

    @Autowired
    public ShoppingListWebController(ShoppingListService shoppingListService, ItemService itemService,
                                     UserService userService, GroupService groupService) {
        this.shoppingListService = shoppingListService;
        this.itemService = itemService;
        this.userService = userService;
        this.groupService = groupService;
    }

    /**
     * Web request (GET) for "/[Group-ID]/[User-ID]": retrieves the page of the shopping list for the respective group
     * and user (who belongs to the group)
     *
     * @param groupId The group-ID which is passed as a parameter through the URL
     * @param email The user-ID which is passed as a cookie which is set on login
     * @return the HTML page "list.html" which displays the shopping list
     */
    @GetMapping("/{groupId}")
    public ModelAndView getShoppingListForUserAndGroup(@PathVariable String groupId,
                                                       @CookieValue("useremail") @Nullable String email,
                                                       //HttpServletResponse response,
                                                       HttpServletRequest request) {

        if (email == null) return new ModelAndView("redirect:noLogin");

        ModelAndView model = new ModelAndView("list");

        ShoppingList sl = shoppingListService.getShoppingListByUserAndGroup(
                userService.getUser(email), groupService.getGroup(groupId));

        model.addObject("shoppingList", sl);
        model.addObject("totalPrice", shoppingListService.getTotalPrice(sl));

        model.addObject("link", request.getServerName());

        //response.addHeader("x-uic-stylesheet", "/shoppinglistmanager/style.css");
        return model;
    }

    /**
     * Web request (POST) for "/itemIncrease": increases the amount of the item in the currently displayed
     * shopping list whose "+"-button is pressed by one.
     *
     * @param request Spring specific: for accessing the shoppingList-ID and item-ID which are stored in the body of the
     *                HTML page "list.html"
     * @return a redirection to {@link #getShoppingListForUserAndGroup(String, String, HttpServletRequest) getShoppingListForUserAndGroup}
     * with the current group-ID and user-ID
     */
    @PostMapping("/itemIncrease")
    public String increaseItemAmountInShoppingList(HttpServletRequest request) {
        String shoppingListId = request.getParameter("shoppingListId");
        String itemName = request.getParameter("itemName");

        ShoppingList sl = shoppingListService.getShoppingList(Long.parseLong(shoppingListId));
        shoppingListService.addItemToShoppingList(sl, itemService.getItem(itemName), 1);

        return "redirect:http://" + request.getServerName() + ":9000/web/groupmanager/groupMenu/" + sl.getGroup().getId();
    }

    /**
     * Web request (POST) for "/itemDecrease": decreases the amount of the item in the currently displayed
     * shopping list whose "-"-button is pressed by one.
     *
     * @param request Spring specific: for accessing the shoppingList-ID and item-ID which are stored in the body of the
     *                HTML page "list.html"
     * @return a redirection to {@link #getShoppingListForUserAndGroup(String, String, HttpServletRequest) getShoppingListForUserAndGroup}
     * with the current group-ID and user-ID
     */
    @PostMapping("/itemDecrease")
    public String decreaseItemAmountInShoppingList(HttpServletRequest request) {
        String shoppingListId = request.getParameter("shoppingListId");
        String itemName = request.getParameter("itemName");

        ShoppingList sl = shoppingListService.getShoppingList(Long.parseLong(shoppingListId));
        shoppingListService.removeItemFromShoppingList(sl, itemService.getItem(itemName), 1);

        return "redirect:http://" + request.getServerName() + ":9000/web/groupmanager/groupMenu/" + sl.getGroup().getId();
    }

    @PostMapping("/addProduct")
    public String addProduct(HttpServletRequest request) {
        Group group = new Group(request.getParameter("groupId"), "");
        User user = new User(request.getParameter("userId"), "");
        Item item = new Item(request.getParameter("itemId"), 0);

        shoppingListService.addItemToShoppingList(shoppingListService.getShoppingListByUserAndGroup(user, group), item,
                Integer.parseInt(request.getParameter("amount")));

        return "redirect:http://" + request.getServerName() + ":9000/web/groupmanager/groupMenu/" + group.getId();
    }

    @GetMapping("/noLogin")
    public RedirectView noLogin(HttpServletRequest request){
        return new RedirectView("http://" + request.getServerName() + ":9080/web/usermanager/login");
    }
}
