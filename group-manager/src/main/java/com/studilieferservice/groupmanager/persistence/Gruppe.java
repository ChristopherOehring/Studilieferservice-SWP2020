package com.studilieferservice.groupmanager.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

// TODO: 5/31/20 Fix the version, so that it only goes up by one if something actually changes. (not important)

// TODO: 6/09/20 Rethink the way GroupEvents are created, as groupService.save() is not necessary to make changes persistent

// TODO: rename this to Group to stick with naming conventions through out the projects

/**
 * The Structure that represents groups <br>
 * To create a group, see the associated method in the
 * {@link com.studilieferservice.groupmanager.controller.GroupController} <br>
 * When created, the user- and admin-lists are empty,
 * the delivery date and place are null, the version
 * equals to zero (is needed for kafka messages) and
 * the group does not hold any invitations <br>
 * When created, the group also receives a random UUID
 * and the requesting user is set as the group owner
 *
 * @author Manuel Jirsak
 * @author Christopher Oehring
 * @version 1.8 6/24/20
 */

@Entity(name = "Gruppe")
@Table(name = "gruppe")
public class Gruppe {

    /**
     * The identification number for this group, as a String <br>
     * The value for this string is a randomly generated UUID,
     * which is assigned, when a group is created
     */
    @Id
    @Column(name = "group_id")
    private String id;

    /**
     * The display name of this group, which has no syntax rules
     * (except for being non-null or non-blank), that is assigned,
     * when a group is created and cannot be changed afterwards
     * //TODO should group name be changeable? ~ Manu 6/24/20
     */
    @NotNull
    private String groupName;

    /**
     * Determines the owner of the group <br>
     * When a user creates a group, he is automatically the owner <br>
     * That means, he has all rights, like adding/removing members,
     * promote members or demote admins, change the delivery data etc. <br>
     * Every group has to have exactly one owner
     */
    @ManyToOne
    private User owner;

    /**
     * Every member of the group is saved in an ArrayList of type User <br>
     * When a member leaves the group, gets promoted to admin or is set
     * as group owner, he gets deleted from this list <br>
     * When a member joins the group, he will be registered in this list
     */
    @ManyToMany
    private List<User> memberList = new ArrayList<>();

    /**
     * Every admin of the group is saved in an ArrayList of type User <br>
     * This excludes members (as they have less rights than admins) and
     * the owner (who has far more rights as the admins) <br>
     * When an admin leaves the group, gets demoted to member or is set
     * as group owner, he gets deleted from this list <br>
     * When a member gets promoted, he will be registered in this list <br>
     * Also, if the owner changes, the former owner is saved as admin
     */
    @ManyToMany
    private List<User> adminList = new ArrayList<>();

