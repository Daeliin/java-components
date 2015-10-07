package com.daeliin.framework.commons.security.details;

import com.daeliin.framework.commons.model.PersistentResource;
import java.io.Serializable;
import java.util.Date;

public interface UserDetails<ID extends Serializable> extends PersistentResource<ID> {

    String getEmail();
    
    void setEmail(final String email);
    
    String getUsername();
    
    void setUsername(final String username);
    
    String getClearPassword();
    
    void setClearPassword(final String clearPassword);
    
    String getPassword();
    
    void setPassword(final String password);
    
    boolean isEnabled();
    
    void setEnabled(final boolean enabled);
    
    String getToken();
    
    void setToken(final String token);
    
    Date getSignedUpDate();
    
    void setSignedUpDate(final Date signedUpDate);
}
