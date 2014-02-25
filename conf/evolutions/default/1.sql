# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table bd_cer (
  cer_serial_number         integer auto_increment not null,
  cer_tipo_archivo          varchar(255),
  cer_public_key            varchar(255),
  cer_type                  varchar(255),
  constraint pk_bd_cer primary key (cer_serial_number))
;

create table bd_crl (
  crl_number                integer auto_increment not null,
  crlnext_update_time       datetime,
  crl_issuer                varchar(255),
  crl_update_time           datetime,
  crl_uri                   varchar(255),
  crl_tipo_archivo          varchar(255),
  crl_version               integer,
  constraint pk_bd_crl primary key (crl_number))
;

create table bd_crl_revocado (
  id_revocacion             integer auto_increment not null,
  crl_serial_revocacion     integer,
  crl_fecha_revocacion      datetime,
  constraint pk_bd_crl_revocado primary key (id_revocacion))
;

create table bd_mft (
  mft_number                integer auto_increment not null,
  mft_tipo_archivo          varchar(255),
  mft_eeis_ca               tinyint(1) default 0,
  mft_signing_time          datetime,
  mft_eeissuer_name         varchar(255),
  mft_eenot_valid_after     datetime,
  mft_eenot_valid_before    datetime,
  mft_eeserial_number       integer,
  mft_not_valid_after       datetime,
  mft_eepublic_key          varchar(255),
  mft_eeprefijos            varchar(255),
  mft_version               integer,
  mft_size                  integer,
  mft_next_update_time      datetime,
  mft_parent_uri            varchar(255),
  mft_this_update_time      datetime,
  mft_eesubject_name        varchar(255),
  mft_not_valid_before      datetime,
  mft_files_id_mft_file     integer,
  constraint pk_bd_mft primary key (mft_number))
;

create table bd_mft_files (
  id_mft_file               integer auto_increment not null,
  mft_file_name             varchar(255),
  mft_file_hash             varchar(255),
  constraint pk_bd_mft_files primary key (id_mft_file))
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
  constraint pk_bd_roa primary key (roa_eeserial_number))
;

create table bd_roa_bloque (
  id_bloque                 integer auto_increment not null,
  prefijo                   varchar(255),
  largo                     integer,
  roa_roa_eeserial_number   integer,
  constraint pk_bd_roa_bloque primary key (id_bloque))
;

create table bd_roa_statement (
  id_statement              integer auto_increment not null,
  roa_roa_eeserial_number   integer,
  largo_maximo              integer,
  constraint pk_bd_roa_statement primary key (id_statement))
;

alter table bd_mft add constraint fk_bd_mft_mftFiles_1 foreign key (mft_files_id_mft_file) references bd_mft_files (id_mft_file) on delete restrict on update restrict;
create index ix_bd_mft_mftFiles_1 on bd_mft (mft_files_id_mft_file);
alter table bd_roa_bloque add constraint fk_bd_roa_bloque_roa_2 foreign key (roa_roa_eeserial_number) references bd_roa (roa_eeserial_number) on delete restrict on update restrict;
create index ix_bd_roa_bloque_roa_2 on bd_roa_bloque (roa_roa_eeserial_number);
alter table bd_roa_statement add constraint fk_bd_roa_statement_roa_3 foreign key (roa_roa_eeserial_number) references bd_roa (roa_eeserial_number) on delete restrict on update restrict;
create index ix_bd_roa_statement_roa_3 on bd_roa_statement (roa_roa_eeserial_number);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table bd_cer;

drop table bd_crl;

drop table bd_crl_revocado;

drop table bd_mft;

drop table bd_mft_files;

drop table bd_roa;

drop table bd_roa_bloque;

drop table bd_roa_statement;

SET FOREIGN_KEY_CHECKS=1;

