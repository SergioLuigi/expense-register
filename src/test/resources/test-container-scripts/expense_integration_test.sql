INSERT INTO public.expense (id, created_date, description, responsible_name, updated_date, value) VALUES('67ac4324-61e7-4e25-8206-7b91f032e597'::uuid, '2022-11-02 18:29:43.516', 'Description1', 'responsibleName1', '2022-11-02 18:29:43.516', 0.00);
INSERT INTO public.expense (id, created_date, description, responsible_name, updated_date, value) VALUES('25286978-a847-4cd9-8771-e1538694e035'::uuid, '2022-11-02 18:30:17.778', 'Description2', 'responsibleName2', '2022-11-02 18:30:17.778', 0.00);
INSERT INTO public.expense (id, created_date, description, responsible_name, updated_date, value) VALUES('d8632b04-0812-48df-9fef-8a784481779c'::uuid, '2022-11-02 18:30:40.475', 'Description3', 'responsibleName3', '2022-11-02 18:30:40.475', 0.00);
INSERT INTO public.tag (id, created_date, description, updated_date) VALUES('5e335989-af03-47f1-86cc-0e4f2820d9b2'::uuid, '2022-11-02 18:29:43.446', 'Car', '2022-11-02 18:29:43.446');
INSERT INTO public.tag (id, created_date, description, updated_date) VALUES('052f6b6d-a57e-4753-be51-07b5996084b4'::uuid, '2022-11-02 18:29:43.469', 'Cat', '2022-11-02 18:29:43.469');
INSERT INTO public.tag (id, created_date, description, updated_date) VALUES('df3fa9b1-d0bc-4236-98f3-0d1a5e2958a1'::uuid, '2022-11-02 18:29:43.469', 'Turtle', '2022-11-02 18:29:43.469');
INSERT INTO public.tag (id, created_date, description, updated_date) VALUES('807ab39f-f61b-47a1-a753-9a2a48e6a161'::uuid, '2022-11-02 18:29:43.470', 'Papper', '2022-11-02 18:29:43.470');
INSERT INTO public.tag (id, created_date, description, updated_date) VALUES('ad3c2267-0054-429a-92a9-b67d3e3dbb2c'::uuid, '2022-11-02 18:30:17.762', 'Left', '2022-11-02 18:30:17.762');
INSERT INTO public.tag (id, created_date, description, updated_date) VALUES('7a1846a9-956d-47af-ba1b-4faee5ccc4d4'::uuid, '2022-11-02 18:30:17.763', 'Right', '2022-11-02 18:30:17.763');
INSERT INTO public.tag (id, created_date, description, updated_date) VALUES('b032c303-9336-46ef-9bb5-1ed450ec9869'::uuid, '2022-11-02 18:30:17.763', 'Center', '2022-11-02 18:30:17.763');
INSERT INTO public.tag (id, created_date, description, updated_date) VALUES('e7a8a108-8ab1-4b88-8b2a-37a13c9193a0'::uuid, '2022-11-02 18:30:40.462', 'Sky', '2022-11-02 18:30:40.462');
INSERT INTO public.tag (id, created_date, description, updated_date) VALUES('759b4b0c-7042-4055-aae3-cf0f97e158ea'::uuid, '2022-11-02 18:30:40.462', 'Sea', '2022-11-02 18:30:40.462');
INSERT INTO public.tag (id, created_date, description, updated_date) VALUES('2026ef67-c3f2-42d4-9651-d16c5bff8df4'::uuid, '2022-11-02 18:30:40.462', 'Store', '2022-11-02 18:30:40.462');
INSERT INTO public.expense_tag (expense_id, tag_id) VALUES('67ac4324-61e7-4e25-8206-7b91f032e597'::uuid, '5e335989-af03-47f1-86cc-0e4f2820d9b2'::uuid);
INSERT INTO public.expense_tag (expense_id, tag_id) VALUES('67ac4324-61e7-4e25-8206-7b91f032e597'::uuid, '052f6b6d-a57e-4753-be51-07b5996084b4'::uuid);
INSERT INTO public.expense_tag (expense_id, tag_id) VALUES('67ac4324-61e7-4e25-8206-7b91f032e597'::uuid, 'df3fa9b1-d0bc-4236-98f3-0d1a5e2958a1'::uuid);
INSERT INTO public.expense_tag (expense_id, tag_id) VALUES('67ac4324-61e7-4e25-8206-7b91f032e597'::uuid, '807ab39f-f61b-47a1-a753-9a2a48e6a161'::uuid);
INSERT INTO public.expense_tag (expense_id, tag_id) VALUES('25286978-a847-4cd9-8771-e1538694e035'::uuid, 'ad3c2267-0054-429a-92a9-b67d3e3dbb2c'::uuid);
INSERT INTO public.expense_tag (expense_id, tag_id) VALUES('25286978-a847-4cd9-8771-e1538694e035'::uuid, '7a1846a9-956d-47af-ba1b-4faee5ccc4d4'::uuid);
INSERT INTO public.expense_tag (expense_id, tag_id) VALUES('25286978-a847-4cd9-8771-e1538694e035'::uuid, 'b032c303-9336-46ef-9bb5-1ed450ec9869'::uuid);
INSERT INTO public.expense_tag (expense_id, tag_id) VALUES('d8632b04-0812-48df-9fef-8a784481779c'::uuid, 'e7a8a108-8ab1-4b88-8b2a-37a13c9193a0'::uuid);
INSERT INTO public.expense_tag (expense_id, tag_id) VALUES('d8632b04-0812-48df-9fef-8a784481779c'::uuid, '759b4b0c-7042-4055-aae3-cf0f97e158ea'::uuid);
INSERT INTO public.expense_tag (expense_id, tag_id) VALUES('d8632b04-0812-48df-9fef-8a784481779c'::uuid, '2026ef67-c3f2-42d4-9651-d16c5bff8df4'::uuid);

