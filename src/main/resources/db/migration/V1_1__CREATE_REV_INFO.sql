create TABLE IF NOT EXISTS public.revinfo (
    REV integer generated by default as identity,
    REVTSTMP int8,
    constraint revinfo_pkey primary key (REV)
)