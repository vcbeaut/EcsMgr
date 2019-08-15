package ecsmgr.ecsarti;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.CreateLaunchTemplateRequest;
import com.aliyuncs.ecs.model.v20140526.CreateLaunchTemplateResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeLaunchTemplatesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeLaunchTemplatesResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeLaunchTemplatesResponse.LaunchTemplateSet;
import com.aliyuncs.ecs.model.v20140526.RunInstancesRequest;
import com.aliyuncs.ecs.model.v20140526.RunInstancesResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;

public class EcsMgr {
	static String LaunchTemplateId;
	static String LaunchTemplateName = "MyStartTemplate";
	//"lt-wz9c03srb2pfg85ulll5";
	static int Amount = 1;
	public static void main(String[] args) {
		System.setProperty("http.proxySet", "true");
		System.setProperty("http.proxyHost", "127.0.0.1");
		System.setProperty("http.proxyPort", "8888");
		
			DefaultProfile profile = DefaultProfile.getProfile("XXX","XXX","XXX");
			IAcsClient client = new DefaultAcsClient(profile);
			
			System.out.print("请输入欲创建ECS实例的数量(1-100)：");
			Scanner sc = new Scanner(System.in);
			Amount = sc.nextInt();
			List<LaunchTemplateSet> tempList = DescribeLaunchTemplates(client,LaunchTemplateName);
			if(tempList.size() == 0) {
				CreateLaunchTemplate(client);
				List<String> list = RunInstances(client,Amount);
				for(int i = 0; i < list.size(); i++) {
					System.out.println(list.get(i));
				}
			}else {
				LaunchTemplateId = tempList.get(0).getLaunchTemplateId();
				//System.out.println("LaunchTemplateId 已存在");
				List<String> list = RunInstances(client,Amount);
				for(int i = 0; i < list.size(); i++) {
					System.out.println(list.get(i));
				}
			}
			
	}
	
	public static void DescribeLaunchTemplates(IAcsClient client) {
		DescribeLaunchTemplatesRequest request = new DescribeLaunchTemplatesRequest();
		DescribeLaunchTemplatesResponse response;
		try {
			response = client.getAcsResponse(request);

		} catch (ServerException e) {
		    e.printStackTrace();
		} catch (ClientException e) {
		    e.printStackTrace();
		}
	}
	
	public static String CreateLaunchTemplate(IAcsClient client) {
		CreateLaunchTemplateRequest request = new CreateLaunchTemplateRequest();
		//LaunchTemplateName = "test3";
		request.setLaunchTemplateName(LaunchTemplateName);
		request.setImageId("m-wz97bl71hqnrytsl1d6r");
		request.setInstanceType("ecs.t5-lc1m2.small");
		request.setSecurityGroupId("sg-wz9c1jbwyjdi7whf9p7d");
		request.setInternetMaxBandwidthOut(100);
		CreateLaunchTemplateResponse response;
		try {
			response = client.getAcsResponse(request);
			LaunchTemplateId = response.getLaunchTemplateId();
			return LaunchTemplateId;

		} catch (ServerException e) {
		    e.printStackTrace();
		} catch (ClientException e) {
		    e.printStackTrace();
		}
		return "";
	}
	
	
	public static List<String> RunInstances(IAcsClient client,int Amount) {
		RunInstancesRequest request = new RunInstancesRequest();
		request.setLaunchTemplateId(LaunchTemplateId);
		request.setLaunchTemplateName(LaunchTemplateName);
		request.setVSwitchId("vsw-wz94hgtdt9ffwcaecs5ok");
		request.setAmount(Amount);
		RunInstancesResponse response = new RunInstancesResponse();
		try {
			response = client.getAcsResponse(request);
			List<String> list = response.getInstanceIdSets();
			return list;
		} catch (ServerException e) {
		    e.printStackTrace();
		    return null;
		} catch (ClientException e) {
		    e.printStackTrace();
		    return null;
		}
	}
	
	public static List<LaunchTemplateSet> DescribeLaunchTemplates(IAcsClient client,String LaunchTemplateName) {
		DescribeLaunchTemplatesRequest request = new DescribeLaunchTemplatesRequest();
		List<String> listTemp = new ArrayList<String>();
		listTemp.add(LaunchTemplateName);
		request.setLaunchTemplateNames(listTemp);
		
		DescribeLaunchTemplatesResponse response = new DescribeLaunchTemplatesResponse();
		try {
			response = client.getAcsResponse(request);
			List<LaunchTemplateSet> responseList = response.getLaunchTemplateSets();
			return responseList;
//			for(int i = 0; i < responseList.size(); i++) {
//				System.out.println(responseList.get(i).getLaunchTemplateId());
//			}
		} catch (ServerException e) {
		    e.printStackTrace();
		} catch (ClientException e) {
		    e.printStackTrace();
		}
		return null;
	}
}
