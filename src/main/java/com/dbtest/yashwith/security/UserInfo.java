package com.dbtest.yashwith.security;

import com.dbtest.yashwith.entities.User;
import com.dbtest.yashwith.enums.Role;
import com.dbtest.yashwith.model.user.TokenPayload;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Data;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class UserInfo implements UserDetails {

    @Setter private transient TokenPayload tokenPayload;
    private transient Collection<? extends GrantedAuthority> authorities;
    private String appVer;

    /**
     * Fetch user from database.
     *
     * @return user fetched from the database.
     */
    public User getUser() {
        return new User();
    }

    /**
     * Get the userId from the user object.
     *
     * @return String consisting of userId.
     */
    public String getUserId() {
        return this.tokenPayload.getUserId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        if (tokenPayload != null) {
            if (tokenPayload.getRole() == null) {
                return authorities;
            }
            if (tokenPayload.getAllowedRoles() == null) {
                return authorities;
            }
            for (Role role : this.tokenPayload.getAllowedRoles()) {
                authorityList.add(new SimpleGrantedAuthority(role.toString()));
            }
            this.authorities = authorityList;
        }
        return authorityList;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "UserInfo{" + "user=" + tokenPayload + ", authorities=" + authorities + "}";
    }

    public String getDetails() {
        if (tokenPayload != null) {
            return "{\"id\": "
                    + "\""
                    + tokenPayload.getUserId()
                    + "\""
                    + ", \"countryCode\": "
                    + "\" \""
                    + ", \"phoneNumber\": "
                    + "\""
                    + tokenPayload.getPhoneNumber()
                    + "\""
                    + "}";
        } else {
            return null;
        }
    }
}
