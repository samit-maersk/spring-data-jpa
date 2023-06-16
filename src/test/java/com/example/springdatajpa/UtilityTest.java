package com.example.springdatajpa;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
public class UtilityTest {

      @Test
      public void test() {
          var myList = List.of(1,2,3,4,5,5,7,9,3,3);

          var removeDuplicate = myList.stream().collect(Collectors.toSet());
          System.out.println(removeDuplicate);

          var removeDuplicate1 = myList.stream().distinct().collect(Collectors.toList());
          System.out.println(removeDuplicate1);

          var myMap = removeDuplicate1.stream().collect(Collectors.toMap(i -> i, i -> i*2));
          System.out.println(myMap);

          var total = myList.stream().reduce(0, (a,b) -> a+b);
          System.out.println(total);
      }
}
