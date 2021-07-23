CREATE TABLE IF NOT EXISTS "Street"
(
    id bigint NOT NULL,
    name character varying NOT NULL,
    CONSTRAINT "Street_pkey" PRIMARY KEY (id)
);

	
CREATE SEQUENCE IF NOT EXISTS "Location_id_seq";
CREATE TABLE IF NOT EXISTS "Location"
(
    id bigint NOT NULL DEFAULT nextval('"Location_id_seq"'),
    latitude double precision NOT NULL,
    longitude double precision NOT NULL,
    "street-id" bigint NOT NULL,
	CONSTRAINT "Location_pkey" PRIMARY KEY (id),
    CONSTRAINT "street-id" FOREIGN KEY ("street-id")
        REFERENCES "Street" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

	
	

CREATE SEQUENCE IF NOT EXISTS "Outcome-status_id_seq";
CREATE TABLE IF NOT EXISTS "Outcome-status"
(
    id bigint NOT NULL DEFAULT nextval('"Outcome-status_id_seq"'),
    category character varying NOT NULL,
    date date NOT NULL,
    CONSTRAINT "Outcome-status_pkey" PRIMARY KEY (id)
);



CREATE TABLE IF NOT EXISTS "Crimes"
(
    category character varying NOT NULL,
    context character varying NOT NULL,
    id bigint NOT NULL,
    "location-subtype" character varying NOT NULL,
    "location-type" character varying NOT NULL,
    month date NOT NULL,
    "persistent-id" character varying NOT NULL,
	"location-id" bigint NOT NULL,
	"outcome-status-id" bigint,
    CONSTRAINT Crimes_pkey PRIMARY KEY (id),
	CONSTRAINT "location-id" FOREIGN KEY ("location-id")
        REFERENCES "Location" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT "outcome-status-id" FOREIGN KEY ("outcome-status-id")
        REFERENCES "Outcome-status" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);



CREATE SEQUENCE IF NOT EXISTS "Stop-and-search_id_seq";
CREATE TABLE IF NOT EXISTS "Stop-and-search"
(
    type character varying NOT NULL,
    "involved-person" boolean NOT NULL,
    "date-time" date NOT NULL,
    operation boolean NOT NULL,
    "operation-name" character varying ,
    "location-id" bigint,
    gender character varying,
    "age-range" character varying,
    "self-defined-ethnicity" character varying,
    "officer-defined-ethnicity" character varying,
    legislation character varying,
    "object-of-search" character varying,
    outcome character varying NOT NULL,
    "outcome-linked-to-object-of-search" character varying,
    "removal-of-more-than-outer-clothing" boolean NOT NULL,
    id bigint NOT NULL DEFAULT nextval('"Stop-and-search_id_seq"'::regclass),
    CONSTRAINT "Stop-and-search_pkey" PRIMARY KEY (id),
	CONSTRAINT "location-id" FOREIGN KEY ("location-id")
        REFERENCES "Location" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)


