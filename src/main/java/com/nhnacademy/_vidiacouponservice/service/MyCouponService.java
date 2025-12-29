package com.nhnacademy._vidiacouponservice.service;

import com.nhnacademy._vidiacouponservice.domain.Coupon;
import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.UserCoupon;
import com.nhnacademy._vidiacouponservice.domain.common.CouponStatus;
import com.nhnacademy._vidiacouponservice.domain.common.DiscountTargetType;
import com.nhnacademy._vidiacouponservice.domain.common.KdcCategory;
import com.nhnacademy._vidiacouponservice.domain.dto.request.CouponValidateItem;
import com.nhnacademy._vidiacouponservice.domain.dto.request.CouponValidateRequest;
import com.nhnacademy._vidiacouponservice.domain.dto.request.OrderCouponRequest;
import com.nhnacademy._vidiacouponservice.domain.dto.response.MyCouponResponse;
import com.nhnacademy._vidiacouponservice.domain.dto.response.OrderBookResponse;
import com.nhnacademy._vidiacouponservice.domain.dto.response.OrderPageCouponResponse;
import com.nhnacademy._vidiacouponservice.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyCouponService {

    private final UserCouponRepository userCouponRepo;

    public List<MyCouponResponse> getMyCoupons(Long userId) {
        return userCouponRepo.findAllByIdUserId(userId).stream()
                .map(uc -> {
                    Coupon c = uc.getCoupon();
                    CouponPolicy p = c.getCouponPolicy();

                    return new MyCouponResponse(
                            c.getCouponId(),
                            p.getPolicyName(),
                            p.getDiscountType().name(),
                            p.getDiscountValue(),
                            p.getMaxDiscountAmount(),
                            p.getDiscountTargetType().name(),
                            p.getCategoryKdcId(),
                            p.getBookId(),
                            c.getIssuedAt(),
                            c.getExpireAt(),
                            c.getStatus(),
                            c.getUserOrderId()
                    );
                })
                .toList();
    }

    // 주문 DTO → 쿠폰 검증 DTO 변환 (새로 추가된 핵심)
    public List<OrderPageCouponResponse> getOrderCoupons(Long userId, OrderCouponRequest req) {

        List<CouponValidateItem> items = req.orderBookResponses().stream()
                .map(b -> new CouponValidateItem(
                        b.bookId(),
                        b.categoryKdc(),
                        b.salePrice(),
                        b.quantity()
                ))
                .toList();

        CouponValidateRequest validateReq =
                new CouponValidateRequest(items);

        return getOrderCoupons(userId, validateReq);
    }


    // 주문 화면용 모든 쿠폰 검증
    public List<OrderPageCouponResponse> getOrderCoupons(Long userId, CouponValidateRequest req) {
        return userCouponRepo.findAllByIdUserId(userId).stream()
                .map(uc -> validate(uc, req))
                .toList();
    }




    private OrderPageCouponResponse validate(UserCoupon uc, CouponValidateRequest req) {

        Coupon c = normalizeExpire(uc.getCoupon());
        CouponPolicy p = c.getCouponPolicy();

        int discountAmount = 0;
        int discountPrice = 0;

        // 상태 체크
        if (c.getStatus() != CouponStatus.UNUSED)
            return fail(c, p, discountAmount, discountPrice, "이미 사용되었거나 만료된 쿠폰입니다.");

        if (c.getExpireAt().isBefore(LocalDateTime.now()))
            return fail(c, p, discountAmount, discountPrice, "쿠폰이 만료되었습니다.");

        int totalAmount = req.items().stream()
                .mapToInt(i -> i.price() * i.quantity())
                .sum();

        // 최소 주문 금액
        if (p.getMinOrderAmount() != null && totalAmount < p.getMinOrderAmount())
            return fail(c, p, discountAmount, discountPrice,
                    "최소 주문 금액 " + p.getMinOrderAmount() + "원 이상에서 사용 가능");

        int targetAmount = calculateTargetAmount(p, req.items());

        if (targetAmount <= 0)
            return fail(c, p, discountAmount, discountPrice, "쿠폰 적용 대상 상품이 없습니다.");

        discountAmount = p.getDiscountValue();
        discountPrice = calcDiscount(targetAmount, p);

        return new OrderPageCouponResponse(
                c.getCouponId(),
                p.getPolicyName(),
                p.getMaxDiscountAmount(),
                p.getDiscountType().name(),
                discountAmount,
                discountPrice,
                c.getExpireAt(),
                true,
                "사용 가능"
        );
    }


    private int calcDiscount(int total, CouponPolicy p) {

        // 정액
        if (p.getDiscountType().name().equals("PRICE"))
            return p.getDiscountValue();

        // 정률
        int discount = total * p.getDiscountValue() / 100;

        if (p.getMaxDiscountAmount() != null)
            discount = Math.min(discount, p.getMaxDiscountAmount());

        return discount;
    }

    private OrderPageCouponResponse fail(Coupon c, CouponPolicy p,
                                         int discountAmount, int discountPrice, String reason) {
        return new OrderPageCouponResponse(
                c.getCouponId(),
                p.getPolicyName(),
                p.getMaxDiscountAmount(),
                p.getDiscountType().name(),
                discountAmount,
                discountPrice,
                c.getExpireAt(),
                false,
                reason
        );
    }

    private Coupon normalizeExpire(Coupon c) {
        if (c.getStatus() == CouponStatus.UNUSED &&
                c.getExpireAt().isBefore(LocalDateTime.now())) {

            c.setStatus(CouponStatus.EXPIRED);
            // 👉 여기서 save 해도 되고, 조회 전용이면 안 해도 됨
        }
        return c;
    }

    private int calculateTargetAmount(
            CouponPolicy p,
            List<CouponValidateItem> items
    ) {
        return switch (p.getDiscountTargetType()) {

            case ALL -> items.stream()
                    .mapToInt(i -> i.price() * i.quantity())
                    .sum();

            case CATEGORY -> {
                String required = KdcCategory
                        .fromKdcId(p.getCategoryKdcId())
                        .getCode();

                yield items.stream()
                        .filter(i ->
                                KdcCategory.fromKdcId(i.categoryKdcId())
                                        .getCode()
                                        .equals(required)
                        )
                        .mapToInt(i -> i.price() * i.quantity())
                        .sum();
            }

            case BOOK -> items.stream()
                    .filter(i -> i.bookId().equals(p.getBookId()))
                    .mapToInt(i -> i.price() * i.quantity())
                    .sum();
        };
    }



}