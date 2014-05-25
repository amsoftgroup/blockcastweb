-- Table: op

-- DROP TABLE op;

CREATE TABLE op
(
  id bigserial NOT NULL,
  location geometry NOT NULL,
  content character varying NOT NULL,
  parent_id bigserial NOT NULL,
  post_timestamp timestamp without time zone,
  CONSTRAINT pk_op PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE op
  OWNER TO postgres;
