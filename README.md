# Hệ thống kiến thức Security với Keycloak : Dự án quản lý người dùng sử dụng Spring Boot và Keycloak, cung cấp giải pháp xác thực và phân quyền an toàn và linh hoạt.
## 🚀 Tính Năng Chính
- Đăng ký người dùng
- Xác thực bằng Keycloak
- Phân quyền chi tiết
- Bảo mật API
- Quản lý profile người dùng
## Keycloak  là một giải pháp quản lý danh tính và truy cập (IAM) mã nguồn mở được sử dụng rộng rãi với các tính năng: Xác thực, ủy quyền, quản lý người dùng và bảo mật.
## 📋 Yêu Cầu Hệ Thống
- JDK 17+
- Maven 3.8+
- Keycloak Server
- Docker (tuỳ chọn)
## Step by Step:
* B1 : Sử dụng docker để chạy server keycloak
* B2 : Cần phải tạo các client cho ứng dụng(truy cập http://localhost:8180) để tạo client lấy các cặp client Id client secret chọn vào các như hình
*  ![image](https://github.com/user-attachments/assets/a5753c6d-6ea4-436f-8b8a-b1cd37e70758)
*B3 : Sử dụng để lấy exchange được exchange client token sử dụng endpoint này http://localhost:8180/realms/ducbao/protocol/openid-connect/token
![image](https://github.com/user-attachments/assets/07708d88-b0af-40cb-bfed-88a780edc2f0)
* B4: Sau khi lấy được token thì sử dụng token của client đó để tạo user cho hệ thống trong keycloak và lưu vào database gọi tới endpoint là  http://localhost:8180/admin/realms/ducbao/users
* ![image](https://github.com/user-attachments/assets/cee909e8-30a9-44d3-9810-891f50d1be13)
* B5: Sau khi tạo được thì nó sẽ trả về ResponseEntity để mình lấy được userId trong thằng header của location và set userId cho user và lưu vào database
* ![image](https://github.com/user-attachments/assets/b0449356-ab84-4fca-8be2-df34d159bc9b)
* ![image](https://github.com/user-attachments/assets/5f70bc09-e335-4b69-9c7d-3bf575d16e4e)
* ![image](https://github.com/user-attachments/assets/40820e4a-4a37-4947-94c9-4143ddc3ea22)
* B6: Sau khi tạo được user thì mình sẽ phải lấy được accessToken của keycloak và fe sẽ nhận được access_token từ dưới đây là config của fe.
* ![image](https://github.com/user-attachments/assets/22bbfa5f-4779-404b-9a13-2703dc47bd90)
* B7: Sau khi nhận được access_token thì sẽ gửi lên và backend sẽ xử lí và gọi user theo cái thằng đó trả về kết quả.
* ![image](https://github.com/user-attachments/assets/6f79df72-7ded-4e0b-b0e8-b7e0bf9065a7)
* ![image](https://github.com/user-attachments/assets/c03d457b-7a4b-4da5-85c5-29b4a949ed0a)


  ## Kết luận:
  * Những thứ đã làm được: Hiểu về flow base với keycloak, có thể thao tác được với keycloak
  * Những thứ chưa làm được : Config sso, social login với facebook, google, security với jwt





 
