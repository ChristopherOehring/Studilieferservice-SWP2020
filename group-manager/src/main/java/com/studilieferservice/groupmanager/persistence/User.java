package com.studilieferservice.groupmanager.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This is how users are represented in this microservice <br>
 * Every user has to have an email address, which is unique -
 * there must not be two users with the same email address <br>
 * Also, every user has to have a first-, last- and username <br>
 * When a user is created, he is saved in the user repository (see
 * {@link com.studilieferservice.groupmanager.service.UserService}
 * and {@link JpaUserRepository} for more information) and can be
 * accessed via the associated user- or group-service
 * @author Manuel Jirsak
 * @author Christopher Oehring
 * @version 1.8 6/24/20
 */
//todo add version   -   Added version and getter/setter/adder-method - but how do we use them now? ~ Manu 6/24/20
//TODO should we allow duplicate usernames? ~ Manu 6/24/20
@Entity(name = "Nutzer")
@Table(name = "nutzer")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    /**
     * the user's email address, which can only be saved once and has to
     * match the valid form of an email address
     */
    @Id
    @Column(name = "email")
    private String email;

    /**
     * user's first name, has to only contain letters (upper/lower case),
     * dashes and spaces, may also contain special letters such as ê, à, ö, ß
     */
    private String firstName;

    /**
     * user's last name, has to only contain letters (upper/lower case),
     * dashes and spaces, may also contain special letters such as ê, à, ö, ß
     */
    private String lastName;

    /**
     * the user's screen name
     */
    private String userName;

    private String street;
    private String city;
    private String zip;

    private long version;

    @JsonIgnore
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    List<Invite> invites = new ArrayList<>();

    /**
     * The constructor for creating users
     * @param email user-id, which has to be a valid email address
     * @param firstName user's first name, has to be a valid name
     * @param lastName user's last name, has to be a valid name
     * @param userName user's screen name
     */
    public User(@JsonProperty("email") String email,
                @JsonProperty("firstName") String firstName,
                @JsonProperty("lastName") String lastName,
                @JsonProperty("userName") String userName,
                @JsonProperty("street") String street,
                @JsonProperty("city") String city,
                @JsonProperty("zip") String zip) {
        if (isValidEmailAddress(email) && isValidName(firstName) && isValidName(lastName)) {
            this.email = email;
            this.firstName = simplifyName(firstName);
            this.lastName = simplifyName(lastName);
            this.userName = userName;
            this.street = street;
            this.city = city;
            this.zip = zip;
            this.version = 0;
        }
        else if(!isValidName(firstName) || !isValidName(lastName)) {
            System.out.println("You have to fill in both your first name and last name, also you may only use letters, dashes and spaces");
        }
        else if (!isValidEmailAddress(email)){
            System.out.println("This is no valid email address");
        }
        else {
            System.out.println("Something wrong");
        }
    }

    /**
     * Default constructor required by jpa
     */
    public User() {    }

    /**
     * some normal getter and setter methods - see attribute declaration for more information
     */
    public String getEmail() {
        return email;
    }

    public void setEmail(String id) {
        this.email = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public long getVersion() {
        return this.version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public void addVersion(long add) {
        this.version = this.version + add;
    }

    /**
     * checks, whether the provided email address matches the valid form <br>
     * You can see all the rules for valid emails for example at
     * https://help.xmatters.com/ondemand/trial/valid_email_format.htm <br>
     * First, it is checked, whether there is any "@" in the provided
     * String and whether the string is not null, if not -> return false <br>
     * Then, the String is parted into two substrings: <br>
     * One string beginning from 0 and ending right before the "@",
     * describing the email prefix, and the other begins right after the "@"
     * and goes up to the end of the address-string, which is the email domain <br>
     * Both substrings must not be null (i.e. must be longer than zero) <br>
     * After that, it is checked, whether both substrings match
     * the regular expression of "[a-zA-Z0-9-_.]*"
     * (contain letters in upper or lower case, digits, dashes, underscores and dots (periods)) <br>
     * Next, it is assured, that no dashes, underscores or dots are following each other
     * and that both substrings do not begin or end with one of these symbols, <br>
     * but also the domain substring has to contain at least one dot
     * @param address the user address which is to validate
     * @return true, if it matches the correct syntax, otherwise false
     */
    public boolean isValidEmailAddress(String address) {
        if(address==null) return false;
        int countAT = StringUtils.countOccurrencesOf(address, "@");

        //address has to contain exactly one "@" and may not be the first or second symbol - every email prefix has to have at least two characters
        if (countAT==1 && address.indexOf("@")>1) {
            String seq1 = address.substring(0, address.indexOf("@"));
            String seq2 = address.substring(address.indexOf("@")+1);
            //return false if "@" is first or last symbol -> one of both substrings would be empty
            if(seq1.isEmpty()||seq2.isEmpty()) {
                return false;
            }
            //both substrings may only contain letters, digits, ".", "-" or "_", method "String.matches(String string)" takes a regular expression
            if(seq1.matches("[a-zA-Z0-9-_.]*") && seq2.matches("[a-zA-Z0-9-_.]*")) {
                //if first part contains at least one of ".", "-" or "_", it is checked whether there is a letter before or after
                if (StringUtils.countOccurrencesOf(seq1, ".") > 0 || StringUtils.countOccurrencesOf(seq1, "-") > 0 || StringUtils.countOccurrencesOf(seq1, "_") > 0) {
                    for (int i = 0; i < seq1.length(); i++) {
                        boolean nonAlphabeticSymbol = seq1.charAt(i) == '.' || seq1.charAt(i) == '-' || seq1.charAt(i) == '_';
                        if (nonAlphabeticSymbol) {
                            //first symbol is one of ".", "-" or "_"
                            if (i == 0) {
                                return false;
                            }
                            //two times ".", "-" or "_"
                            if (i + 2 < seq1.length()) {
                                boolean beforeIsAlphabetic = Character.isAlphabetic(seq1.charAt(i - 1));
                                boolean afterIsAlphabetic = Character.isAlphabetic(seq1.charAt(i + 1));
                                if (!(beforeIsAlphabetic && afterIsAlphabetic)) {
                                    return false;
                                }
                            }
                            //last symbol before "@" is one of ".", "-" or "_"
                            if (i + 1 == seq1.length()) {
                                return false;
                            }
                        }
                    }
                }
                //second part has to have at least one dot, otherwise the same rules apply
                if (StringUtils.countOccurrencesOf(seq2, ".") >= 1) {
                    for (int i = 0; i < seq2.length(); i++) {
                        boolean nonAlphabeticSymbol = seq2.charAt(i) == '.' || seq2.charAt(i) == '-' || seq2.charAt(i) == '_';
                        if (nonAlphabeticSymbol && i + 1 < seq2.length()) {
                            //first symbol after "@" is one of ".", "-" or "_"
                            if (i == 0) {
                                return false;
                            }
                            //two times one of ".", "-" or "_"
                            if (i + 2 < seq2.length()) {
                                boolean beforeIsAlphabetic = Character.isAlphabetic(seq2.charAt(i - 1));
                                boolean afterIsAlphabetic = Character.isAlphabetic(seq2.charAt(i + 1));
                                if (!(beforeIsAlphabetic && afterIsAlphabetic)) {
                                    return false;
                                }
                            }
                        }
                        //last symbol is one of ".", "-" or "_"
                        if (nonAlphabeticSymbol && i + 1 == seq2.length()) {
                            return false;
                        }
                    }
                } else return false; //no "." in second part, must be for mail domain
                return true;
            }
        }
        return false; //would be if there is no "@"
    }

    /**
     * Checks whether the given String would be a valid name for a person <br>
     * I.e. a name may not contain digits, question marks or brackets,
     * but may contain special letters like ê, à, ö, ß <br>
     * It is also correct, if the string contains spaces or dashes,
     * but there must be no dashes at the beginning, at the end
     * or one right after the other <br>
     * Of course, the name must not be blank
     * @param name the name which is to validate
     * @return true if the name matches the rules described above
     */
    public boolean isValidName(String name) {
        if(name!=null) {
            //return false, if it begins or ends with dash or does contain "double-dash"
            if (name.charAt(0) == '-' || name.charAt(name.length() - 1) == '-' || name.contains("--")) {
                return false;
            }
            if (!name.isBlank()) {
                return name.matches("[a-zA-Z -À-ÿ]*");
            }
        }
        return false;
    }

    /**
     * Removes unnecessary white spaces at the beginning or at the end <br>
     * TBH, it's pretty useless and could be completely deleted <br>
     * All usages could be replaced with String.strip() <br>
     * TL;DR: I'm dumb
     * @param name name to strip
     * @return stripped string
     */
    public String simplifyName(String name) {
        if(StringUtils.countOccurrencesOf(name, " ")>0) {
            name=name.strip();
        }
        return name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Lists all open invites of this user, depending on whether he was invited to a group <br>
     * This list does not contain accepted, declined and/or removed invites <br>
     * For more information see {@link Invite} and {@link com.studilieferservice.groupmanager.service.InviteService}
     */
    public List<Invite> getInvites() {
        return invites;
    }

    /**
     * If a user receives an invitation, it is saved in the user's list
     * until he accepts/declines it or until it gets removed <br>
     * This list does not contain accepted, declined and/or removed invites <br>
     * For more information see {@link Invite} and {@link com.studilieferservice.groupmanager.service.InviteService}
     */
    public boolean addInvite(Invite invite){
        return invites.add(invite);
    }

    /**
     * Removes an invitation from the user's list <br>
     * This method is used whenever a user accepts/declines an invitation
     * or if the invitation is retracted by the inviter <br>
     * If that is the case, the invite gets removed from the list
     * This list does not contain accepted, declined and/or removed invites <br>
     * For more information see {@link Invite} and {@link com.studilieferservice.groupmanager.service.InviteService}
     */
    public boolean removeInvite(Invite invite) {
        return invites.remove(invite);
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", invites=" + invites +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email) &&
                firstName.equals(user.firstName) &&
                lastName.equals(user.lastName) &&
                Objects.equals(userName, user.userName) &&
                invites.equals(user.invites);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, firstName, lastName, userName, invites);
    }
}
