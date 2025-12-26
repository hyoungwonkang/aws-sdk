package com.example.demo;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

public class DemoApplication {

	public static void main(String[] args) {

		// try-catch-resources: close 필요없는 try-catch

		// S3Client 생성

		try ( S3Client client = S3Client.builder()
				.region(Region.AP_NORTHEAST_2) // 서울 리전
				.build() ) {

			client.listBuckets();
			System.out.println("자격 증명 확인 성공!");

		} catch (Exception e) {

			System.out.println("연결 실패: 자격 증명 확인");
			e.printStackTrace();
		}
	
	}

}
