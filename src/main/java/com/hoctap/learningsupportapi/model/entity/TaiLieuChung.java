package  com.hoctap.learningsupportapi.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tai_lieu_chung")
@Getter
@Setter
public class TaiLieuChung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_tai_lieu")
    private Integer id;

    @Column(name = "tieu_de")
    private String title;

    @Column(name = "mo_ta")
    private String description;

    @Column(name = "duong_dan_file")
    private String filePath;

    @Column(name = "loai_file")
    private String type;

    @Column(name = "kich_thuoc_file")
    private Long size;

    @Column(name = "luot_xem")
    private Integer views;

    @Column(name = "luot_tai")
    private Integer downloads;

    @Column(name = "ngay_tai_len")
    private LocalDateTime createdAt;

    @Transient
    private String nhan;

    @ManyToOne
    @JoinColumn(name = "ma_chu_de")
    private ChuDe chuDe;

    @OneToMany(mappedBy = "taiLieu")
    private List<DanhGiaTaiLieuChung> danhGias;

}
