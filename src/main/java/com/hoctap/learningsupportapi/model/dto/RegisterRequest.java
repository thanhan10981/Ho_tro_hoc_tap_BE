package com.hoctap.learningsupportapi.model.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
@Data
public class RegisterRequest {

    @NotBlank
    private String hoTen;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String matKhau;

    @NotBlank
    private String xacNhanMatKhau;

    private String linhVucQuanTam;
}
