package com.studilieferservice.groupmanager.persistence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This is how users are represented in this microservice
 */

@Entity(name = "Nutzer")
@Table(name = "nutzer")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @Id
    @Column(name = "email")
    private String email;

    private String firstName;

    private String lastName;

    private String userName;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    List<Invite> invites = new ArrayList<>();

    /**
     * just another constructor for creating users
     * @param email user-id, which has to be a valid email address
     * @param firstName user's first name
     * @param lastName user's last name
     * @param userName user's screen name
     */
    public User(@JsonProperty("email") String email,
                @JsonProperty("firstName") String firstName,
                @JsonProperty("lastName") String lastName,
                @JsonProperty("userName") String userName) {
        if (isValidEmailAddress(email) && isValidName(firstName) && isValidName(lastName)) {
            this.email = email;
            this.firstName = simplifyName(firstName);
            this.lastName = simplifyName(lastName);
            this.userName = userName;
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

    //Adresse ist erlaubt, wenn @ enthalten und nach @ mind. 1 zeichen und danach 1 punkt und noch mal mind. 1 zeichen
    //erlaubtes Email Format: https://help.xmatters.com/ondemand/trial/valid_email_format.htm
    public boolean isValidEmailAddress(String address) {
        if(address==null) return false;
        int countAT = StringUtils.countOccurrencesOf(address, "@");

        if (countAT==1 && address.indexOf("@")>1) {
            String seq1 = address.substring(0, address.indexOf("@"));
            String seq2 = address.substring(address.indexOf("@")+1);
            //passiert, wenn "@" erstes oder letztes Zeichen ist
            if(seq1.isEmpty()||seq2.isEmpty()) {
                return false;
            }
            //substrings dürfen nur Buchstaben, Zahlen, -, _ und . enthalten; String.matches nimmt regulären Ausdruck an
            if(seq1.matches("[a-zA-Z0-9-_.]*") && seq2.matches("[a-zA-Z0-9-_.]*")) {
                //enthält erster Teil einen Punkt, wird überprüft, ob davor und danach noch ein Buchstabe kommt
                if (StringUtils.countOccurrencesOf(seq1, ".") > 0 || StringUtils.countOccurrencesOf(seq1, "-") > 0) {
                    for (int i = 0; i < seq1.length(); i++) {
                        if (seq1.charAt(i) == '.') {
                            //passiert, wenn erstes Zeichen ein ".", "_" oder "-" ist
                            if ((seq1.charAt(i) == '.' || seq1.charAt(i) == '_' || seq1.charAt(i) == '-') && i == 0) {
                                return false;
                            }
                            //passiert, wenn zwei "." hinter einander
                            if (i > 0 && i + 2 < seq1.length()) {
                                boolean beforeIsAlphabetic = Character.isAlphabetic(seq1.charAt(i - 1));
                                boolean afterIsAlphabetic = Character.isAlphabetic(seq1.charAt(i + 1));
                                if (!(beforeIsAlphabetic && afterIsAlphabetic)) {
                                    return false;
                                }
                            }
                            //passiert, wenn letztes Zeichen vor "@" ein "." ist
                            if (seq1.charAt(i) == '.' && i + 1 == seq1.length()) {
                                return false;
                            }
                        }
                    }
                }
                //zweiter Teil muss mindestens einen Punkt enthalten, sonst gelten dieselben Regeln
                if (StringUtils.countOccurrencesOf(seq2, ".") >= 1) {
                    for (int i = 0; i < seq2.length(); i++) {
                        if (seq2.charAt(i) == '.' && i + 1 < seq2.length()) {
                            //passiert, wenn erstes Zeichen nach "@" ein "." ist
                            if (seq2.charAt(i) == '.' && i == 0) {
                                return false;
                            }
                            //passiert, wenn zwei "." hinter einander
                            if (i > 0 && i + 2 < seq2.length()) {
                                boolean beforeIsAlphabetic = Character.isAlphabetic(seq2.charAt(i - 1));
                                boolean afterIsAlphabetic = Character.isAlphabetic(seq2.charAt(i + 1));
                                if (!(beforeIsAlphabetic && afterIsAlphabetic)) {
                                    return false;
                                }
                            }
                        }
                        //passiert, wenn letztes Zeichen ein "." ist
                        if (seq2.charAt(i) == '.' && i + 1 == seq2.length()) {
                            return false;
                        }
                    }
                } else return false; //dann ist kein "." im zweiten Teil enthalten, das muss aber für die Mail Domain so sein
                return true;
            }
        }
        return false;
    }

    //Name darf nur Buchstaben, Striche und Leerzeichen (für Doppelnamen) enthalten, darf nicht leer sein
    public boolean isValidName(String name) {
        //darf weder am Anfang noch am Ende ein "-" haben, auch nicht zwei -- nacheinander
        if(name!=null) {
            if (name.charAt(0) == '-' || name.charAt(name.length() - 1) == '-' || name.contains("--")) {
                return false;
            }
            if (!name.isBlank()) {
                return name.matches("[a-zA-Z -]*");
            }
        }
        return false;
    }

    public String simplifyName(String name) {
        if(StringUtils.countOccurrencesOf(name, " ")>0) {
            name=name.strip();
            /*for(int i = 0; i<name.length();i++) {
                //entfernt überflüssige Leerzeichen am Anfang
                if(i==0 && name.charAt(i)==' ') {
                    System.out.println(name);
                    name=name.replaceFirst(" ","");
                    System.out.println(name);
                    i--;
                }
                //entfernt überflüssige Leerzeichen am Ende
                if(i==name.length()-1 && name.charAt(i)== ' ') {
                    System.out.println(name);
                    name=name.strip();
                    System.out.println(name);
                    i--;
                }
            }*/
        }
        return name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Invite> getInvites() {
        return invites;
    }

    public boolean addInvite(Invite invite){
        return invites.add(invite);
    }

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
