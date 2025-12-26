package com.example.demo;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.GetCallerIdentityResponse;

public class StsTest {
    public static void main(String[] args) {
        try ( StsClient stsClient = StsClient.builder()
                .region(Region.AP_NORTHEAST_2)
                .build() ) {

                GetCallerIdentityResponse identity = 
                stsClient.getCallerIdentity();
			System.out.println(identity.account());
			System.out.println(identity.arn());
        
        } catch (Exception e) {
			System.out.println("실패");

            e.printStackTrace();
        }
    }
}