-- Table: public.roles
-- DROP TABLE IF EXISTS public.roles;
CREATE SEQUENCE IF NOT EXISTS public.roles_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

CREATE TABLE IF NOT EXISTS public.roles
(
    id integer NOT NULL,
    name character varying(20) COLLATE pg_catalog."default",
    CONSTRAINT roles_pkey PRIMARY KEY (id)
    )

    TABLESPACE pg_default;

alter table public.roles alter column id set DEFAULT nextval('roles_id_seq'::regclass);

-- Table: public.users
-- DROP TABLE IF EXISTS public.users;
CREATE SEQUENCE IF NOT EXISTS public.users_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;
CREATE TABLE IF NOT EXISTS public.users
(
    id bigint NOT NULL,
    email character varying(50) COLLATE pg_catalog."default",
    password character varying(120) COLLATE pg_catalog."default",
    username character varying(20) COLLATE pg_catalog."default",
    create_time timestamp without time zone,
    ip character varying(255) COLLATE pg_catalog."default",
    status integer,
    update_time timestamp without time zone,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email),
    CONSTRAINT ukr43af9ap4edm43mmtq01oddj6 UNIQUE (username)
    )

    TABLESPACE pg_default;

alter table public.users alter column id set DEFAULT nextval('users_id_seq'::regclass);

-- Table: public.user_roles
-- DROP TABLE IF EXISTS public.user_roles;
CREATE TABLE IF NOT EXISTS public.user_roles
(
    user_id bigint NOT NULL,
    role_id integer NOT NULL,
    CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id),
    CONSTRAINT fkh8ciramu9cc9q3qcqiv4ue8a6 FOREIGN KEY (role_id)
    REFERENCES public.roles (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT fkhfh9dx7w3ubf1co1vdev94g3f FOREIGN KEY (user_id)
    REFERENCES public.users (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    )

    TABLESPACE pg_default;

-- Table: public.refreshtoken
-- DROP TABLE IF EXISTS public.refreshtoken;
CREATE TABLE IF NOT EXISTS public.refreshtoken
(
    id bigint NOT NULL,
    expiry_date timestamp without time zone NOT NULL,
    token character varying(255) COLLATE pg_catalog."default" NOT NULL,
    user_id bigint,
    CONSTRAINT refreshtoken_pkey PRIMARY KEY (id),
    CONSTRAINT uk_or156wbneyk8noo4jstv55ii3 UNIQUE (token),
    CONSTRAINT fka652xrdji49m4isx38pp4p80p FOREIGN KEY (user_id)
    REFERENCES public.users (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    )

    TABLESPACE pg_default;



