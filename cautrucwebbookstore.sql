create database webbookstore;

-- Sử dụng cơ sở dữ liệu
USE webbookstore;

-- Tạo bảng category
CREATE TABLE Category (
  -- Khai báo thuộc tính id kiểu int, không được null, tăng tự động và là khóa chính
  id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  -- Khai báo thuộc tính name kiểu varchar(255), không được null
  name varchar(255) NOT NULL,
  -- Khai báo thuộc tính status kiểu int, không được null
  status INT NOT NULL
);

-- Tạo bảng Author 
CREATE TABLE Author (
  -- Khai báo thuộc tính id kiểu int, không được null, tăng tự động và là khóa chính
  id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  -- Khai báo thuộc tính name kiểu varchar(255), không được null
  name varchar(255) NOT NULL,
  -- Khai báo thuộc tính bio kiểu varchar(255)
  bio text
);

-- Tạo bảng book
CREATE TABLE Book (
  -- Khai báo thuộc tính id kiểu int, không được null, tăng tự động và là khóa chính
  id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  -- Khai báo thuộc tính name kiểu varchar(255), không được null
  name varchar(255) NOT NULL,
  -- Khai báo thuộc tính cost_price kiểu double, không được null
  cost_price double NOT NULL,
  -- Khai báo thuộc tính sell_price kiểu double, không được null
  sell_price double NOT NULL,
  -- Khai báo thuộc tính category_id kiểu int và là khóa ngoại tham chiếu đến bảng Category
  category_id int,
  FOREIGN KEY (category_id) REFERENCES Category(id),
  -- Khai báo thuộc tính publisher kiểu varchar(255), không được null
  publisher varchar(255) NOT NULL,
  -- Khai báo thuộc tính description kiểu text, có thể null
  description text,
  -- Khai báo thuộc tính status kiểu int, không được null
  status INT NOT NULL,
  -- Khai báo thuộc tính detail kiểu text, có thể null
  detail text,
  -- Khai báo thuộc tính quantity kiểu int, không được null
  quantity INT NOT NULL
);

-- Tạo bảng trung gian BookAuthor
CREATE TABLE BookAuthor (
  -- Khai báo thuộc tính id kiểu int, không được null, tăng tự động và là khóa chính
  id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  -- Khai báo thuộc tính book_id kiểu int và là khóa ngoại tham chiếu đến bảng Book
  book_id int,
  FOREIGN KEY (book_id) REFERENCES Book(id),
  -- Khai báo thuộc tính author_id kiểu int và là khóa ngoại tham chiếu đến bảng Author 
  author_id int,
  FOREIGN KEY (author_id) REFERENCES Author(id)
);

-- Tạo bảng BookImage
CREATE TABLE BookImage (
  -- Khai báo thuộc tính id kiểu int, không được null, tăng tự động và là khóa chính
  id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  -- Khai báo thuộc tính book_id kiểu int và là khóa ngoại tham chiếu đến bảng Book
  book_id int,
  FOREIGN KEY (book_id) REFERENCES Book(id),
  -- Khai báo thuộc tính name kiểu varchar(255), không được null 
  name varchar(255) NOT NULL,
  -- Khai báo thuộc tính index kiểu int, không được null 
  position int NOT NULL,
  -- Khai báo thuộc tính path kiểu varchar(255), không được null 
  path varchar(255) NOT NULL
);

-- Tạo bảng account
CREATE TABLE Account (
  -- Khai báo thuộc tính id kiểu int, không được null, tăng tự động và là khóa chính
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  -- Khai báo thuộc tính first_name kiểu varchar(255)
  first_name varchar(255),
  -- Khai báo thuộc tính last_name kiểu varchar(255)
  last_name varchar(255),
  -- Khai báo thuộc tính username kiểu varchar(255), không được null
  username varchar(255) NOT NULL,
  -- Khai báo thuộc tính password kiểu varchar(255), không được null
  password varchar(255) NOT NULL,
  -- Khai báo thuộc tính gender kiểu varchar(255)
  gender varchar(255),
  -- Khai báo thuộc tính img kiểu char(255)
  img char(255),
  -- Khai báo thuộc tính date_of_birth kiểu date
  date_of_birth DATE,
  -- Khai báo thuộc tính address kiểu varchar(255), không được null
  address VARCHAR(255) NOT NULL,
  -- Khai báo thuộc tính phone_number kiểu varchar(255), không được null
  phone_number varchar(255) NOT NULL,
  -- Khai báo thuộc tính email kiểu char(255), không được null
  email char(255) NOT NULL,
  -- Khai báo thuộc tính account_type kiểu int, không được null
  account_type INT NOT NULL,
  -- Khai báo thuộc tính status kiểu int, không được null
  status INT NOT NULL
);

-- Tạo bảng Cart
CREATE TABLE Cart (
  -- Khai báo thuộc tính id kiểu int, không được null, tăng tự động và là khóa chính
  id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  -- Khai báo thuộc tính account_id kiểu int, và là khóa ngoại tham chiếu đến bảng Account 
  account_id INT,
  FOREIGN KEY (account_id) REFERENCES Account(id)
);

-- Tạo bảng CartItem
CREATE TABLE CartItem (
  -- Khai báo thuộc tính id kiểu int, không được null, tăng tự động và là khóa chính
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  -- Khai báo thuộc tính quantity kiểu int, không được null
  quantity INT NOT NULL,
  -- Khai báo thuộc tính cart_id kiểu int và là khóa ngoại tham chiếu đến bảng Cart
  cart_id INT,
  FOREIGN KEY (cart_id) REFERENCES Cart(id),
  -- Khai báo thuộc tính book_id kiểu int và là khóa ngoại tham chiếu đến bảng Book
  book_id INT,
  FOREIGN KEY (book_id) REFERENCES Book(id)
);

-- Tạo bảng Order
CREATE TABLE `Order` (
	-- Khai báo thuộc tính id kiểu int, không được null, tăng tự động và là khóa chính
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	-- Khai báo thuộc tính date_order kiểu Date, không được null
	date_order DATE NOT NULL,
	-- Khai báo thuộc tính total_price kiểu double, không được null
	total_price DOUBLE NOT NULL,
	-- Khai báo thuộc tính name kiểu varchar(255), không được null
	name VARCHAR(255) NOT NULL,
	-- Khai báo thuộc tính address kiểu varchar(255), không được null
	address VARCHAR(255) NOT NULL,
	-- Khai báo thuộc tính phone_number kiểu varchar(20), không được null
	phone_number VARCHAR(20) NOT NULL,
	-- Khai báo thuộc tính email kiểu char(255), không được null
	email CHAR(255) NOT NULL,
	-- Khai báo thuộc tính account_id kiểu int và là khóa ngoại tham chiếu đến bảng Account
	account_id INT,
	FOREIGN KEY (account_id) REFERENCES Account(id),
	-- Khai báo thuộc tính status kiểu int, không được null
	status INT NOT NULL
);


