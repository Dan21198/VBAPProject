package com.example.vbapproject.auth;

import com.example.vbapproject.role.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  private String userName;
  private String password;
  private Role role;
}
