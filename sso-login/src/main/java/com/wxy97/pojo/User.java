package com.wxy97.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data  //getter setter
@NoArgsConstructor //无参构造
@AllArgsConstructor // 有参构造
@Accessors(chain = true)//链式结构
public class User {
    private Integer id;
    private String username;
    private String password;
}