-- Tạo bảng OrderItem
CREATE TABLE OrderItem (
  -- Khai báo thuộc tính id kiểu int, không được null, tăng tự động và là khóa chính
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  -- Khai báo thuộc tính quantity kiểu int, không được null
  quantity INT NOT NULL,
  -- Khai báo thuộc tính price kiểu double, không được null
  price double NOT NULL,
  -- Khai báo thuộc tính book_id kiểu int và là khóa ngoại tham chiếu đến bảng Book
  book_id INT,
  FOREIGN KEY (book_id) REFERENCES Book(id),
  -- Khai báo thuộc tính order_id kiểu int và là khóa ngoại tham chiếu đến bảng Order
  order_id INT,
  FOREIGN KEY (order_id) REFERENCES `Order`(id)
);

-- Thêm dữ liệu vào bảng Category
INSERT INTO Category (name, status) VALUES ("CHÍNH TRỊ - PHÁP LUẬT", 1);
INSERT INTO Category (name, status) VALUES ("GIÁO TRÌNH", 1);
INSERT INTO Category (name, status) VALUES ("KHOA HỌC CÔNG NGHỆ - KINH TẾ", 1);
INSERT INTO Category (name, status) VALUES ("TRUYỆN, TIỂU THUYẾT", 1);
INSERT INTO Category (name, status) VALUES ("TÂM LÝ, TÂM LINH, TÔN GIÁO", 1);
INSERT INTO Category (name, status) VALUES ("THIẾU NHI", 1);
INSERT INTO Category (name, status) VALUES ("VĂN HỌC NGHỆ THUẬT", 1);
INSERT INTO Category (name, status) VALUES ("VĂN HÓA XÃ HỘI - LỊCH SỬ", 1);

-- Thêm dữ liệu vào bảng Account
-- admin 
INSERT INTO Account(first_name, last_name, username, password, gender, img, date_of_birth, address, phone_number, email, account_type, status)
VALUES ("Minh", "Phạm", "phamthiminh", "$2a$10$lUfNHigfLAxtdO5TeYKf1ezzqdTquqtIL/HkdyD4LU0wlixIfpAZG", "Nữ", "", "2002-03-03", "Vũng Tàu", "0388394909", "minh09@gmail.com", 0, 1);
INSERT INTO Account(first_name, last_name, username, password, gender, img, date_of_birth, address, phone_number, email, account_type, status)
VALUES ("Mai", "Lâm", "lamngocmai", "$2a$10$ecLWKUctvRxMJoZLWHiwyuH8tGP7LFTrNEDoTiNsvkJOsAOZJh6eG", "Nữ", "", "2002-04-13", "Trà Vinh", "057399379", "mai79@gmail.com", 0, 1);
-- customer 
INSERT INTO Account(first_name, last_name, username, password, gender, img, date_of_birth, address, phone_number, email, account_type, status)
VALUES ("A", "Nguyễn", "nguyenvana", "$2a$10$2e.U.VeBZI9AI9M4HLpHeevfHaBn5fLHBILe6TIpTAORGKV/fa9wq", "Nam", "", "2002-06-10", "TP.HCM", "0388156408", "a08@gmail.com", 1, 1);
INSERT INTO Account(first_name, last_name, username, password, gender, img, date_of_birth, address, phone_number, email, account_type, status)
VALUES ("Sang", "Nguyễn", "nguyenvansang", "$2a$10$EOAYipGx4YVsEPCzLxXgFu/rlFA5R93lp6DPwjssLj.OGFYRKENHm", "Nam", "", "2002-09-10", "An Giang", "0288199567", "sang67@gmail.com", 1, 1);
INSERT INTO Account(first_name, last_name, username, password, gender, img, date_of_birth, address, phone_number, email, account_type, status)
VALUES ("Hạ", "Trần", "tranthiha", "$2a$10$fbmKZXKvk4APdj4p6becc.RHSG1bIKpjsNyQl6ml.OEx3.zfWqGL.", "Nữ", "", "2003-10-10", "Bến Tre", "0788467389", "ha89@gmail.com", 1, 1);
INSERT INTO Account(first_name, last_name, username, password, gender, img, date_of_birth, address, phone_number, email, account_type, status)
VALUES ("Tươi", "Trần", "tranvantuoi", "$2a$10$i30yFjhiZywZbfshpgiIA.MoDHert9OvSD7ZKXcqTvDO28AxZFh2y", "Nam", "", "2000-02-07", "Quảng Ngãi", "0977767587", "tuoi87@gmail.com", 1, 1);
INSERT INTO Account(first_name, last_name, username, password, gender, img, date_of_birth, address, phone_number, email, account_type, status)
VALUES ("Hoa", "Phan", "phanthihoa", "$2a$10$4cU9CFgSw1.pbTB/GbODUuzlGQLOGB2/O58Yyi1nygMEzWXDdNyEW", "Nữ", "", "1999-09-09", "Bắc Ninh", "0578695748", "hoa48@gmail.com", 1, 1);
INSERT INTO Account(first_name, last_name, username, password, gender, img, date_of_birth, address, phone_number, email, account_type, status)
VALUES ("Anh", "Nguyễn", "nguyendieuanh", "$2a$10$V0nioZSN5WopWcXyxAta3uTHB2v/ehixJ3GQV.4c35jfiYl733lKW", "Nữ", "", "1998-02-11", "Bắc Kạn", "0478399293", "anh93@gmail.com", 1, 1);
INSERT INTO Account(first_name, last_name, username, password, gender, img, date_of_birth, address, phone_number, email, account_type, status)
VALUES ("Bích", "Lê", "lengocbich", "$2a$10$cVxqCvC9fVqEfGd8jkFwXOPj0rcCMDLrxCMfcWmf/aVMHFgWXq75u", "Nữ", "", "1999-03-12", "Lâm Đồng", "0747749829", "bich29@gmail.com",1, 1);
INSERT INTO Account(first_name, last_name, username, password, gender, img, date_of_birth, address, phone_number, email, account_type, status)
VALUES ("Dung", "Đỗ", "domydung", "$2a$10$vwwuS0KBK4qdqZkBAzp8zuFrnedxMcInQPaHTRD.bYLQ7VeQxm1dq", "Nữ", "", "2002-08-04", "Long An", "0747483974", "dung74@gmail.com", 1, 1);

