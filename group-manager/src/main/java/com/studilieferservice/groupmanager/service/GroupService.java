package com.studilieferservice.groupmanager.service;

import com.studilieferservice.groupmanager.persistence.Gruppe;
import com.studilieferservice.groupmanager.persistence.JpaGroupRepository;
import com.studilieferservice.groupmanager.persistence.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.studilieferservice.groupmanager.service.GroupEventType.*;

/**
 * This is a service for database Operations regarding Groups
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
}
