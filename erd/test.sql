DROP TABLE IF EXISTS user_coupon;
DROP TABLE IF EXISTS coupon;
DROP TABLE IF EXISTS coupon_policy;


/* ===============================
   1. 쿠폰 정책 테이블
================================ */
CREATE TABLE coupon_policy (
                               coupon_policy_id     BIGINT       NOT NULL AUTO_INCREMENT COMMENT '쿠폰정책ID',
                               coupon_policy_name   VARCHAR(100) NOT NULL COMMENT '쿠폰정책이름',

                               policy_type          TINYINT      NOT NULL COMMENT '발급조건 (0=WELCOME,1=BIRTHDAY,2=EVENT)',
                               discount_type        TINYINT      NOT NULL COMMENT '할인방식 (0=PRICE,1=RATE)',
                               discount_value       INT          NOT NULL COMMENT '할인값',

                               discount_target_type TINYINT      NOT NULL COMMENT '할인적용대상 (0=ALL,1=CATEGORY,2=BOOK)',
                               category_kdc_id      VARCHAR(3)   NULL COMMENT '카테고리코드 (CATEGORY일 때)',
                               book_id              BIGINT       NULL COMMENT '도서ID (BOOK일 때)',

                               validity_type        TINYINT      NOT NULL COMMENT '유효기간 타입 (0=RELATIVE,1=ABSOLUTE)',
                               valid_days           INT          NULL COMMENT '상대유효기간(일)',

                               start_date           DATETIME     NULL COMMENT '절대유효 시작일',
                               end_date             DATETIME     NULL COMMENT '절대유효 종료일',

                               limited_quantity     INT          NULL COMMENT '한정수량 (NULL이면 무제한)',
                               issued_quantity      INT          NOT NULL DEFAULT 0 COMMENT '발급된 수량',

                               min_order_amount     INT          NOT NULL DEFAULT 0 COMMENT '최소 주문 금액',
                               max_discount_amount  INT          NOT NULL DEFAULT 0 COMMENT '최대 할인 금액',

                               is_activation        TINYINT      NOT NULL DEFAULT 1 COMMENT '활성화 여부',

                               PRIMARY KEY (coupon_policy_id)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci;


/* ===============================
   2. 발급된 쿠폰 테이블
================================ */
CREATE TABLE coupon (
                        coupon_id         BIGINT     NOT NULL AUTO_INCREMENT COMMENT '쿠폰ID',
                        coupon_policy_id  BIGINT     NOT NULL COMMENT '쿠폰정책ID',

                        issued_at         DATETIME   NOT NULL COMMENT '발급일',
                        expire_at         DATETIME   NOT NULL COMMENT '만료일',
                        used_at           DATETIME   NULL COMMENT '사용일',

                        status            TINYINT    NOT NULL DEFAULT 0 COMMENT '상태 (0=UNUSED,1=USED,2=EXPIRED)',
                        user_order_id     BIGINT     NULL COMMENT '사용된 주문ID',

                        PRIMARY KEY (coupon_id),

                        CONSTRAINT fk_coupon_policy
                            FOREIGN KEY (coupon_policy_id)
                                REFERENCES coupon_policy(coupon_policy_id)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci;


/* ===============================
   3. 유저 보유 쿠폰 테이블
================================ */
CREATE TABLE user_coupon (
                             coupon_id BIGINT NOT NULL COMMENT '쿠폰ID',
                             user_id   BIGINT NOT NULL COMMENT '유저ID',
                             policy_id BIGINT NOT NULL COMMENT '정책ID (중복 발급 방지용)',

                             PRIMARY KEY (coupon_id, user_id),

                             CONSTRAINT fk_user_coupon_coupon
                                 FOREIGN KEY (coupon_id)
                                     REFERENCES coupon(coupon_id)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci;


/* ===============================
   4. 쿠폰 테이블 인덱스 (핵심)
================================ */

-- 사용 / 만료 / 조회 공용
CREATE INDEX idx_coupon_status_expire
    ON coupon (status, expire_at);

-- 주문 롤백
CREATE INDEX idx_coupon_order
    ON coupon (user_order_id, status);

-- 정책별 조회 / 통계 / 복구
CREATE INDEX idx_coupon_policy
    ON coupon (coupon_policy_id);


/* ===============================
   5. issued_quantity 복구용 SQL
================================ */
UPDATE coupon_policy p
SET issued_quantity = (
    SELECT COUNT(*)
    FROM coupon c
    WHERE c.coupon_policy_id = p.coupon_policy_id
);
