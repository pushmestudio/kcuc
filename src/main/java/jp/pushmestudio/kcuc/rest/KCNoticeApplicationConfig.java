package jp.pushmestudio.kcuc.rest;

import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import io.swagger.jaxrs.config.BeanConfig;

@ApplicationPath("/api")
public class KCNoticeApplicationConfig extends Application {
	
	/**
	 * to be added
	 */
	public KCNoticeApplicationConfig() {
		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setTitle("KCUC");
		beanConfig.setDescription("Knowledge Center Update Checker");
		beanConfig.setVersion("1.0.2");
		beanConfig.setSchemes(new String[] { "http" });
		beanConfig.setHost("172.17.0.2:8080"); // 各自環境のdockerのIPアドレスに変更必要
		beanConfig.setBasePath("/kcuc/rest-v1"); // applicationのルートパスを指定
		beanConfig.setPrettyPrint(true);
		beanConfig.setResourcePackage("io.swagger.resources");
		beanConfig.setResourcePackage("jp.pushmestudio.kcuc.rest"); // KCNoticeResource内のapiを追加
		beanConfig.setScan(true);
	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> resources = new java.util.HashSet<>();
		addRestResourceClasses(resources);

		// enable Swagger
		resources.add(io.swagger.jaxrs.listing.ApiListingResource.class);
		resources.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);

		return resources;
	}
	
	private void addRestResourceClasses(Set<Class<?>> resources) {
		resources.add(KCNoticeResource.class);
	}
}