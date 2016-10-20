package jp.pushmestudio.kcuc.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@ApplicationPath("/rest-v1")
public class KCNoticeApplication extends Application {
	// 従来的にresponseを返したいならここで既定したものを使うとよい
	static final Response RESP_OK = Response.ok().build();
	static final Response RESP_ACCEPTED = Response.status(Status.ACCEPTED).build();
	static final Response RESP_CREATED = Response.status(Status.CREATED).build();
	static final Response RESP_NO_CONTENT = Response.status(Status.NO_CONTENT).build();

	static final Response RESP_BAD_REQUEST = Response.status(Status.BAD_REQUEST).build();
	static final Response RESP_NOT_FOUND = Response.status(Status.NOT_FOUND).build();

	static final Response RESP_INTERNAL_SERVER_ERROR = Response.status(Status.INTERNAL_SERVER_ERROR).build();
}