-- Thêm dữ liệu vào bảng Author
INSERT INTO Author(name, bio)
VALUES ("Quốc Hội", "");
INSERT INTO Author(name, bio)
VALUES ("LS Trương Thanh Đức", "");
INSERT INTO Author(name, bio)
VALUES ("Khánh Linh", "");
INSERT INTO Author(name, bio)
VALUES ("Joseph Murphy", "");
INSERT INTO Author(name, bio)
VALUES ("Aristotle", "");
INSERT INTO Author(name, bio)
VALUES ("Trần Nhựt Tân", "");
INSERT INTO Author(name, bio)
VALUES ("Lê Thái Dũng", "");
INSERT INTO Author(name, bio)
VALUES ("Ian Bremmer", "");
INSERT INTO Author(name, bio)
VALUES ("Kate Andersen Brower", "");
INSERT INTO Author(name, bio)
VALUES ("Trần Công Diêu", "");
INSERT INTO Author(name, bio)
VALUES ("Phan Văn Trường", "");
INSERT INTO Author(name, bio)
VALUES ("Andrea Hirata", "");

-- Thêm dữ liệu vào bảng Book  
INSERT INTO Book(name, cost_price, sell_price, category_id, publisher, description, status, detail, quantity)
VALUES ("Luật Sở Hữu Trí Tuệ (Hiện Hành) (Sửa Đổi, Bổ Sung Năm 2009, 2022)", 70000, 78000, 1, 
"NXB Chính Trị Quốc Gia", 
'"Luật Sở Hữu Trí Tuệ"
Nội dung cuốn sách là văn bản Luật Đấu thầu năm 2013 được nhất thể hóa với Luật sửa đổi, bổ sung Điều 6 và Phụ lục 4 về Danh mục ngành, nghề đầu tư kinh doanh có điều kiện của Luật đầu tư; Luật hỗ trợ doanh nghiệp nhỏ và vừa năm 2017; Luật kiến trúc năm 2019 và Luật đầu tư theo phương thức đối tác công tư năm 2020, giúp bạn đọc thuận tiện tra cứu, tham khảo.',
 1, "Tên Nhà Cung Cấp:	NXB Chính Trị Quốc Gia
Tác giả:	Quốc Hội
NXB:	Chính Trị Quốc Gia Sự Thật
Năm XB:	2022
Ngôn Ngữ:	Tiếng Việt
Trọng lượng (gr):	220
Kích Thước Bao Bì:	19 x 13 x 1 cm
Số trang:	275
Hình thức:	Bìa Mềm", 100);
INSERT INTO Book(name, cost_price, sell_price, category_id, publisher, description, status, detail, quantity)
VALUES ("Luật Doanh Nghiệp (Hiện Hành) (Sửa Đổi, Bổ Sung Năm 2022)", 120000, 122000, 1,
"NXB Chính Trị Quốc Gia", 
'"Luật Doanh Nghiệp"
Luật Doanh nghiệp (hiện hành) (sửa đổi, bổ sung năm 2022) gồm 10 chương, 218 điều, quy định về thành lập, tổ chức quản lý, tổ chức lại, giải thể và hoạt động có liên quan của doanh nghiệp, bao gồm công ty trách nhiệm hữu hạn, công ty cổ phần, công ty hợp danh và doanh nghiệp tư nhân; quy định về nhóm công ty. Luật Doanh nghiệp được Quốc hội khóa XIV kỳ họp thứ 9 thông qua ngày 17/6/2020, thay thế cho Luật Doanh nghiệp năm 2014.
Ngày 11/1/2022, tại kỳ họp bất thường lần thứ nhất, Quốc hội khóa XV đã thông qua Luật sửa đổi, bổ sung một số điều của Luật Đầu tư công, Luật Đầu tư theo phương thức đối tác công tư, Luật Đầu tư, Luật Nhà ở, Luật Đấu thầu, Luật Điện lực, Luật Doanh nghiệp, Luật Thuế tiêu thụ đặc biệt và Luật Thi hành án dân sự, trong đó có nội dung sửa đổi, bổ sung một số điều của Luật Doanh nghiệp năm 2020.
Cuốn sách Luật Doanh nghiệp (hiện hành) (sửa đổi, bổ sung năm 2022) là văn bản Luật hợp nhất của Luật Doanh nghiệp năm 2020 và các nội dung của lần sửa đổi năm 2022, tạo thành một văn bản Luật Doanh nghiệp thống nhất, hiện hành và mới nhất, thuận tiện trong sử dụng, tra cứu và học tập.', 
1, "Tên Nhà Cung Cấp:	NXB Chính Trị Quốc Gia
Tác giả:	Quốc Hội
NXB:	Chính Trị Quốc Gia Sự Thật
Năm XB:	2023
Trọng lượng (gr):	237
Kích Thước Bao Bì:	19 x 13 x 1.2 cm
Số trang:	295
Hình thức:	Bìa Mềm", 100);
INSERT INTO Book(name, cost_price, sell_price, category_id, publisher, description, status, detail, quantity)
VALUES ("Kinh Doanh Sành Luật (Ứng Dụng Luật Doanh Nghiệp Năm 2020, Sửa Đổi, Bổ Sung Năm 2022 Và Quy Định Liên Quan)", 190000, 195000, 1, 
"NXB Chính Trị Quốc Gia", 
'"Kinh Doanh Sành Luật "
Trong bối cảnh xã hội hiện nay, xu hướng toàn cầu hóa, hội nhập quốc tế, cuộc Cách mạng công nghiệp lần thứ 4 và đặc biệt là dịch bệnh Covid-19 đã tác động nhiều chiều đến nền kinh tế Việt Nam cũng như hoạt động kinh doanh của các công ty. Để tận dụng lợi thế cũng như hạn chế các rủi ro trong hoạt động sản xuất - kinh doanh thì những người lãnh đạo, quản lý công ty, các luật sư tư vấn cần phải đưa ra các giải pháp pháp lý chính xác, kịp thời. Muốn vậy, chỉ hiểu biết pháp luật thôi thì chưa đủ, những người lãnh đạo, quản lý công ty, các luật sư tư vấn còn cần phải nắm vững và làm chủ được hoàn cảnh bằng cách vận dụng linh hoạt các quy định của pháp luật, phải trở thành những chuyên gia, những người “sành sỏi”, hiểu biết pháp luật nhằm đem lại hiệu quả cao nhất trong hoạt động kinh doanh. Xuất phát từ lý do này, Luật sư Trương Thanh Đức đã dành tâm huyết nghiên cứu và biên soạn cuốn sách Kinh doanh sành luật (Ứng dụng Luật Doanh nghiệp năm 2020 và quy định liên quan).
Cuốn sách được tác giả biên soạn dựa trên cơ sở kế thừa nội dung cuốn sách Luận giải về Luật Doanh nghiệp hiện hành (36 kế sách pháp lý của doanh nghiệp). Nội dung cuốn sách tổng hợp luận những điều khoản quan trọng của Luật Doanh nghiệp - 2020 cũng như các quy định liên quan giúp người đọc hệ thống hóa, nắm bắt được một cách toàn diện và khoa học các quy định pháp luật liên quan đến các vấn đề, vướng mắc thường gặp trong hoạt động đầu tư, kinh doanh. Đặc biệt, người đọc sẽ tìm thấy ở cuốn sách những điểm cần lưu ý và những giải pháp mang tính gợi mở cho các vấn đề cụ thể từng xảy ra trong thực tiễn. Bên cạnh đó, bằng việc so sánh giữa các quy định pháp luật qua các lần sửa đổi, bổ sung, người đọc sẽ có được cái nhìn hệ thống về tiến trình thay đổi, phát triển của Luật Doanh nghiệp nói riêng, pháp luật Việt Nam nói chung', 
1, "Tên Nhà Cung Cấp:	NXB Chính Trị Quốc Gia
Tác giả:	LS Trương Thanh Đức
NXB:	Chính Trị Quốc Gia Sự Thật
Năm XB:	2023
Trọng lượng (gr):	540
Kích Thước Bao Bì:	24 x 16 x 2.6 cm
Số trang:	524
Hình thức:	Bìa Mềm", 100);
INSERT INTO Book(name, cost_price, sell_price, category_id, publisher, description, status, detail, quantity)
VALUES ("Tủ Sách Bác Hồ - Trung Thu Cháu Nhớ Bác Hồ", 40000, 47000, 1,
"NXB Lao Động", 
'"Tủ Sách Bác Hồ - Trung Thu Cháu Nhớ Bác Hồ"
Nằm trong "Tủ Sách Bác Hồ", Cuốn sách “Trung Thu Cháu Nhớ Bác Hồ”, là một tác phẩm viết về Bác Hồ và những câu chuyện của Bác với các em thiếu nhi. Cuốn sách do Nhà sách Tân Việt và NXB Văn Học liên kết hợp tác phát hành. Sách được xuất bản lần đầu năm 2017 và đã được công chúng nồng nhiệt đón nhận. Cuốn sách đã trở nên thân thiết với các bạn đọc với nhiều lứa tuổi khác nhau, đặc biệt là các cháu thiếu niên.', 
1, "Tên Nhà Cung Cấp:	Tân Việt
Tác giả:	Khánh Linh
NXB:	Lao Động
Năm XB:	2022
Trọng lượng (gr):	140
Kích Thước Bao Bì:	20.5 x 13.5 x 0.8 cm
Số trang:	192
Hình thức:	Bìa Mềm", 100);
INSERT INTO Book(name, cost_price, sell_price, category_id, publisher, description, status, detail, quantity)
VALUES ("Luật Hấp Dẫn Và Sức Mạnh Tiềm Thức", 130000, 139000, 1,
"NXB Lao Động", 
'"Luật Hấp Dẫn Và Sức Mạnh Tiềm Thức"
Nguồn gốc của phản ứng là tiềm thức và tư duy, và như luật hấp dẫn đã khẳng định một cách chắc chắn, chính tiềm thức và tư duy đang điều khiển cuộc sống của bạn.
Người giàu có và kẻ nghèo khổ, người thành công và kẻ thất bại, người sáng chói giữa vầng hào quang và kẻ thảm hại xám xịt ảm đạm, một thiên tài hay kẻ nằm ở dưới đáy… tất cả bọn họ đều chỉ khác nhau ở duy nhất một điểm. Đó là có người đã nhận ra, đồng thời ứng dụng rất tốt luật hấp dẫn trong cuộc sống của mình, một số khác thì không.
Trong cuốn sách này, tác giả sẽ đưa ra cho bạn những lời khuyên thực tế và hữu ích về:
Cách cư xử, ứng xử và giao tiếp với mọi người để giúp bạn sử dụng nhuần nhuyễn luật hấp dẫn, nâng cao sức mạnh của tiềm thức.
Phương pháp khơi nguồn và tận dụng mọi khả năng của mình, để ngày càng trở nên giàu có hơn và đạt được thành công trong cuộc sống.
Nếu như bạn muốn hiểu về luật hấp dẫn, muốn biết cách dựa vào nó để tìm thấy cảm hứng, niềm vui và nguồn năng lượng tích cực, chắc chắn Ứng dụng luật hấp dẫn là một sự lựa chọn sáng suốt dành cho bạn.', 
1, "Tên Nhà Cung Cấp:	1980 Books
Tác giả:	Joseph Murphy
Người Dịch:	Dương Bùi
NXB:	Lao Động
Năm XB:	2023
Ngôn Ngữ:	Tiếng Việt
Trọng lượng (gr):	200
Kích Thước Bao Bì:	19 x 13 x 0.9 cm
Số trang:	180
Hình thức:	Bìa Mềm", 100);
INSERT INTO Book(name, cost_price, sell_price, category_id, publisher, description, status, detail, quantity)
VALUES ("Chính Trị Luận (Tái Bản 2023)", 180000, 189000, 1,
"NXB Thế Giới", 
'"Chính Trị Luận"
Tác phẩm nổi tiếng viết về các khái niệm mà từ đó định hình các quốc gia và chính phủ. Mặc dù, Aristotle cổ vũ mạnh mẽ cho chế độ nô lệ lạc hậu, quan điểm của ông về Hiến pháp và cách điều hành chính phủ lại rất kinh điển. Dù chỉ thảo luận về nhà nước và các định chế thời Hy Lạp cổ nhưng tác phẩm này của ông đã đặt nền tảng cho khoa học chính trị hiện đại.
Tác phẩm này được xem là căn bản cho Chính trị học Tây phương. Chính trị luận nghiên cứu các vấn đề cơ bản về nhà nước, chính quyền, chính trị, tự do, công bằng, tài sản, quyền, luật và việc thực thi luật pháp của các cơ quan thẩm quyền.
Aristotle là biểu tượng của trí tuệ tư duy triết học. Mặc dù nội dung rất sâu sắc nhưng cách trình bày của ông lại rất dễ hiểu. Ông viết những suy nghĩ của mình một cách rõ ràng với độ chính xác siêu phàm. Học thuyết của ông có ảnh hưởng lớn đến những lĩnh vực hiện đại như : khoa học, chủ nghĩa duy thực và logic học
Theo Aristotle, là một người tốt không thôi chưa đủ. Nếu người dân tốt mà không tích cực tham gia vào đời sống chính trị của chế độ thì chế độ đó có cơ nguy trở thành thoái hóa và trở thành một chế độ xấu. – Dịch giả Nông Duy Trường', 
1, "Tên Nhà Cung Cấp:	Alpha Books
Tác giả:	Aristotle
Người Dịch:	Nông Duy Trường
NXB:	Thế Giới
Năm XB:	2023
Ngôn Ngữ:	Tiếng Việt
Trọng lượng (gr):	474
Kích Thước Bao Bì:	20.5 x 14 x 2 cm
Số trang:	438
Hình thức:	Bìa Mềm", 100);
INSERT INTO Book(name, cost_price, sell_price, category_id, publisher, description, status, detail, quantity)
VALUES ("Nghệ Thuật Suy Tưởng - Luận Lí Học Tổng Quát Và Phương Pháp Luận", 90000, 98000, 1,
"NXB Hồng Đức", 
'"Nghệ Thuật Suy Tưởng - Luận Lí Học Tổng Quát Và Phương Pháp Luận"
Con người sống là hành động: hành động để thỏa mãn nhu cầu thực tiễn hay để thỏa mãn lòng hiếu tri. Với khả năng giới hạn, khi hành động, họ lại thường gặp thất bại, hiểu biết lại thường sai lầm. Sau nhiều lần “thử” và “lầm” mà trí tuệ thức tỉnh, vẫn tra hỏi, vẫn tìm hiểu những tương quan khách quan: đó là lúc khoa học thực sự hình thành.
NGHỆ THUẬT SUY TƯỞNG tóm lược nhiều học thuyết triết học về các nguyên tắc, quy luật hướng dẫn cho việc suy tưởng đúng, nhất là luận lí học và nhận thức luận. Qua những phần được trình bày rất rõ ràng và dễ hiểu, tác giả đã thành công đưa những tri thức triết học và logic học khô khan, khó hiểu thành một đường dây xuyên suốt giúp người đọc dễ nắm bắt và trau dồi được nghệ thuật suy tưởng.', 
1, "Tên Nhà Cung Cấp:	Cty Văn Hóa Văn Lang
Tác giả:	Trần Nhựt Tân
NXB:	Hồng Đức
Năm XB:	2022
Trọng lượng (gr):	249
Kích Thước Bao Bì:	21 x 13.5 x 0.9 cm
Số trang:	231
Hình thức:	Bìa Mềm", 100);
INSERT INTO Book(name, cost_price, sell_price, category_id, publisher, description, status, detail, quantity)
VALUES ("Chủ Quyền Biển Đảo Việt Nam Xưa Và Nay", 70000, 72000, 1,
"NXB Hồng Đức", 
'"Chủ Quyền Biển Đảo Việt Nam Xưa Và Nay"
Quyển sách "Chủ quyền biển đảo Việt Nam xưa và nay" của tác giả Lê Thái Dũng được xuất bản bởi nhà xuất bản Hồng Đức vào năm 2020, gồm 216 trang, khổ 21cm. Sách tập hợp nhiều bài viết khác nhau, được trình bày theo lối lịch sử sắp xếp logic theo từng giai đoạn. Sách là một tài liệu quý khi đã khái quát và đưa ra được một số bằng chứng pháp lý từ thời phong kiến Việt Nam đã thực thi chủ quyền trên hai quần đảo Hoàng Sa và Trường Sa. Tác giả còn miêu tả rất cụ thể lực lượng thuỷ quân của Việt Nam trải qua từng thời kỳ, có những thăng tầm biến đổi lúc thịnh, lúc suy nhưng vẫn đảm bảo thực thi chủ quyền của mình. Sách “Chủ quyền biển đảo Việt Nam xưa và nay” chứa đựng nhiều kiến thức bổ ích và quý bảo về biển đảo Việt Nam, được tác giả viết rất dễ hiểu không nhàm chán, khô khan, ngược lại rất hay và giàu tính văn học nghệ thuật. Sách đang có tại Thư viện Nguyễn Đình Chiểu tỉnh Bến Tre.', 
1, "Tên Nhà Cung Cấp:	CÔNG TY CỔ PHẦN SÁCH TRÍ THỨC VIỆT
Tác giả:	Lê Thái Dũng
NXB:	NXB Hồng Đức
Năm XB:	2020
Trọng lượng (gr):	220
Kích Thước Bao Bì:	20.5 x 13 cm
Số trang:	215
Hình thức:	Bìa Mềm", 100);
INSERT INTO Book(name, cost_price, sell_price, category_id, publisher, description, status, detail, quantity)
VALUES ("Siêu Cường - Ba Lựa Chọn Về Vai Trò Của Hoa Kỳ Đối Với Thế Giới", 135000, 140000, 1,
"NXB Trẻ", 
'"Siêu Cường - Ba Lựa Chọn Về Vai Trò Của Hoa Kỳ Đối Với Thế Giới"
Trong tương lai gần, Hoa Kỳ vẫn sẽ là siêu cường duy nhất trên thế giới. Nhưng đó là kiểu siêu cường như thế nào? Hoa Kỳ sẽ đóng vai trò gì trên thế giới? Và bạn muốn Hoa Kỳ đóng vai trò gì?
Trong tác phẩm Siêu cường, Ian Bremmer lập luận rằng chính sách đối ngoại thiếu định hướng của Washington vừa tốn kém lại nguy hiểm. Kể từ khi kết thúc chiến tranh lạnh, Mỹ đã sẩy chân trong nhiều cuộc khủng hoảng ở Afghanistan, Iraq, Iran, Libya, Syria và Ukraine mà không có chiến lược rõ ràng. Bremmer khuyến khích chúng ta nghĩ kĩ hơn về vai trò mà nước Mỹ nên đảm nhiệm và cách sử dụng địa vị siêu cường, với ba lựa chọn khác biệt:
Nước Mỹ độc lập: Nước Mỹ không nên nhận trách nhiệm giải quyết vấn đề của dân tộc khác mà nên lãnh đạo bằng cách nêu gương.
Nước Mỹ thực dụng: Nước Mỹ không thể giải quyết mọi thách thức quốc tế nhưng nước Mỹ có thể và nên tập trung vào các cơ hội và bảo vệ lợi ích của mình khi chúng bị đe dọa.
Nước Mỹ thiết yếu: Sự ổn định toàn cầu đang ngày càng phụ thuộc vào những giá trị mà chỉ có nước Mỹ mới bảo vệ được. Người Mỹ không bao giờ có thể sống trong hòa bình nếu các dân tộc khác bị từ chối các quyền tự do cơ bản nhất.
Có nhiều lập luận vững chắc để ủng hộ và phản đối mỗi lựa chọn, nhưng nước Mỹ cần đưa ra lựa chọn của mình.
Sách cũng giúp độc giả hiểu thêm về đường hướng của tổng thống Donald Trump trong chính sách ngoại giao và điều phối kinh tế của nước Mỹ hiện nay.', 
1, "Tên Nhà Cung Cấp:	NXB Trẻ
Tác giả:	Ian Bremmer
Người Dịch:	Phạm Trang Nhung
NXB:	NXB Trẻ
Năm XB:	2019
Trọng lượng (gr):	360
Kích Thước Bao Bì:	13 x 20.5
Số trang:	350
Hình thức:	Bìa Mềm", 100);
INSERT INTO Book(name, cost_price, sell_price, category_id, publisher, description, status, detail, quantity)
VALUES ("Nhà Trắng - Những Chuyện Chưa Kể", 120000, 125000, 1,
"NXB Trẻ", 
'"Nhà Trắng - Những Chuyện Chưa Kể"
Cuốn cuốn sách về cuộc sống công tư của các gia đình tổng thống Mỹ, từ gia đình Kennedy, Clinton, đến gia đình Obama. Sách được viết dựa trên hàng trăm tiếng đồng hồ phỏng vấn các nhân viên phục vụ, hầu phòng, bếp trưởng, thợ cắm hoa, gác cửa và các nhân viên khác trong Nhà Trắng. Qua giọng kể của chính các nhân viên làm việc trong tư dinh tổng thống, sách mang đến cho người đọc cái nhìn sâu sắc về cuộc sống hàng ngày và những thách thức mà các gia đình tổng thống Mỹ phải đối mặt. Từ những khoảnh khắc thân mật trong cuộc sống hôn nhân của gia đình Kennedy, vụ lùm xùm xoay quanh Bill và Hillary Clinton, cho đến sự hiện diện lịch sử mang tính đột phá của Barack và Michelle Obama trong Nhà Trắng. Cuốn sách này không chỉ mang lại kiến thức về lịch sử Mỹ mà còn giúp người đọc hiểu rõ hơn về cuộc sống thực sự bên trong Nhà Trắng.
•    Vợ chồng Tổng thống Kennedy – từ khoảnh khắc thân mật gần gũi trong đời sống hôn nhân của họ đến quãng thời gian hỗn loạn sau ngày Tổng thống Kennedy bị ám sát.
•    Vợ chồng Tổng thống Johnson – nêu bật câu chuyện kỳ quái về nỗi ám ảnh của Tổng thống Johnson với hệ thống vòi sen trong Nhà Trắng.
•    Vợ chồng Tổng thống Nixon – kể cả sự xuất hiện bất ngờ của ông Richard Nixon trong căn bếp Nhà Trắng buổi sáng ngày ông từ chức.
•    Vợ chồng Tổng thống Reagan – từ đám cháy gây nguy hiểm cho ông Ronald Reagan ở cuối nhiệm kỳ hai của ông đến việc bà Nancy kiểm soát mọi chi tiết từ lớn đến bé trong Nhà Trắng.
•    Vợ chồng Tổng thống Clinton – cặp vợ chồng khiến các gia nhân đứng ngồi không yên với những trận cãi vã ác liệt cùng những màn chén bay đĩa bay của họ.
•    Vợ chồng Tổng thống Obama – cặp vợ chồng cùng khiêu vũ theo một bài hát của nữ ca sĩ da đen Mary J. Blige trong đêm đầu tiên dọn vào Nhà Trắng.', 
1, "Tác giả:	Kate Andersen Brower_ Kim Anh dịch
Người Dịch:	Nguyễn Thị Kim Anh
NXB:	NXB Trẻ
Năm XB:	2018
Trọng lượng (gr):	400
Kích Thước Bao Bì:	15.5 x 23 x 2
Số trang:	320
Hình thức:	Bìa Mềm", 100);
INSERT INTO Book(name, cost_price, sell_price, category_id, publisher, description, status, detail, quantity)
VALUES ("Super Môn Toán - Tập 1: Hình Học Không Gian 11&12", 900000, 99000, 2,
"NXB Đại học Quốc gia Hà Nội", 
'"Super Môn Toán - Tập 1: Hình Học Không Gian 11&12"
Với mong muốn giúp học sinh yêu thích và giải quyết tốt các bài toán hình học không gian trong các đề thi tốt nghiệp và đề thi học sinh giỏi, chúng tôi xin giới thiệu đến các bạn đọc cuốn sách: Super Môn Toán - Tập 1: Hình Học Không Gian 11&12. Cuốn sách bao gồm 9 chủ đề:
Trong mỗi chủ đề các tác giả nêu ra một số các định nghĩa, định lí cơ bản trong Sách giáo khoa. Sau đó là phương pháp giải các dạng toán thường xuyên xuất hiện trong các đề thi tốt nghiệp Trung học Phổ thông. Hệ thống các bài tập từ dễ đến khó và tổng hợp tất cả các bài toán hình không gian trong các đề thi Tốt nghiệp THPT và trong các đề thi đại học gần đây.
Với nội dung súc tích, các tác giả đã cố gắng sắp xếp, lựa chọn các bài toàn tiêu biểu cho từng dạng bài toán tương ứng với các câu hỏi về hình học. Chúng tôi hi vọng cuốn sách này là một tài liệu thiết thực, bổ ích cho người dạy học và học, đặc biệt là các em học sinh yêu thích Hình học không gian và chuẩn bị thi tốt nghiệp THPT hay học sinh giỏi.', 
1, "Tác giả: Trần Công Diêu
Nhà xuất bản: NXB Đại học Quốc gia Hà Nội
Nhà phát hành: TCDBooks
Khối lượng: 600.00 gam
Ngôn ngữ: Tiếng Việt
Định dạng: Bìa mềm
Kích thước: 23.5 x 16.5 cm
Ngày phát hành: 01/2021
Số trang: 392", 100);
INSERT INTO Book(name, cost_price, sell_price, category_id, publisher, description, status, detail, quantity)
VALUES ("Cơn Lốc Quản Trị - Ba Trụ Cột Của Văn Hóa Doanh Nghiệp", 105000, 110000, 3,
"NXB Trẻ", 
'"Cơn Lốc Quản Trị - Ba Trụ Cột Của Văn Hóa Doanh Nghiệp"
Doanh nghiệp nào cũng có lúc gặp phải những vấn đề không nhất thiết mang tính kỹ thuật, như sự thiếu vắng động lực, hoặc tinh thần tương tác và làm việc nhóm thấp, thậm chí là mâu thuẫn giữa các thành viên, trong đó có cả các lãnh đạo cấp trung và cấp cao. Chẳng công cụ quản lý nào cho phép giải quyết được những vấn đề bắt nguồn từ sự ganh tị, đố kị, thiên vị, hoặc tệ hơn nữa là nạn bè đảng hay tham nhũng nội bộ.
Các mô hình quản lý chỉ mang lý luận kỹ thuật cục bộ hạn hẹp và những giải pháp cấu trúc có sẵn cho doanh nghiệp. Chỉ văn hóa mới có khả năng vào sâu một cách uyển chuyển các vấn đề trong mối quan hệ giữa người với người. Và đây chính là những vấn đề mà các doanh nghiệp Việt Nam thường gặp phải.
Trong tác phẩm mới nhất về văn hóa doanh nghiệp, tác giả Phan Văn Trường phát triển và đi sâu hơn nữa về chủ đề khá trừu tượng này. Vẫn giữ nguyên phong cách tiếp cận dựa trên những câu chuyện từ thực tế trải nghiệm của mình, ông dẫn dắt độc giả trên con đường tìm hiểu vai trò của văn hóa doanh nghiệp, đồng thời đi sâu giải thích ba phong cách văn hóa mà chính ông đã tiên phong áp dụng cho những doanh nghiệp mình từng tham gia quản trị.', 
1, "Tên Nhà Cung Cấp:	NXB Trẻ
Tác giả:	Phan Văn Trường
NXB:	Trẻ
Năm XB:	2023
Ngôn Ngữ:	Tiếng Trung
Trọng lượng (gr):	260
Kích Thước Bao Bì:	23 x 15.5 x 1.2 cm
Số trang:	248
Hình thức:	Bìa Mềm", 100);
INSERT INTO Book(name, cost_price, sell_price, category_id, publisher, description, status, detail, quantity)
VALUES ("Chiến Binh Cầu Vồng (Tái Bản 2020)", 109000, 119000, 4,
"NXB Hội Nhà Văn", 
'"Chiến Binh Cầu Vồng (Tái Bản 2020)"
“Thầy Harfan và cô Mus nghèo khổ đã mang đến cho tôi tuổi thơ đẹp nhất, tình bạn đẹp nhất, và tâm hồn phong phú, một thứ gì đó vô giá, thậm chí còn có giá trị hơn những khao khát mơ ước. Có thể tôi lầm, nhưng theo ý tôi, đây thật sự là hơi thở của giáo dục và linh hồn của một chốn được gọi là trường học.” - (Trích tác phẩm)
Trong ngày khai giảng, nhờ sự xuất hiện vào phút chót của cậu bé thiểu năng trí tuệ Harun, trường Muhammadiyah may mắn thoát khỏi nguy cơ đóng cửa. Nhưng ước mơ dạy và học trong ngôi trường Hồi giáo ấy liệu sẽ đi về đâu, khi ngôi trường xập xệ dường như sẵn sàng sụp xuống bất cứ lúc nào, khi lời đe dọa đóng cửa từ viên thanh tra giáo dục luôn lơ lửng trên đầu, khi những cỗ máy xúc hung dữ đang chực chờ xới tung ngôi trường để dò mạch thiếc…? Và liệu niềm đam mê học tập của những Chiến binh Cầu vồng đó có đủ sức chinh phục quãng đường ngày ngày đạp xe bốn mươi cây số, rồi đầm cá sấu lúc nhúc bọn ăn thịt người, chưa kể sự mê hoặc từ những chuyến phiêu lưu chết người theo tiếng gọi của ngài pháp sư bí ẩn trên đảo Hải Tặc, cùng sức cám dỗ khôn cưỡng từ những đồng tiền còm kiếm được nhờ công việc cu li toàn thời gian ...?
Chiến binh Cầu vồng có cả tình yêu trong sáng tuổi học trò lẫn những trò đùa tinh quái, cả nước mắt lẫn tiếng cười – một bức tranh chân thực về hố sâu ngăn cách giàu nghèo, một tác phẩm văn học cảm động truyền tải sâu sắc nhất ý nghĩa đích thực của việc làm thầy, việc làm trò và việc học.
Tác phẩm đã bán được trên năm triệu bản, được dịch ra 26 thứ tiếng, là một trong những đại diện xuất sắc nhất của  văn học Indonesia hiện đại.', 
1, "Tên Nhà Cung Cấp:	Nhã Nam
Tác giả:	Andrea Hirata
Người Dịch:	Dạ Thảo
NXB:	NXB Hội Nhà Văn
Năm XB:	2020
Trọng lượng (gr):	450
Kích Thước Bao Bì:	20.5 x 14 cm
Số trang:	428
Hình thức:	Bìa Mềm", 100);

-- Thêm dữ liệu vào bảng BookAuthor
INSERT INTO BookAuthor(book_id, author_id) VALUE(1,1);
INSERT INTO BookAuthor(book_id, author_id) VALUE(2,1);
INSERT INTO BookAuthor(book_id, author_id) VALUE(3,2);
INSERT INTO BookAuthor(book_id, author_id) VALUE(4,3);
INSERT INTO BookAuthor(book_id, author_id) VALUE(5,4);
INSERT INTO BookAuthor(book_id, author_id) VALUE(6,5);
INSERT INTO BookAuthor(book_id, author_id) VALUE(7,6);
INSERT INTO BookAuthor(book_id, author_id) VALUE(8,7);
INSERT INTO BookAuthor(book_id, author_id) VALUE(9,8);
INSERT INTO BookAuthor(book_id, author_id) VALUE(10,9);
INSERT INTO BookAuthor(book_id, author_id) VALUE(11,10);
INSERT INTO BookAuthor(book_id, author_id) VALUE(12,11);
INSERT INTO BookAuthor(book_id, author_id) VALUE(13,12);

-- Thêm dữ liệu vào bảng BookImage
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (1, "1", 1, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/001/1");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (1, "2", 2, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/001/2");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (1, "3", 3, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/001/3");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (1, "4", 4, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/001/4");

INSERT INTO BookImage(book_id, name, position, path) 
VALUE (2, "1", 1, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/002/1");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (2, "2", 2, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/002/2");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (2, "3", 3, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/002/3");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (2, "4", 4, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/002/4");

INSERT INTO BookImage(book_id, name, position, path) 
VALUE (3, "1", 1, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/003/1");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (3, "2", 2, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/003/2");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (3, "3", 3, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/003/3");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (3, "4", 4, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/003/4");

INSERT INTO BookImage(book_id, name, position, path) 
VALUE (4, "1", 1, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/004/1");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (4, "2", 2, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/004/2");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (4, "3", 3, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/004/3");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (4, "4", 4, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/004/4");

INSERT INTO BookImage(book_id, name, position, path) 
VALUE (5, "1", 1, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/005/1");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (5, "2", 2, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/005/2");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (5, "3", 3, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/005/3");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (5, "4", 4, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/005/4");

INSERT INTO BookImage(book_id, name, position, path) 
VALUE (6, "1", 1, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/006/1");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (6, "2", 2, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/006/2");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (6, "3", 3, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/006/3");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (6, "4", 4, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/006/4");

INSERT INTO BookImage(book_id, name, position, path) 
VALUE (7, "1", 1, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/007/1");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (7, "2", 2, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/007/2");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (7, "3", 3, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/007/3");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (7, "4", 4, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/007/4");

INSERT INTO BookImage(book_id, name, position, path) 
VALUE (8, "1", 1, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/008/1");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (8, "2", 2, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/008/2");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (8, "3", 3, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/008/3");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (8, "4", 4, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/008/4");

INSERT INTO BookImage(book_id, name, position, path) 
VALUE (9, "1", 1, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/009/1");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (9, "2", 2, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/009/2");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (9, "3", 3, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/009/3");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (9, "4", 4, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/009/4");

INSERT INTO BookImage(book_id, name, position, path) 
VALUE (10, "1", 1, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/010/1");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (10, "2", 2, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/010/2");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (10, "3", 3, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/010/3");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (10, "4", 4, "https://res.cloudinary.com/dylwewmt0/image/upload/v1694971592/PHOTO_LIST/Chinhtri_phapluat/010/4");

INSERT INTO BookImage(book_id, name, position, path) 
VALUE (11, "1", 1, "https://res.cloudinary.com/dylwewmt0/image/upload/v1695020353/PHOTO_LIST/Giao_trinh/001/1");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (11, "2", 2, "https://res.cloudinary.com/dylwewmt0/image/upload/v1695020353/PHOTO_LIST/Giao_trinh/001/2");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (11, "3", 3, "https://res.cloudinary.com/dylwewmt0/image/upload/v1695020353/PHOTO_LIST/Giao_trinh/001/3");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (11, "4", 4, "https://res.cloudinary.com/dylwewmt0/image/upload/v1695020353/PHOTO_LIST/Giao_trinh/001/4");

INSERT INTO BookImage(book_id, name, position, path) 
VALUE (12, "1", 1, "https://res.cloudinary.com/dylwewmt0/image/upload/v1695021586/PHOTO_LIST/Khoahoc_congnghe_kinhte/001/1");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (12, "2", 2, "https://res.cloudinary.com/dylwewmt0/image/upload/v1695021586/PHOTO_LIST/Khoahoc_congnghe_kinhte/001/2");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (12, "3", 3, "https://res.cloudinary.com/dylwewmt0/image/upload/v1695021586/PHOTO_LIST/Khoahoc_congnghe_kinhte/001/3");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (12, "4", 4, "https://res.cloudinary.com/dylwewmt0/image/upload/v1695021586/PHOTO_LIST/Khoahoc_congnghe_kinhte/001/4");

INSERT INTO BookImage(book_id, name, position, path) 
VALUE (13, "1", 1, "https://res.cloudinary.com/dylwewmt0/image/upload/v1695025933/PHOTO_LIST/Sach_truyen_tieuthuyet/001/1");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (13, "2", 2, "https://res.cloudinary.com/dylwewmt0/image/upload/v1695025933/PHOTO_LIST/Sach_truyen_tieuthuyet/001/2");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (13, "3", 3, "https://res.cloudinary.com/dylwewmt0/image/upload/v1695025933/PHOTO_LIST/Sach_truyen_tieuthuyet/001/3");
INSERT INTO BookImage(book_id, name, position, path) 
VALUE (13, "4", 4, "https://res.cloudinary.com/dylwewmt0/image/upload/v1695025933/PHOTO_LIST/Sach_truyen_tieuthuyet/001/4");

-- Thêm dữ liệu vào bảng Cart
INSERT INTO Cart(account_id) VALUES (3), (4);

-- Thêm dữ liệu vào bảng CartItem
INSERT INTO CartItem(quantity, cart_id, book_id) VALUES (2, 1, 1);
INSERT INTO CartItem(quantity, cart_id, book_id) VALUES (2, 1, 2);
INSERT INTO CartItem(quantity, cart_id, book_id) VALUES (2, 1, 3);
INSERT INTO CartItem(quantity, cart_id, book_id) VALUES (2, 1, 4);
INSERT INTO CartItem(quantity, cart_id, book_id) VALUES (2, 1, 5);
