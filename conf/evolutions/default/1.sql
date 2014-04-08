# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table bd_cer (
  name                      varchar(255) not null,
  cer_numero_serial         integer,
  cer_uri_repositorio       varchar(255),
  cer_uri_crl               varchar(255),
  cer_aia                   varchar(255),
  cer_sia                   varchar(255),
  cer_uri_manifiesto        varchar(255),
  cer_emisor                varchar(255),
  cer_sujeto                varchar(255),
  cer_uri_padre             varchar(255),
  cer_fecha_inicio          datetime,
  cer_fecha_fin             datetime,
  cer_fecha_borrado         datetime,
  cer_es_ca                 tinyint(1) default 0,
  cer_clave_publica         varchar(255),
  cer_identificador_clave_publica varchar(255),
  cer_id_algoritmo_firma    varchar(255),
  cer_nombre_algoritmo_firma varchar(255),
  cer_firma                 varchar(255),
  cer_tipo                  varchar(255),
  cer_version               integer,
  cer_soporta_extension     tinyint(1) default 0,
  cer_uso_extendido_de_clave varchar(255),
  cer_restricciones_basicas integer,
  constraint pk_bd_cer primary key (name))
;

create table bd_cer_clave_de_uso (
  id_bloque                 integer auto_increment not null,
  bd_cer_name               varchar(255) not null,
  digital_singature         tinyint(1) default 0,
  non_repudiation           tinyint(1) default 0,
  key_encipherment          tinyint(1) default 0,
  data_encipherment         tinyint(1) default 0,
  key_agreement             tinyint(1) default 0,
  key_cert_sign             tinyint(1) default 0,
  crl_sign                  tinyint(1) default 0,
  encipher_only             tinyint(1) default 0,
  decipher_only             tinyint(1) default 0,
  constraint pk_bd_cer_clave_de_uso primary key (id_bloque))
;

create table bd_cer_his (
  id_cer                    integer auto_increment not null,
  bd_cer_name               varchar(255) not null,
  cer_fecha_cambio          datetime,
  cer_valor_anterior        varchar(255),
  cer_valor_actual          varchar(255),
  cer_tipo_cambio           varchar(255),
  constraint pk_bd_cer_his primary key (id_cer))
;

create table bd_cer_recurso (
  id_bloque                 integer auto_increment not null,
  bd_cer_name               varchar(255) not null,
  asn                       varchar(255),
  prefijo                   varchar(255),
  largo                     integer,
  constraint pk_bd_cer_recurso primary key (id_bloque))
;

create table bd_crl (
  crl_issuer                varchar(255) not null,
  crl_number                integer,
  crlnext_update_time       datetime,
  crl_update_time           datetime,
  crl_uri                   varchar(255),
  crl_version               integer,
  crl_fecha_borrado         datetime,
  constraint pk_bd_crl primary key (crl_issuer))
;

create table bd_crl_his (
  id_crl                    integer auto_increment not null,
  bd_crl_crl_issuer         varchar(255) not null,
  crl_fecha_cambio          datetime,
  crl_valor_anterior        varchar(255),
  crl_valor_actual          varchar(255),
  crl_tipo_cambio           varchar(255),
  constraint pk_bd_crl_his primary key (id_crl))
;

create table bd_crl_revocado (
  id_revocacion             integer auto_increment not null,
  bd_crl_crl_issuer         varchar(255) not null,
  crl_serial_revocacion     integer,
  crl_fecha_revocacion      datetime,
  constraint pk_bd_crl_revocado primary key (id_revocacion))
;

create table bd_mft (
  mft_eeissuer_name         varchar(255) not null,
  mft_number                integer,
  mft_eeis_ca               tinyint(1) default 0,
  mft_signing_time          datetime,
  mft_eenot_valid_after     datetime,
  mft_eenot_valid_before    datetime,
  mft_eeserial_number       integer,
  mft_not_valid_after       datetime,
  mft_eepublic_key          varchar(255),
  mft_version               integer,
  mft_size                  integer,
  mft_next_update_time      datetime,
  mft_parent_uri            varchar(255),
  mft_this_update_time      datetime,
  mft_eesubject_name        varchar(255),
  mft_not_valid_before      datetime,
  mft_fecha_borrado         datetime,
  mft_eeprefijos            tinyint(1) default 0,
  constraint pk_bd_mft primary key (mft_eeissuer_name))
;

create table bd_mft_files (
  id_mft_file               integer auto_increment not null,
  bd_mft_mft_eeissuer_name  varchar(255) not null,
  mft_file_name             varchar(255),
  mft_file_hash             varchar(255),
  mft_file_uri              varchar(255),
  constraint pk_bd_mft_files primary key (id_mft_file))
;

create table bd_mft_his (
  id_mft                    integer auto_increment not null,
  bd_mft_mft_eeissuer_name  varchar(255) not null,
  mft_fecha_cambio          datetime,
  mft_valor_anterior        varchar(255),
  mft_valor_actual          varchar(255),
  mft_tipo_cambio           varchar(255),
  constraint pk_bd_mft_his primary key (id_mft))
;

