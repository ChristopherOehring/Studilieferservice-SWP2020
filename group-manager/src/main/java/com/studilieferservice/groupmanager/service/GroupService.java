package com.studilieferservice.groupmanager.service;

import com.studilieferservice.groupmanager.persistence.Gruppe;
import com.studilieferservice.groupmanager.persistence.JpaGroupRepository;
import com.studilieferservice.groupmanager.persistence.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.studilieferservice.groupmanager.service.GroupEventType.*;

/**
 * This is a service for database Operations regarding Groups
 * @author Christopher Oehring
 * @author Manuel Jirsak
 * @version 1.2 6/24/20
 */
@Service
public class GroupService {

    private final JpaGroupRepository groupRepository;

    private final ApplicationEventPublisher eventPublisher;

    private final UserService userService;

    @Autowired
    public GroupService(JpaGroupRepository groupRepository,
                        ApplicationEventPublisher eventPublisher,
                        UserService userService) {
        this.groupRepository = groupRepository;
        this.eventPublisher = eventPublisher;
        this.userService = userService;
    }

    //These Methods change stuff

    /**
     * Saves a group to the JpaRepository
     * @param group The group that should be saved
     * @return the group that was saved
     */
    public Gruppe save(Gruppe group) {
        Gruppe saved = groupRepository.save(group);

        eventPublisher.publishEvent(new GroupEvent(saved, this, UPDATE));

        return saved;
    }

    /**
     * This is used to delete a group
     * @param id the id of the group that should be deleted
     * @return true, if the group was found and could be deleted.
     *          false, if the group could not be found
     */
    @Transactional
    public boolean deleteById(String id) {
        Optional<Gruppe> g = groupRepository.findById(id);
        if(g.isEmpty()){
            return false;
        }
        groupRepository.deleteById(id);

        eventPublisher.publishEvent(new GroupEvent(g.get(), this, DELETION));
        return true;
    }

    //these methods dont change stuff

    /**
     * @return returns a list of all groups
     */
    public List<Gruppe> findAll() {
        return groupRepository.findAll();
    }

    /**
     * Retrieves an entity by its id.
     * @param id id - must not be null.
     * @return an optional containing the Group, or null if none was found.
     */
    public Optional<Gruppe> findById(String id) {
        return groupRepository.findById(id);
    }

    /**
     * returns all groups where the user of the given email is the Owner
     * @param email the identifying email
     * @return a List of all the groups that match
     * @throws InvalidParameterException if the user could not be found
     */
    public List<Gruppe> findAllWhereOwner(String email) throws InvalidParameterException{
        Optional<User> userOptional = userService.findById(email);
        if(userOptional.isEmpty()) throw new InvalidParameterException();
        User user = userOptional.get();

        List<Gruppe> result = groupRepository.findAll();
        result = result.stream()
                .filter(g -> g.isOwner(user))
                .collect(Collectors.toList());
        return result;
    }

    /**
     * returns all groups where the user of the given email is an Admin
     * @param email the identifying email
     * @return a List of all the groups that match
     * @throws InvalidParameterException if the user could not be found
     */
    public List<Gruppe> findAllWhereAdmin(String email) throws InvalidParameterException{
        Optional<User> userOptional = userService.findById(email);
        if(userOptional.isEmpty()) throw new InvalidParameterException();
        User user = userOptional.get();

        List<Gruppe> result = groupRepository.findAll();
        result = result.stream()
                .filter(g -> g.getAdmins().contains(user))
                .collect(Collectors.toList());
        return result;
    }

    /**
     * returns all groups where the user of the given email is a Member
     * @param email the identifying email
     * @return a List of all the groups that match
     * @throws InvalidParameterException if the user could not be found
     */
    public List<Gruppe> findAllWhereMember(String email) throws InvalidParameterException{
        Optional<User> userOptional = userService.findById(email);
        if(userOptional.isEmpty()) throw new InvalidParameterException();
        User user = userOptional.get();

        List<Gruppe> result = groupRepository.findAll();
        result = result.stream()
                .filter(g -> g.getMembers().contains(user))
                .collect(Collectors.toList());
        return result;
    }

    /**
     * returns all groups in which the user of the given email is not present
     * @param email the identifying email
     * @return a List of all the groups that match
     * @throws InvalidParameterException if the user could not be found
     */
    public List<Gruppe> findAllOther(String email) throws InvalidParameterException{
        Optional<User> userOptional = userService.findById(email);
        if(userOptional.isEmpty()) throw new InvalidParameterException();
        User user = userOptional.get();
        List<Gruppe> result = groupRepository.findAll();

        result = result.stream()
                .filter(g -> g.getPermissions(user) == null)
                .collect(Collectors.toList());
        return result;
    }

    /**
     * Creates a new group
     * @param groupName the name of the new group
     * @param owner the user who is creating the group
     * @return the groupId of the new group
     */
    public String createGroup(String groupName, User owner){
        Gruppe group = new Gruppe();
        group.setGroupName(groupName);
        group.setId(UUID.randomUUID().toString());


        group.setOwner(owner);

        group.setDeliveryDate(LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        List<String> address = new ArrayList<>();
        address.add(owner.getCity());
        address.add(owner.getZip());

        //splits the single "street" string into the street and the house number as two separate strings
        if (!owner.getStreet().matches(".*\\d.*")) {
            address.add(owner.getStreet());
            address.add("");
        } else {
            String street = owner.getStreet();
            StringBuilder number = new StringBuilder();

            int index;
            if (Character.isAlphabetic(street.charAt(street.length()-1))) {
                number.append(street.charAt(street.length() - 1));
                index = 2;
            } else
                index = 1;

            for (int i = street.length() - index; i > 0; i--)
            {
                if (Character.isDigit(street.charAt(i)))
                    number.insert(0, street.charAt(i));
                else if(Character.isAlphabetic(street.charAt(i)) || Character.isSpaceChar(street.charAt(i))) {
                    street = street.substring(0, i);
                    break;
                }
            }
            address.add(street);
            address.add(number.toString());
        }

        group.setDeliveryPlace(address);

        System.out.println(group);
        save(group);
        return group.getId();
    }
}
