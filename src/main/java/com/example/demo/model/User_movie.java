package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User_movie {

	  private int userid;
      private int movieid;
      private boolean favourite;
      private int personal_rating;
      private String notes;
      
      public int getUserid() {
          return userid;
      }
      public void setUserid(int userid) {
          this.userid = userid;
      }
      public int getMovieid() {
          return movieid;
      }
      public void setMovieid(int movieid) {
          this.movieid = movieid;
      }
      public boolean isFavourite() {
          return favourite;
      }
      public void setFavourite(boolean favourite) {
          this.favourite = favourite;
      }
      public int getPersonal_rating() {
          return personal_rating;
      }
      public void setPersonal_rating(int personal_rating) {
          this.personal_rating = personal_rating;
      }
      public String getNotes() {
          return notes;
      }
      public void setNotes(String notes) {
          this.notes = notes;
      }
}
