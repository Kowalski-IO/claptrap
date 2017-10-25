DROP SCHEMA IF EXISTS claptrap;
CREATE SCHEMA claptrap;

SET SCHEMA claptrap;

CREATE TABLE email
(
  id UUID PRIMARY KEY,
  received TIMESTAMP NOT NULL,
  environment TEXT NOT NULL,
);

CREATE TABLE header
(
  id UUID PRIMARY KEY,
  email_id UUID NOT NULL,
  message_id TEXT NOT NULL,
  date TIMESTAMP NOT NULL,
  subject TEXT NOT NULL,
  content_type TEXT NOT NULL,
  CONSTRAINT email_header_fk FOREIGN KEY (email_id) REFERENCES email (id) ON DELETE CASCADE
);

CREATE TABLE body
(
  id UUID PRIMARY KEY,
  email_id UUID NOT NULL,
  html TEXT,
  plain_text TEXT,
  CONSTRAINT email_body_fk FOREIGN KEY (email_id) REFERENCES email (id) ON DELETE CASCADE
);

CREATE TABLE contact
(
  id UUID PRIMARY KEY,
  email_id UUID NOT NULL,
  email TEXT NOT NULL,
  personal TEXT,
  type VARCHAR2(10) NOT NULL,
  CONSTRAINT contact_email_fk FOREIGN KEY (email_id) REFERENCES email (id) ON DELETE CASCADE
);

CREATE TABLE attachment
(
  id UUID PRIMARY KEY,
  email_id UUID NOT NULL,
  filename TEXT NOT NULL,
  content_type TEXT NOT NULL,
  CONSTRAINT attachment_body_fk FOREIGN KEY (email_id) REFERENCES email (id) ON DELETE CASCADE
);

CREATE INDEX email_id_index ON email (id);

CREATE INDEX header_email_id_index ON header (email_id);
CREATE INDEX body_email_id_index ON body (email_id);
CREATE INDEX contact_email_id_index ON contact (email_id);
CREATE INDEX attachment_email_id_index ON attachment (email_id);
