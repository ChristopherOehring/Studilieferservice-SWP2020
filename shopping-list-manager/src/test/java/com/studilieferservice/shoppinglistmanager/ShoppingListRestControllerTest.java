package com.studilieferservice.shoppinglistmanager;

import com.studilieferservice.shoppinglistmanager.controller.ShoppingListRestController;
import com.studilieferservice.shoppinglistmanager.controller.bodies.ItemAndShoppingListBody;
import com.studilieferservice.shoppinglistmanager.group.Group;
import com.studilieferservice.shoppinglistmanager.group.GroupService;
import com.studilieferservice.shoppinglistmanager.item.Item;
import com.studilieferservice.shoppinglistmanager.item.ItemService;
import com.studilieferservice.shoppinglistmanager.shoppinglist.ShoppingList;
import com.studilieferservice.shoppinglistmanager.shoppinglist.ShoppingListService;
import com.studilieferservice.shoppinglistmanager.user.User;
import com.studilieferservice.shoppinglistmanager.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * @author Seraj Hadros
 * @version 1.1 7/07/20
 */
public class ShoppingListRestControllerTest {
    @InjectMocks
    private ShoppingListRestController shoppingListRestController;

    @Mock
    private ShoppingListService shoppingListService;

    @Mock
            private UserService userService;
    @Mock
    private  GroupService groupService;
    @Mock
    private  ItemService itemService;

    ShoppingList shoppingList1;

    User user = new User("email@web.com","tester");
    Group group = new Group("testGroup","SWP");
    Item item = new Item("Käse",2.99);
    ShoppingList shoppingList = new ShoppingList(user,group);
    ItemAndShoppingListBody body = new ItemAndShoppingListBody(item,shoppingList,1);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        shoppingList1 = mock(ShoppingList.class);

    }

    @Test
    public void indexTest(){
       String response = shoppingListRestController.index();
       assertThat(response).isEqualTo("It works! There is nothing more to see here");
    }

    @Test
    public void createShoppingListTest(){
        ResponseEntity<?> response = shoppingListRestController.createShoppingList(shoppingList1);
        assertThat(response.getStatusCodeValue()).isEqualTo(201);

    }


    @Test
    public void deleteShoppingListTest(){
      ResponseEntity<?> response = shoppingListRestController.deleteShoppingList(shoppingList1);
      assertThat(response.getStatusCodeValue()).isEqualTo(200);

    }

    @Test
    public void createUserTest(){
        ResponseEntity<?> response = shoppingListRestController.createUser(user);
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
    }

    @Test
    public void createGroupTest(){
        ResponseEntity<?> response = shoppingListRestController.createGroup(group);
        assertThat(response.getStatusCodeValue()).isEqualTo(201);

    }

    @Test
    public void createItemTest(){
        ResponseEntity<?> response = shoppingListRestController.createItem(item);
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
    }

    @Test
    public void addItemToShoppingListTest(){

        Mockito.when(shoppingListService
                .addItemToShoppingList(body.shoppingList, body.item, body.amount))
                .thenReturn(1);
        ResponseEntity<?> response = shoppingListRestController.addItemToShoppingList(body);
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).isEqualTo("Added Item to ShoppingList: amount="+body.amount
                +", newTotalAmount="+1+", "+body.item.toString()+", "+body.shoppingList.toString());
    }

    @Test
    public void removeItemFromShoppingListTest(){
        Mockito.when(shoppingListService.removeItemFromShoppingList(body.shoppingList, body.item, body.amount))
                .thenReturn(1);
        ResponseEntity<?> response = shoppingListRestController.removeItemFromShoppingList(body);
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
    }

    @Test
    public void getCompleteShoppingListAsJSONTest(){
        Mockito.when(shoppingListService.getCompleteShoppingListAsJSON("testGroup")).thenReturn(shoppingList.toString());
        ResponseEntity<?> response = shoppingListRestController.getCompleteShoppingListAsJSON("testGroup");
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(shoppingList.toString());
    }
}
