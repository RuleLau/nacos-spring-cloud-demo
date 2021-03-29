
create table if not exists test.t_user
(
	ID int auto_increment
		primary key,
	USERNAME varchar(30) null,
	PASSWORD varchar(100) null,
	AUTHORITIES varchar(300) null
);


INSERT INTO test.t_user (ID, USERNAME, PASSWORD, AUTHORITIES) VALUES (1, 'root', '123456', null);
INSERT INTO test.t_user (ID, USERNAME, PASSWORD, AUTHORITIES) VALUES (2, 'admin', '$2a$10$aFBK2RTTxNAaHmbphq7itu3IcaukHTTiH6R8Q7O.ajs27INYXwBHW', 'ROLE_ADMIN');