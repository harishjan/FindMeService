package com.helpfinder.model.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

//skill search is done with this request param class
public class SkillSearchQueryRequest {
         @NotBlank
         @Size(min = 6, max = 20)
         private String searchQuery;
         @NotNull 
         private int mileRadius;
         @NotNull 
         private double userLat;
         @NotNull 
         private double userLong;
         
         public String getSearchQuery() {
               return searchQuery;
         }
       
         public void setSearchQuery(String searchQuery) {
                this.searchQuery = searchQuery;
         }
           
         public int getMileRadius() {
                return mileRadius;
         }

         public void setMileRadius(int mileRadius) {
                this.mileRadius = mileRadius;
         }
         
         public void setUserLat(double lat) {
                this.userLat = lat;
         }
         
         public double getUserLat() {
             return this.userLat;
       }
     
       public void setUserLong(double userLong) {
              this.userLong = userLong;
       }
         
       public double getUserLong() {
              return userLong;
       }
     


}
