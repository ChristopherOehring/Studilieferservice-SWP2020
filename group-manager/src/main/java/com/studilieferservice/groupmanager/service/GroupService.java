package com.studilieferservice.groupmanager.service;

import com.studilieferservice.groupmanager.persistence.Gruppe;
import com.studilieferservice.groupmanager.persistence.JpaGroupRepository;
import com.studilieferservice.groupmanager.persistence.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.studilieferservice.groupmanager.service.GroupEventType.*;

/**
 * just a normal service for handling JpaRepositories
 * save: saves Groups to the repository
 * findGroup: give it a group ID (String) and you get the group with group ID, group name and all users (if this group exists)
 * findAll: gets you all groups with group ID, group name and all users
 * findById: no difference to findGroup - maybe we will remove one of these methods later on
 * deleteById: removes a group from the repository when given the group ID of the specific group
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
    public Gruppe save(Gruppe group) {
        Gruppe saved = groupRepository.save(group);

        eventPublisher.publishEvent(new GroupEvent(saved, this, CREATION));

        return saved;
    }

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
    public Optional<Gruppe> findGroup(String id){
        return  groupRepository.findById(id);
    }

    public List<Gruppe> findAll() {
        return groupRepository.findAll();
    }

    public Optional<Gruppe> findById(String id) {
        return groupRepository.findById(id);
    }

    /**
     * befördert einen Nutzer zum Admin, falls er bereits ein "member" der Gruppe ist
     * @param groupId die id der gruppe
     * @param userId die id des nutzers
     */
    public boolean promote(String groupId, String userId) throws NoSuchElementException {
        Gruppe group = groupRepository.findById(groupId).get();
        User user = userService.findUser(userId).get();
        return group.promote(user);
    }
}
