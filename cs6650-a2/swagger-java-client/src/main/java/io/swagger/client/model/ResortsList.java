/*
 * Ski Data API for NEU Seattle distributed systems course
 * An API for an emulation of skier managment system for RFID tagged lift tickets. Basis for CS6650 Assignments for 2019
 *
 * OpenAPI spec version: 1.0.2
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package io.swagger.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.client.model.ResortsListResorts;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * ResortsList
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-10-10T04:42:35.987Z[GMT]")
public class ResortsList {
  @SerializedName("resorts")
  private List<ResortsListResorts> resorts = null;

  public ResortsList resorts(List<ResortsListResorts> resorts) {
    this.resorts = resorts;
    return this;
  }

  public ResortsList addResortsItem(ResortsListResorts resortsItem) {
    if (this.resorts == null) {
      this.resorts = new ArrayList<ResortsListResorts>();
    }
    this.resorts.add(resortsItem);
    return this;
  }

   /**
   * Get resorts
   * @return resorts
  **/
  @Schema(description = "")
  public List<ResortsListResorts> getResorts() {
    return resorts;
  }

  public void setResorts(List<ResortsListResorts> resorts) {
    this.resorts = resorts;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResortsList resortsList = (ResortsList) o;
    return Objects.equals(this.resorts, resortsList.resorts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resorts);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResortsList {\n");
    
    sb.append("    resorts: ").append(toIndentedString(resorts)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}