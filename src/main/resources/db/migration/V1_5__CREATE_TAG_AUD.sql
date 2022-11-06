CREATE TABLE IF NOT EXISTS public.tag_aud (
	id uuid NOT NULL,
    rev int4 NOT NULL,
	revtype int2 NULL,
	created_date timestamp NULL,
	description varchar(255) NULL,
	updated_date timestamp NULL,
	CONSTRAINT tag_aud_pkey PRIMARY KEY (id, rev),
	CONSTRAINT tag_aud_rev_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);