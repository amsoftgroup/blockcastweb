-- Table: public.op

-- DROP TABLE public.op;

CREATE TABLE public.op
(
  id bigint NOT NULL DEFAULT nextval('op_id_seq'::regclass),
  location geometry(Geometry,4326) NOT NULL,
  content character varying NOT NULL,
  parent_id bigint NOT NULL DEFAULT nextval('op_parent_id_seq'::regclass),
  post_timestamp timestamp without time zone,
  post_duration bigint,
  post_radius_meters bigint,
  CONSTRAINT pk_op PRIMARY KEY (id )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.op
  OWNER TO blockcast;
GRANT ALL ON TABLE public.op TO blockcast;
GRANT ALL ON TABLE public.op TO postgres;