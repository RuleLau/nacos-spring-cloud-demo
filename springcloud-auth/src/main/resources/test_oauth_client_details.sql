
create table if not exists test.oauth_client_details
(
	client_id varchar(255) not null
		primary key,
	resource_ids varchar(255) null,
	client_secret varchar(255) null,
	scope varchar(255) null,
	authorized_grant_types varchar(255) null,
	web_server_redirect_uri varchar(255) null,
	authorities varchar(255) null,
	access_token_validity int null,
	refresh_token_validity int null,
	additional_information text null,
	autoapprove varchar(255) default 'false' null
)
charset=utf8;



INSERT INTO test.oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('nacos-service-provider', 'dev2', '$2a$10$cRpWB711xfJ9UyG/UzDY3ezZss5GSTH4yktYbb33XMpi9xSRbx0K.', 'all', 'password,authorization_code,refresh_token', 'http://localhost:9001/login', 'ROLE_NORMAL', 3600, 3600, '{"country":"CN","country_code":"086"}', 'true');
INSERT INTO test.oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('oauth-client', 'dev', '$2a$10$cRpWB711xfJ9UyG/UzDY3ezZss5GSTH4yktYbb33XMpi9xSRbx0K.', 'all', 'password,authorization_code,refresh_token', 'http://localhost:9001/login,http://localhost:9002/login', 'ROLE_NORMAL', 3600, 3600, '{"country":"CN","country_code":"086"}', 'true');