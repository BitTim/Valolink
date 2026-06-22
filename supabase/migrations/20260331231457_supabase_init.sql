grant all on all tables in schema public to service_role;
grant usage on schema public to anon, authenticated;
grant select, insert, update, delete on all tables in schema public to authenticated;
grant select on all tables in schema public to anon;
grant usage, select on all sequences in schema public to authenticated, anon;

-- Ensure future tables also get grants
alter default privileges in schema public
    grant all on tables to service_role;
alter default privileges in schema public
    grant select, insert, update, delete on tables to authenticated;
alter default privileges in schema public
    grant select on tables to anon;