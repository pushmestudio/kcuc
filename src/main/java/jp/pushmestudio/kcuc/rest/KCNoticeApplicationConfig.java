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
		beanConfig.setVersion("0.3.0");
		beanConfig.setSchemes(new String[] {"http", "https"});
		// beanConfig.setHost("172.17.0.2:8080"); // Hostの設定値は省略すると稼働しているホストになる想定のため設定呼び出しをコメントアウト
		beanConfig.setBasePath("/kcuc/rest-v1"); // context＋applicationのルートパスを指定
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
