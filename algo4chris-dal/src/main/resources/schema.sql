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

-- Table: public.member
-- DROP TABLE IF EXISTS public.member;
CREATE SEQUENCE IF NOT EXISTS public.member_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;
CREATE TABLE IF NOT EXISTS public.member
(
    id bigint NOT NULL,
    email character varying(50) COLLATE pg_catalog."default",
    password character varying(120) COLLATE pg_catalog."default",
    membername character varying(20) COLLATE pg_catalog."default",
    create_time timestamp without time zone,
    ip character varying(255) COLLATE pg_catalog."default",
    status integer,
    update_time timestamp without time zone,
    CONSTRAINT users_pkey PRIMARY KEY (id)
    )

    TABLESPACE pg_default;

alter table public.member alter column id set DEFAULT nextval('member_id_seq'::regclass);

-- Table: public.user_roles
-- DROP TABLE IF EXISTS public.user_roles;
CREATE TABLE IF NOT EXISTS public.member_roles
(
    member_id bigint NOT NULL,
    role_id integer NOT NULL,
    CONSTRAINT user_roles_pkey PRIMARY KEY (member_id, role_id),
    CONSTRAINT fkh8ciramu9cc9q3qcqiv4ue8a6 FOREIGN KEY (role_id)
    REFERENCES public.roles (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT fkhfh9dx7w3ubf1co1vdev94g3f FOREIGN KEY (member_id)
    REFERENCES public.member (id) MATCH SIMPLE
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
    member_id bigint,
    CONSTRAINT refreshtoken_pkey PRIMARY KEY (id),
    CONSTRAINT uk_or156wbneyk8noo4jstv55ii3 UNIQUE (token),
    CONSTRAINT fka652xrdji49m4isx38pp4p80p FOREIGN KEY (member_id)
    REFERENCES public.member (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    )

    TABLESPACE pg_default;

-- Table: public.blackwhitelist
-- DROP TABLE IF EXISTS public.blackwhitelist;
CREATE SEQUENCE IF NOT EXISTS public.blackwhitelist_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;
CREATE TABLE IF NOT EXISTS public.blackwhitelist
(
    id bigint NOT NULL,
    ip character varying(18) COLLATE pg_catalog."default" NOT NULL,
    type smallint,
    CONSTRAINT blackwhitelist_pkey PRIMARY KEY (id)
    )

    TABLESPACE pg_default;

COMMENT ON TABLE public.blackwhitelist
    IS '黑白名單';
-- Index: bw_index_01
-- DROP INDEX IF EXISTS public.bw_index_01;
CREATE INDEX IF NOT EXISTS bw_index_01
    ON public.blackwhitelist USING btree
    (type ASC NULLS LAST)
    TABLESPACE pg_default;

alter table public.blackwhitelist alter column id set DEFAULT nextval('blackwhitelist_id_seq'::regclass);

