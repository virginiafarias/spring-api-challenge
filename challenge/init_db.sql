--
-- PostgreSQL database dump
--

-- Dumped from database version 12.3
-- Dumped by pg_dump version 12.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

create table if not exists public.user_t
(
	id serial
		constraint user_t_pkey
			primary key,
	admin boolean not null,
	created_date timestamp,
	email varchar(255),
	github varchar(255),
	login varchar(255),
	name varchar(255),
	password varchar(255),
	updated_date timestamp
);

INSERT INTO public.user_t (admin, created_date, email, github, login, name, password, updated_date) VALUES (true, '2021-08-27 12:11:52.904666', 'user1@mail.com', 'jmilkiewicz', 'user1', 'User 1', '$2a$12$NLdMnAZdOWqVMDn7OHb55.l4J0xHcDcEv6CZT/2DFNTTPUsNuhQ0.', '2021-08-29 19:26:57.000000');
INSERT INTO public.user_t (admin, created_date, email, github, login, name, password, updated_date) VALUES (false, '2021-08-27 12:11:52.904666', 'user2@mail.com', 'user2', 'user2', 'User 2', '$2a$12$NLdMnAZdOWqVMDn7OHb55.l4J0xHcDcEv6CZT/2DFNTTPUsNuhQ0.', '2021-08-27 12:11:52.904666');
INSERT INTO public.user_t (admin, created_date, email, github, login, name, password, updated_date) VALUES (false, '2021-08-27 12:11:52.904666', 'user3@mail.com', 'user3', 'user3', 'User 3', '$2a$12$NLdMnAZdOWqVMDn7OHb55.l4J0xHcDcEv6CZT/2DFNTTPUsNuhQ0.', '2021-08-27 12:11:52.904666');
