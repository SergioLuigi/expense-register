CREATE TABLE IF NOT EXISTS public.bill (
	id uuid not null,
	code uuid not null unique,
	value numeric(19, 2) not null,
	expire_at date not null,
	constraint bill_pke primary key(id)
);