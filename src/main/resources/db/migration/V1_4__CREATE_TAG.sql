CREATE TABLE IF NOT EXISTS public.tag (
	id uuid NOT NULL,
	created_date timestamp NULL,
	description varchar(255) NOT NULL UNIQUE,
	updated_date timestamp NULL,
	CONSTRAINT tag_pkey PRIMARY KEY (id)
);