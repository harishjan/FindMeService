
/* 
 
 This is the enum which define the user type  
 * @author  Harish Janardhanan * 
 * @since   120-Jan-2022
 */
package com.helpfinder.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//enum defining different roles users can have
public enum EUserType {
         ROLE_ADMIN,
         ROLE_MODERATOR,
         ROLE_WORKER_USER,
         ROLE_HELPFINDER_USER;
         
         private static final Map<String, EUserType> nameToValueMap =
         new HashMap<String, EUserType>();
     
            static {
                for (EUserType value : EnumSet.allOf(EUserType.class)) {
                    nameToValueMap.put(value.name(), value);
                }
            }
            
            public static Optional<EUserType> forName(String name) {
                return Optional.of(nameToValueMap.get(name));
            }
}
