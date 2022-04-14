INSERT INTO roles(id,name) values (1,'ROLE_USER') on conflict (id) DO NOTHING ;
INSERT INTO roles(id,name) values (2,'ROLE_SELLER') on conflict (id) DO NOTHING ;
INSERT INTO roles(id,name) values (3,'ROLE_ADMIN') on conflict (id) DO NOTHING ;
