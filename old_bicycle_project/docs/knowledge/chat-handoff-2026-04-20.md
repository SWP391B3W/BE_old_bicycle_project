# Chat Handoff - 2026-04-20

Tài liệu này tóm tắt những gì đã làm trong cuộc chat để lần sau mở lại là có thể tiếp tục ngay, không cần nhắc lại từ đầu.

## 1) Hạ tầng và deploy

- VPS AWS EC2 đã được cấu hình và chạy app Spring Boot qua `systemd`.
- Nginx đang reverse proxy từ port 80/443 vào app nội bộ port 8080.
- Domain public đang dùng: `https://bemarket.run.place`.
- HTTPS đã bật bằng Let’s Encrypt và có redirect từ HTTP sang HTTPS.
- JAR đang chạy trên VPS nằm ở `/home/ec2-user/marketbike/app.jar`.

## 2) Webhook SePay

- Endpoint webhook: `/api/payments/sepay/webhook`.
- Webhook auth key được đọc từ biến môi trường `SEPAY_WEBHOOK_API_KEY` hoặc fallback `SEPAY_WEBHOOK_KEY`.
- Hiện `.env` trên VPS đang có `SEPAY_MOCK_MODE=true` và `SEPAY_WEBHOOK_API_KEY` để trống.
- Logic webhook chỉ nhận payment hợp lệ khi có `gatewayOrderCode` khớp với payment đang chờ.
- Dữ liệu webhook hợp lệ cần:
  - `transferType = in`
  - `code` hoặc `content` chứa mã `OB-...`
  - `transferAmount` khớp hoặc đủ điều kiện theo payment

## 3) Kết quả test thật đã chạy thành công

- Đã test end-to-end thật trên môi trường production domain.
- Flow thành công:
  1. Login buyer
  2. Lấy order đang `awaiting_payment`
  3. Gọi `/api/payments/orders/{orderId}/upfront-request`
  4. Gửi webhook vào `/api/payments/sepay/webhook`
  5. Kiểm tra payment và order đã đổi trạng thái đúng

- Kết quả cuối:
  - `payment.status = success`
  - `order.fundingStatus = held`
  - `order.status = deposited`

- Lệnh test PowerShell đã pass với `E2E_RESULT=SUCCESS`.

## 4) Account test đã dùng

- `admin@bike.com` / `Fpt@2026`
- `seller@bike.com` / `Fpt@2026`
- `buyer@bike.com` / `Fpt@2026`
- `inspector@bike.com` / `Fpt@2026`

## 5) Base URL cho FE

- FE nên gọi BE qua: `https://bemarket.run.place`
- Không nên dùng IP thô `http://52.4.235.12` nữa.
- Nếu FE có file env, thường đổi một trong các biến sau:
  - `VITE_API_BASE_URL=https://bemarket.run.place`
  - `REACT_APP_API_BASE_URL=https://bemarket.run.place`
  - `NEXT_PUBLIC_API_BASE_URL=https://bemarket.run.place`

## 6) Swagger và API visibility

- Lúc đầu Swagger trên VPS chỉ hiện một phần controller vì đang chạy jar cũ.
- Đã rebuild và redeploy jar mới, sau đó Swagger `/v3/api-docs` hiển thị đầy đủ endpoint hơn.

## 7) GitHub Actions / deploy workflow

- Đã tạo workflow deploy trên GitHub Actions.
- Workflow có các bước chính:
  - build project
  - upload jar lên VPS
  - backup jar cũ
  - restart service
  - health check
  - rollback nếu deploy lỗi

## 8) Một số file đã chỉnh trong BE

- `src/main/resources/application.properties`
  - đồng bộ biến môi trường cho SePay và model AI
- `.github/workflows/deploy.yml`
  - pipeline deploy lên VPS
- Các thay đổi app trước đó đã được split và push riêng
  - product payload compatibility
  - inspection flow
  - payment setter / validation cleanup

## 9) Ghi chú quan trọng cho lần sau

- Nếu webhook trả `Thanh toán không hợp lệ`, thường là do:
  - chưa có payment thật đang chờ
  - `gatewayOrderCode` không khớp
  - token auth webhook sai
  - `transferType` không phải `in`

- Với test thật, nên dùng order đã ở trạng thái `pending` + `awaiting_payment` thay vì tạo webhook giả từ số order code tự bịa.

- Root `/` có thể trả `401` là bình thường do security config.

## 10) Trạng thái hiện tại

- VPS hoạt động bình thường.
- Domain HTTPS hoạt động bình thường.
- Webhook đã test thành công.
- FE cần trỏ base URL về `https://bemarket.run.place`.
