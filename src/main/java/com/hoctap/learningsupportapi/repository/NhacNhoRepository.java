package com.hoctap.learningsupportapi.repository;
import com.hoctap.learningsupportapi.model.dto.lichhoc.EmailNhacNhoDTO;
import com.hoctap.learningsupportapi.model.entity.NhacNho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NhacNhoRepository extends JpaRepository<NhacNho, Integer> {
    Optional<NhacNho> findByMaSuKienAndMaNguoiDung(
            Integer maSuKien,
            Integer maNguoiDung
    );

    @Modifying
    @Query("""
    UPDATE NhacNho n
    SET n.email = :email
    WHERE n.maNguoiDung = :userId
""")
    void updateEmailByUser(
            @Param("userId") Integer userId,
            @Param("email") String email
    );

    @Query(
            value = """
        SELECT TOP 1 email
        FROM nhac_nho
        WHERE ma_nguoi_dung = :userId
          AND email IS NOT NULL
    """,
            nativeQuery = true
    )
    Optional<String> findFirstEmailByUserId(
            @Param("userId") Integer userId
    );

    @Query(
            value = """
        SELECT 
            nn.nhac_id        AS nhacId,
            lh.tieu_de        AS tieuDe,
            lh.ma_su_kien     AS maSuKien,
            nd.ho_ten         AS hoTen,
            COALESCE(nn.email, nd.email) AS email,
            lh.thoi_gian_ket_thuc AS thoiGianKetThuc,
            mhcn.ten_mon_hoc  AS tenMonHoc,
            lh.loai_su_kien   AS loaiSuKien,
            lh.thoi_gian_bat_dau AS thoiGianBatDau,
            lh.muc_do_uu_tien AS mucDoUuTien,
            lh.dia_diem       AS diaDiem,
            lh.mo_ta          AS moTa,
            nn.loai_nhac_nho  AS loaiNhacNho
        FROM nhac_nho nn
        JOIN lich_hoc lh ON lh.ma_su_kien = nn.ma_su_kien
        JOIN nguoi_dung nd ON nd.ma_nguoi_dung = nn.ma_nguoi_dung
        LEFT JOIN mon_hoc_ca_nhan mhcn ON mhcn.ma_mon_hoc = lh.ma_mon_hoc
        WHERE nn.nhac_email = 1
          AND (nn.trangthai IS NULL OR nn.trangthai = 'PENDING')
          AND nn.ngay_gui IS NULL
          AND nn.thoigian_nhacnho <= GETDATE()
    """,
            nativeQuery = true
    )
    List<Object[]> findEmailCanGuiRaw();
    Optional<NhacNho> findByMaSuKienAndMaNguoiDungAndLoaiNhacNho(
            Integer maSuKien,
            Integer maNguoiDung,
            Boolean loaiNhacNho
    );

    @Modifying
    @Query("""
    DELETE FROM NhacNho n
    WHERE n.maSuKien = :maSuKien
      AND n.maNguoiDung = :maNguoiDung
""")
    void deleteByMaSuKienAndMaNguoiDung(
            Integer maSuKien,
            Integer maNguoiDung
    );

}
