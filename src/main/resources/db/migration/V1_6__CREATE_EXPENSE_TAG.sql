CREATE TABLE IF NOT EXISTS public.expense_tag (
	expense_id uuid NOT NULL,
	tag_id uuid NOT NULL,
    CONSTRAINT expense_tag_pkey PRIMARY KEY (tag_id, expense_id),
	CONSTRAINT expense_tag_tag_id_fk FOREIGN KEY (tag_id) REFERENCES public.tag(id),
	CONSTRAINT expense_tag_expense_id_fk FOREIGN KEY (expense_id) REFERENCES public.expense(id)
);