CREATE TABLE IF NOT EXISTS public.expense (
	id uuid NOT NULL,
	created_date timestamp NULL,
	description varchar(255) NOT NULL,
	responsible_name varchar(50) NOT NULL,
	updated_date timestamp NULL,
	value numeric(19, 2) NOT NULL,
	CONSTRAINT expense_pkey PRIMARY KEY (id)
);