package com.nhom14.webbookstore.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nhom14.webbookstore.service.CloudinaryService;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

	private Cloudinary cloudinary;

    public CloudinaryServiceImpl() {
        // Khởi tạo đối tượng Cloudinary với các thông tin xác thực của bạn
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dosdzo1lg",
                "api_key", "471512853679338",
                "api_secret", "tmvGJJ8g7bRQKvhSNDXZRvD4WDI"
        ));
    }

    @Override
    public String uploadImage(MultipartFile image, String publicId) throws IOException {
        // Đọc dữ liệu từ InputStream và lưu vào một mảng byte
        byte[] imageData = image.getBytes();

        // Tải lên hình ảnh lên Cloudinary
        @SuppressWarnings("unchecked")
        Map<String, Object> uploadResult = (Map<String, Object>) cloudinary.uploader().upload(imageData, ObjectUtils.asMap(
                "public_id", publicId
        ));

        // Lấy URL của hình ảnh từ kết quả tải lên
        return (String) uploadResult.get("secure_url");
    }

    @Override
    public String uploadImageFromUrl(String imageUrl, String publicId) throws IOException {
    	// Tạo một đối tượng URI từ đường dẫn hình ảnh trên Cloudinary
        URI uri = URI.create(imageUrl);

        // Tạo một đối tượng URL từ đối tượng URI
        URL url = uri.toURL();

        // Tạo một đối tượng HttpURLConnection để kết nối và tải xuống hình ảnh từ URL
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Lấy InputStream để đọc dữ liệu hình ảnh
        InputStream inputStream = connection.getInputStream();

        // Đọc dữ liệu từ InputStream và lưu vào một mảng byte
        byte[] imageData = inputStream.readAllBytes();

        // Tải lên hình ảnh lên Cloudinary
        @SuppressWarnings("unchecked")
        Map<String, Object> uploadResult = (Map<String, Object>) cloudinary.uploader().upload(imageData, ObjectUtils.asMap(
                "public_id", publicId
        ));

        // Lấy URL của hình ảnh từ kết quả tải lên
        return (String) uploadResult.get("secure_url");
    }

}
