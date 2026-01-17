package com.hoctap.learningsupportapi.repository;

import com.hoctap.learningsupportapi.model.entity.TomTatBaiHoc;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TomTatSpecification {

    public static Specification<TomTatBaiHoc> byUser(Integer userId) {
        return (root, query, cb) ->
                cb.equal(root.get("maNguoiDung"), userId);
    }

    public static Specification<TomTatBaiHoc> keyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) {
                return cb.conjunction();
            }

            String like = "%" + keyword.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("tieuDe")), like),
                    cb.like(cb.lower(root.get("noiDungTomTat")), like)
            );
        };
    }

    public static Specification<TomTatBaiHoc> monHoc(Integer maMonHoc) {
        return (root, query, cb) -> {
            if (maMonHoc == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("maMonHoc"), maMonHoc);
        };
    }

    public static Specification<TomTatBaiHoc> dateRange(LocalDate from, LocalDate to) {
        return (root, query, cb) -> {
            if (from == null && to == null) {
                return cb.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();

            if (from != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("ngayTao"),
                                from.atStartOfDay()
                        )
                );
            }

            if (to != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("ngayTao"),
                                to.atTime(23, 59, 59)
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<TomTatBaiHoc> soTrang(Integer min, Integer max) {
        return (root, query, cb) -> {
            if (min == null && max == null) {
                return cb.conjunction();
            }

            if (min != null && max != null) {
                return cb.between(root.get("soTrang"), min, max);
            }

            if (min != null) {
                return cb.greaterThanOrEqualTo(root.get("soTrang"), min);
            }

            return cb.lessThanOrEqualTo(root.get("soTrang"), max);
        };
    }

    public static Specification<TomTatBaiHoc> soTu(Integer min, Integer max) {
        return (root, query, cb) -> {
            if (min == null && max == null) {
                return cb.conjunction();
            }

            if (min != null && max != null) {
                return cb.between(root.get("soTu"), min, max);
            }

            if (min != null) {
                return cb.greaterThanOrEqualTo(root.get("soTu"), min);
            }

            return cb.lessThanOrEqualTo(root.get("soTu"), max);
        };
    }
}
