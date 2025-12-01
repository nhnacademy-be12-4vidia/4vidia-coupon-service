
-- 1. 쿠폰_정책 테이블
CREATE TABLE coupon_policy (
                               coupon_policy_id   BIGINT       NOT NULL AUTO_INCREMENT COMMENT '쿠폰정책ID',
                               coupon_policy_name VARCHAR(100) NOT NULL COMMENT '쿠폰정책이름',

                               policy_type        TINYINT      NOT NULL COMMENT '발급조건 (0=WELCOME,1=BIRTHDAY,2=EVENT)',
                               discount_type      TINYINT      NOT NULL COMMENT '할인방식 (0=PRICE,1=RATE)',
                               discount_value     INT          NOT NULL COMMENT '할인값',

                               discount_target_type TINYINT    NOT NULL COMMENT '할인적용대상 (0=ALL,1=CATEGORY,2=BOOK)',
                               category_id        BIGINT       NULL COMMENT '카테고리ID (CATEGORY일 때만 사용)',
                               book_id            BIGINT       NULL COMMENT '북ID (BOOK일 때만 사용)',

                               validity_type      TINYINT      NOT NULL COMMENT '유효기간 타입 (0=RELATIVE,1=ABSOLUTE)',
                               valid_days         INT          NULL COMMENT '상대적유효기간(일)',

                               start_date         DATETIME     NULL COMMENT '절대유효기간 시작',
                               end_date           DATETIME     NULL COMMENT '절대유효기간 종료',

                               limited_quantity   INT          NULL COMMENT '한정수량 (NULL이면 무제한)',
                               issued_quantity    INT          NOT NULL DEFAULT 0 COMMENT '발급된수량',
                               min_order_amount   INT          NOT NULL DEFAULT 0 COMMENT '최소주문금액',
                               max_discount_amount INT         NOT NULL DEFAULT 0 COMMENT '최대할인금액(percentage일 때 사용)',

                               is_activation      TINYINT      NOT NULL DEFAULT 1 COMMENT '활성화여부',

                               PRIMARY KEY (coupon_policy_id)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci;


-- 2. 발급된_쿠폰 테이블
CREATE TABLE coupon (
                        coupon_id       BIGINT    NOT NULL AUTO_INCREMENT COMMENT '쿠폰ID',
                        coupon_policy_id BIGINT   NOT NULL COMMENT '쿠폰정책ID',

                        issued_at       DATETIME  NOT NULL COMMENT '발급일자',
                        expire_at       DATETIME  NOT NULL COMMENT '만료일자',
                        used_at         DATETIME  NULL COMMENT '사용일자',

                        status          TINYINT   NOT NULL DEFAULT 0 COMMENT '상태 (0=UNUSED,1=USED,2=EXPIRED)',

                        PRIMARY KEY (coupon_id),
                        CONSTRAINT fk_coupon_policy
                            FOREIGN KEY (coupon_policy_id) REFERENCES coupon_policy(coupon_policy_id)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci;


-- 3. 유저_보유_쿠폰 테이블 (복합 PK: coupon_id + user_id)
CREATE TABLE user_coupon (
                             coupon_id BIGINT NOT NULL COMMENT '쿠폰ID',
                             user_id   BIGINT NOT NULL COMMENT '유저ID',
                             policy_id BIGINT NOT NULL COMMENT '정책ID (중복방지용)',

                             PRIMARY KEY (coupon_id, user_id),

                             UNIQUE KEY uq_user_policy (policy_id, user_id),

                             CONSTRAINT fk_user_coupon_coupon
                                 FOREIGN KEY (coupon_id) REFERENCES coupon(coupon_id)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci;