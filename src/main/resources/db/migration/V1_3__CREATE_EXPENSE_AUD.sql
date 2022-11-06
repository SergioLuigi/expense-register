CREATE TABLE IF NOT EXISTS public.expense_aud (
	id uuid NOT NULL,
	rev int4 NOT NULL,
	revtype int2 NULL,
	created_date timestamp NULL,
	description varchar(255) NULL,
	responsible_name varchar(50) NULL,
	updated_date timestamp NULL,
	value numeric(19, 2) NULL,
	CONSTRAINT expense_aud_pkey PRIMARY KEY (id, rev),
	CONSTRAINT expense_aud_rev_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);