INSERT INTO roles(name) SELECT 'ROLE_USER' where not exists (select 1 from roles where id = 1);
INSERT INTO roles(name) SELECT 'ROLE_SELLER' where not exists (select 1 from roles where id = 2);
INSERT INTO roles(name) SELECT 'ROLE_ADMIN' where not exists (select 1 from roles where id = 3);