create table bd_roa (
  roa_eeserial_number       integer auto_increment not null,
  roa_not_valid_before      datetime,
  roa_not_valid_after       datetime,
  roa_asn                   integer,
  roa_signing_time          datetime,
  roa_crl_uri               varchar(255),
  roa_parent_certificate_uri varchar(255),
  roa_eesubject_name        varchar(255),
  roa_eeissuer_name         varchar(255),
  roa_content_type          varchar(255),
  roa_eepublic_key          varchar(255),
  roa_eenot_valid_before    datetime,
  roa_eenot_valid_after     datetime,
  roa_eeis_ca               tinyint(1) default 0,
  roa_fecha_borrado         datetime,
  constraint pk_bd_roa primary key (roa_eeserial_number))
;

create table bd_roa_bloque (
  id_bloque                 integer auto_increment not null,
  bd_roa_roa_eeserial_number integer not null,
  bloque_prefijo_start      varchar(255),
  bloque_prefijo_end        varchar(255),
  constraint pk_bd_roa_bloque primary key (id_bloque))
;

create table bd_roa_his (
  id_roa                    integer auto_increment not null,
  bd_roa_roa_eeserial_number integer not null,
  roa_fecha_cambio          datetime,
  roa_valor_anterior        varchar(255),
  roa_valor_actual          varchar(255),
  roa_tipo_cambio           varchar(255),
  constraint pk_bd_roa_his primary key (id_roa))
;

create table bd_roa_statement (
  id_statement              integer auto_increment not null,
  st_prefijo_start          varchar(255),
  st_prefijo_end            varchar(255),
  largo_maximo              integer,
  roa_roa_eeserial_number   integer,
  constraint pk_bd_roa_statement primary key (id_statement))
;

alter table bd_cer_clave_de_uso add constraint fk_bd_cer_clave_de_uso_bd_cer_1 foreign key (bd_cer_name) references bd_cer (name) on delete restrict on update restrict;
create index ix_bd_cer_clave_de_uso_bd_cer_1 on bd_cer_clave_de_uso (bd_cer_name);
alter table bd_cer_his add constraint fk_bd_cer_his_bd_cer_2 foreign key (bd_cer_name) references bd_cer (name) on delete restrict on update restrict;
create index ix_bd_cer_his_bd_cer_2 on bd_cer_his (bd_cer_name);
alter table bd_cer_recurso add constraint fk_bd_cer_recurso_bd_cer_3 foreign key (bd_cer_name) references bd_cer (name) on delete restrict on update restrict;
create index ix_bd_cer_recurso_bd_cer_3 on bd_cer_recurso (bd_cer_name);
alter table bd_crl_his add constraint fk_bd_crl_his_bd_crl_4 foreign key (bd_crl_crl_issuer) references bd_crl (crl_issuer) on delete restrict on update restrict;
create index ix_bd_crl_his_bd_crl_4 on bd_crl_his (bd_crl_crl_issuer);
alter table bd_crl_revocado add constraint fk_bd_crl_revocado_bd_crl_5 foreign key (bd_crl_crl_issuer) references bd_crl (crl_issuer) on delete restrict on update restrict;
create index ix_bd_crl_revocado_bd_crl_5 on bd_crl_revocado (bd_crl_crl_issuer);
alter table bd_mft_files add constraint fk_bd_mft_files_bd_mft_6 foreign key (bd_mft_mft_eeissuer_name) references bd_mft (mft_eeissuer_name) on delete restrict on update restrict;
create index ix_bd_mft_files_bd_mft_6 on bd_mft_files (bd_mft_mft_eeissuer_name);
alter table bd_mft_his add constraint fk_bd_mft_his_bd_mft_7 foreign key (bd_mft_mft_eeissuer_name) references bd_mft (mft_eeissuer_name) on delete restrict on update restrict;
create index ix_bd_mft_his_bd_mft_7 on bd_mft_his (bd_mft_mft_eeissuer_name);
alter table bd_roa_bloque add constraint fk_bd_roa_bloque_bd_roa_8 foreign key (bd_roa_roa_eeserial_number) references bd_roa (roa_eeserial_number) on delete restrict on update restrict;
create index ix_bd_roa_bloque_bd_roa_8 on bd_roa_bloque (bd_roa_roa_eeserial_number);
alter table bd_roa_his add constraint fk_bd_roa_his_bd_roa_9 foreign key (bd_roa_roa_eeserial_number) references bd_roa (roa_eeserial_number) on delete restrict on update restrict;
create index ix_bd_roa_his_bd_roa_9 on bd_roa_his (bd_roa_roa_eeserial_number);
alter table bd_roa_statement add constraint fk_bd_roa_statement_roa_10 foreign key (roa_roa_eeserial_number) references bd_roa (roa_eeserial_number) on delete restrict on update restrict;
create index ix_bd_roa_statement_roa_10 on bd_roa_statement (roa_roa_eeserial_number);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table bd_cer;

drop table bd_cer_clave_de_uso;

drop table bd_cer_his;

drop table bd_cer_recurso;

drop table bd_crl;

drop table bd_crl_his;

drop table bd_crl_revocado;

drop table bd_mft;

drop table bd_mft_files;

drop table bd_mft_his;

drop table bd_roa;

drop table bd_roa_bloque;

drop table bd_roa_his;

drop table bd_roa_statement;

SET FOREIGN_KEY_CHECKS=1;

