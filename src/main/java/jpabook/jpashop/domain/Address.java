package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.service.annotation.GetExchange;

@Embeddable
@Getter
public class Address {

  private String city;
  private String street;
  private String zipcode;

  protected Address() {

  }

  public Address(String city, String street, String zipcode) {
    this.city = city;
    this.street = street;
    this.zipcode = zipcode;
  }
}