    /**
     * Saves all outgoing invites of this group, i.e. if there are invitations,
     * that are not accepted/declined by the invited user or removed yet,
     * they are saved in this ArrayList (for more information,see {@link Invite},
     * or {@link com.studilieferservice.groupmanager.service.InviteService})
     */
    @JsonIgnore
    @OneToMany(
            mappedBy = "group",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    List<Invite> invites = new ArrayList<>();

    //TODO why do we have this? Do we have to send a kafka message every time a group is updated? ~ Manu 6/24/20
    private long version;

    private String deliveryCity; //city to deliver to
    private String zipCode; //zip code of city
    private String deliveryStreet; //street and house number to deliver to
    private String deliveryHouseNumber;

    private String deliveryDate;

    /**
     * Every group has its own delivery address and delivery date,
     * determining the day of payment and delivery <br>
     * Only admins and the owner are allowed to change these values <br>
     * The delivery address (delivery place) consists of four parts: <br>
     *     String deliveryCity: City to deliver to <br>
     *     String zipCode: postal code of provided city <br>
     *     String deliveryStreet: Street name to deliver to <br>
     *     String deliveryHouseNumber: House to deliver to <br>
     * If you wonder about syntax and usage, you can get more information at
     * {@link com.studilieferservice.groupmanager.controller.bodys.GroupDeliveryBody}
     * and {@link com.studilieferservice.groupmanager.controller.GroupController}
     * @return list of address parts, all together determine the delivery place
     */
    public List<String> getDeliveryPlace() {
        List<String> place = new ArrayList<>();
        place.add(deliveryCity);
        place.add(zipCode);
        place.add(deliveryStreet);
        place.add(deliveryHouseNumber);
        return place;
    }

    /**
     * Changes the delivery address of the group, this feature is only
     * available for admins and the owner <br>
     * The delivery address (delivery place) consists of four parts: <br>
     *     String deliveryCity: City to deliver to <br>
     *     String zipCode: postal code of provided city <br>
     *     String deliveryStreet: Street name to deliver to <br>
     *     String deliveryHouseNumber: House to deliver to <br>
     * If you wonder about syntax and usage, you can get more information at
     * {@link com.studilieferservice.groupmanager.controller.bodys.GroupDeliveryBody}
     * and {@link com.studilieferservice.groupmanager.controller.GroupController}
     * @param deliveryPlace list of four strings, containing the city name,
     *                      zip code, street name and the house number,
     *                      which is then set as the new delivery address
     *                      for future orders of this group
     */
    public void setDeliveryPlace(List<String> deliveryPlace) {
        this.deliveryCity = deliveryPlace.get(0);
        this.zipCode = deliveryPlace.get(1);
        this.deliveryStreet = deliveryPlace.get(2);
        this.deliveryHouseNumber = deliveryPlace.get(3);
    }

    /**
     * The delivery date of a group is saved as a String in the form of
     * DD.MM.YYYY (for more information, see method documentation for
     * isValidDate(String date), which is somewhere below <br>
     * @return date of when the next order will be delivered as String
     */
    public String getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * Sets the delivery date of a group - if you ever use this method,
     * ALWAYS call isValidDate(String date) beforehand; this is done
     * automatically by now, because the only usage (and it should stay
     * the only usage) of this method is in the GroupController, where
     * the provided String is tested, whether it is a valid date (see
     * {@link com.studilieferservice.groupmanager.controller.GroupController}),
     * otherwise you could set an invalid date as next delivery date <br>
     * The delivery date of a group is saved as a String in the form of
     * DD.MM.YYYY (for more information, see method documentation for
     * isValidDate(), which is somewhere below <br>
     */
    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * all following methods just do that, what their name is describing
     */
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Gruppe() {
        this.version = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        version++;
        this.groupName = groupName;
    }

    public boolean addMember(User user) {
        if (owner.equals(user) || adminList.contains(user) || memberList.contains(user)) return false;
        version++;
        return memberList.add(user);
    }

    public boolean removeMember(User user) {
        if (memberList.remove(user)) {
            version++;
            return true;
        }
        return false;
    }

    public boolean removeAdminOrMember(User user) {
        version++;
        return (memberList.remove(user) | adminList.remove(user));
    }

    public List<User> getMembers() {
        return memberList;
    }

    public void updateMember(User oldMember, User newMember) {
        if (this.memberList.contains(oldMember)) {
            memberList.remove(oldMember);
            memberList.add(newMember);
            version++;
        }
    }

    public List<User> getAdmins() {
        return adminList;
    }

    // TODO: 5/31/20 Should this really be possible?:
    //TODO We should think about a better solution, this is weird... maybe it would be okay to delete this and change its usages to access promote(User user) ~ Manu 6/24/20
    public boolean addAdmin(User admin) {
        if (adminList.contains(admin) || memberList.contains(admin) || owner.equals(admin)) {
            return false;
        }
        version++;
        return this.adminList.add(admin);
    }

    public boolean removeAdmin(User admin) {
        if (this.adminList.remove(admin)) {
            version++;
            return true;
        }
        return false;
    }

    public void setOwner(User owner) {
        version++;
        removeAdminOrMember(owner);
        this.owner = owner;
    }

    /**
     * Gives any user special rights (like changing delivery data or
     * adding users to the group), i.e. promoting them to a group admin
     * @param user just a plain user, for examples or explanation see
     *             {@link User} or look at different json bodies
     * @return true, if the user was a member and is now an admin
     */
    public boolean promote(User user) {
        if (memberList.remove(user)) {
            adminList.add(user);
            version++;
            return true;
        }
        return false;
    }

    /**
     * Takes away special rights from any group admin and degrades them,
     * making them a normal group member
     * @param user just a plain user, for examples or explanation see
     *             {@link User} or look at different json bodies
     * @return true, if the user was an admin and is now a member
     */
    public boolean demote(User user) {
        if (adminList.remove(user)) {
            memberList.add(user);
            version++;
            return true;
        }
        return false;
    }

    // lookup

    /**
     * IS USER OWNER????
     * @param user USER WHO MAY BE OWNER????
     * @return YES, IF USER IS OWNER, NO, IF USER IS NOT THE OWNER!!!!
     */
    public boolean isOwner(User user) {
        return user == this.owner;
    }

    public String getPermissions(User user) {
        if (memberList.contains(user)) return "member";
        if (adminList.contains(user)) return "admin";
        if (owner == user) return "owner";
        return null;
    }

    public User getOwner() {
        return owner;
    }

    public List<Invite> getInvites() {
        return invites;
    }

    public void setInvites(List<Invite> invites) {
        this.invites = invites;
    }

    public boolean addInvite(Invite invite) {
        return invites.add(invite);
    }

    public boolean removeInvite(Invite invite) {
        return invites.remove(invite);
    }

    @Override
    public String toString() {
        return "Gruppe{" +
                "id='" + id + '\'' +
                ", groupName='" + groupName + '\'' +
                ", owner=" + owner +
                ", memberList=" + memberList +
                ", adminList=" + adminList +
                ", invites=" + invites +
                ", version=" + version +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gruppe gruppe = (Gruppe) o;
        return version == gruppe.version &&
                id.equals(gruppe.id) &&
                groupName.equals(gruppe.groupName) &&
                owner.equals(gruppe.owner) &&
                Objects.equals(memberList, gruppe.memberList) &&
                Objects.equals(adminList, gruppe.adminList) &&
                Objects.equals(invites, gruppe.invites);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupName, owner, memberList, adminList, invites, version);
    }

    /**
     * Checks, whether the provided date matches the correct syntax
     * of DD.MM.YYYY (other forms like MM/DD/YYYY or DD-MM-YYYY are
     * not supported, you have to convert them first)
     * @param date String to check, whether it is a date
     * @return if syntax is correct and date is still in the future
     *         it returns true, otherwise returns false
     */
    public boolean isValidDate(String date) {
        //date has to consist of two digits, then a dot, again two digits and a dot and then four digits for the year
        if (!date.matches("[0-9][0-9]\\.[0-9][0-9]\\.[0-9][0-9][0-9][0-9]")) {
            return false; //happens if date not XX.XX.XXXX, and X element of N
        }
        int day = Integer.parseInt(date.substring(0, 2));
        int month = Integer.parseInt(date.substring(3, 5));
        int year = Integer.parseInt(date.substring(6, 10));
        /*
        day must not be greater than 31, month not greater than 12 etc;
        also, February has max 29 days and April, June, September and November have 30 days
         */
        if (day > 31 || month > 12 || (day > 29 && month == 2) || (day > 30 && month == 4) || (day > 30 && month == 6) || (day > 30 && month == 9) || (day > 30 && month == 11))
            return false;
        /*
        February only has 29 days, if the year is a leap year (it's a leap year whenever year is divisible by four)
        There are two special regulations:
            1.)  if the year is divisible by 100, it is not a leap year (1900 was no leap year)
            2.)  but if the year is divisible by 400, it IS a leap year (2000 was a leap year)
         */
        if ((day == 29 && month == 2) && year % 4 == 0) {
            if (year % 100 == 0) {
                return year % 400 == 0;
            }
        }
        //syntax is correct, now check, whether the date is in the future
        return isDateInFuture(day, month, year);
    }

    /**
     * Checks, whether the given date is still in the future (i.e. not
     * equal to the present day) <br>
     * The provided date has to match the correct syntax of DD.MM.YYYY,
     * to achieve that, this method is only called by isValidDate(String)
     * @param dayDate provided day; if the provided month is equal to the
     *                current month, this parameter has to be greater than
     *                the current day (no same-day deliveries)
     * @param monthDate provided month; if the provided year is equal to
     *                  the current year, this parameter has to be greater
     *                  or equal to the current month
     * @param yearDate provided year, has to be greater or equal to the
     *                 current year
     * @return true, if the date is greater than the present day, for now,
     *         we do not support same-day deliveries (as far as we know)
     */
    private boolean isDateInFuture(int dayDate, int monthDate, int yearDate) {

        //January is represented through 0 and is not month 1, so we need to decrement the given month
        monthDate--;
        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        //year must be greater or equal to the current year (no deliveries in the past)
        if (yearDate < year) {
            return false;
        }
        if (yearDate == year) {
            //if going to be delivered in the same year, it has to be in this month or later
            if (monthDate < month) {
                return false;
            }
            if (monthDate == month) {
                //delivery day has to be GREATER than the current day (no same-day deliveries)
                return dayDate > dayOfMonth;
            }
        }
        //date is in a future year
        return true;
    }
}
