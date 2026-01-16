package com.hoctap.learningsupportapi.model.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "nhac_nho")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NhacNho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nhac_id")
    private Integer nhacId;

    @Column(name = "ma_su_kien", nullable = false)
    private Integer maSuKien;

    @Column(name = "ma_nguoi_dung", nullable = false)
    private Integer maNguoiDung;

    @Column(name = "thoigian_nhacnho", nullable = false)
    private LocalDateTime thoiGianNhacNho;

    @Column(name = "nhac_app")
    private Boolean nhacApp;

    @Column(name = "nhac_email")
    private Boolean nhacEmail;

    @Column(name = "trangthai")
    private String trangThai;

    @Column(name = "ngay_gui")
    private LocalDateTime ngayGui;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "loai_nhac_nho", nullable = false)
    private Boolean loaiNhacNho;
}
